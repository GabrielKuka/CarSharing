package com.car.sharing.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.car.sharing.R
import com.car.sharing.databinding.FragmentViewPostBinding
import com.car.sharing.models.CarPhoto
import com.car.sharing.models.Post
import com.car.sharing.models.Rating
import com.car.sharing.ui.adapters.PhotoViewPagerAdapter
import com.car.sharing.ui.dialogs.TextDialog
import com.car.sharing.utils.IPostPhotos
import com.car.sharing.utils.IRating
import com.car.sharing.viewmodels.PostViewModel
import com.meet.quicktoast.Quicktoast

class ViewPostFragment : Fragment(), IPostPhotos, IRating {

    private lateinit var binder: FragmentViewPostBinding
    private lateinit var postViewModel: PostViewModel
    private lateinit var photoAdapter: PhotoViewPagerAdapter
    private lateinit var post: Post

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

}
