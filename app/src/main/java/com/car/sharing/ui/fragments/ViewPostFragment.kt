package com.car.sharing.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.car.sharing.R
import com.car.sharing.databinding.FragmentViewPostBinding
import com.car.sharing.models.CarPhoto
import com.car.sharing.models.Post
import com.car.sharing.models.Rating
import com.car.sharing.ui.adapters.PhotoViewPagerAdapter
import com.car.sharing.ui.dialogs.BasicDialog
import com.car.sharing.ui.dialogs.TextDialog
import com.car.sharing.utils.IPost
import com.car.sharing.utils.IPostPhotos
import com.car.sharing.utils.IRatingInteraction
import com.car.sharing.viewmodels.PostViewModel
import com.meet.quicktoast.Quicktoast

class ViewPostFragment : Fragment(), IPostPhotos, IRatingInteraction,
    PopupMenu.OnMenuItemClickListener, IPost {

    private lateinit var binder: FragmentViewPostBinding
    private lateinit var postViewModel: PostViewModel
    private lateinit var photoAdapter: PhotoViewPagerAdapter

    private lateinit var post: Post
    private lateinit var carPhotos: List<CarPhoto>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            post = it.getParcelable("post")!!
        }

        postViewModel = requireActivity().run {
            ViewModelProviders.of(this).get(PostViewModel::class.java)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binder.post = post
        binder.currentUser = postViewModel.getCurrentUser()

        postViewModel.retrievePhotos(post.postId, this)

        postViewModel.fetchPostRating(post.postId, this)

        postViewModel.hasUserRated(post.postId, this)

        binder.ratingButton.setOnClickListener {
            addRating()
        }

        binder.seeAllRatingsButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("postId", post.postId)

            val ratingsFragment = RatingsFragment()
            ratingsFragment.arguments = bundle

            addFragmentOnTop(ratingsFragment)
        }

        binder.postMenuButton.setOnClickListener {
            val popup = PopupMenu(requireActivity(), it)
            popup.setOnMenuItemClickListener(this)
            popup.inflate(R.menu.view_post_menu)
            popup.show()
        }

    }

    override fun onHasUserRated(rating: Rating) {
        binder.userRating = rating
    }

    override fun onPostRatingFetched(avg: Double) {

    }

    private fun addRating() {
        val rateValue = binder.ratingBar.rating
        val review = binder.rateReview.editText?.text.toString().trim()

        val rateId = postViewModel.getRatingsRef().push().key + "-" + post.postId

        val rating = Rating(
            rateId,
            rateValue.toDouble(),
            review,
            postViewModel.getCurrentUser()?.displayName!!,
            postViewModel.getCurrentUser()?.email!!,
            post.postId
        )

        postViewModel.addRating(rating, this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binder = DataBindingUtil.inflate(inflater, R.layout.fragment_view_post, container, false)

        return binder.root
    }

    override fun onErrorRetrieve(msg: String) {
        Quicktoast(requireActivity()).swarn(msg)
    }

    override fun onSuccessRetrieve(list: MutableList<CarPhoto>) {
        carPhotos = list
        photoAdapter = PhotoViewPagerAdapter(requireContext(), list)
        binder.photoViewPager.adapter = photoAdapter
    }

    override fun onAddedRating(msg: String) {
        Quicktoast(requireActivity()).sinfo(msg)
    }

    override fun onErrorRating(msg: String) {
        showTextDialog(msg)
    }

    private fun addFragmentOnTop(fragment: Fragment) {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .add(R.id.home_fragment_container, fragment)
            .addToBackStack("")
            .commit()
    }

    private fun showTextDialog(msg: String) {
        TextDialog(msg).show(requireActivity().supportFragmentManager, "")
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.delete_post_button -> {
                BasicDialog("Are you sure you want to delete this post?") {
                    requireActivity().supportFragmentManager.beginTransaction().detach(this)
                        .commit()
                    postViewModel.deletePost(post.postId, this)
                }.show(requireActivity().supportFragmentManager, "")
                true
            }

            R.id.edit_post_button -> {
                val bundle = Bundle()
                bundle.putParcelable("post", post)
                bundle.putParcelableArrayList("carPhotos", ArrayList<CarPhoto>(carPhotos))

                val fragment = AddEditPost()
                fragment.arguments = bundle

                addFragmentOnTop(fragment)
                true
            }
            else -> {
                false
            }
        }
    }


    override fun onDeleteError(msg: String) {
        showTextDialog(msg)
    }

}
