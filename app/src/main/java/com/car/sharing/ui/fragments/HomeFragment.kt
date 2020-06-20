package com.car.sharing.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.car.sharing.R
import com.car.sharing.databinding.FragmentHomeBinding
import com.car.sharing.models.Post
import com.car.sharing.ui.adapters.PhotoViewPagerAdapter
import com.car.sharing.ui.adapters.PostAdapter
import com.car.sharing.utils.IFetch
import com.car.sharing.viewmodels.HomeViewModel
import com.meet.quicktoast.Quicktoast
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(), PostAdapter.PostInteraction, IFetch {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binder: FragmentHomeBinding
    private lateinit var postAdapter: PostAdapter
    private lateinit var photosadapter: PhotoViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }

        homeViewModel = requireActivity().run {
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        }
    }

    override fun onStart() {
        super.onStart()
        homeViewModel.fetchAllPosts(this)
    }

    override fun onPause() {
        super.onPause()
        if (homeViewModel.isLoading().value!!) {
            homeViewModel.setLoading()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        initObservers()

        binder.swipeLayout.setOnRefreshListener {
            homeViewModel.fetchAllPosts(this)
            binder.swipeLayout.isRefreshing = false
        }

        binder.provideCarButton.setOnClickListener {
            addFragmentOnTop(AddEditPost())
        }

    }

    private fun initObservers() {
        homeViewModel.isLoading().observe(viewLifecycleOwner, Observer {
            binder.isLoading = it
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binder = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        return binder.root
    }

    private fun initRecyclerView() {
        postAdapter = PostAdapter(this)
        post_rv.adapter = postAdapter
    }

    private fun addFragmentOnTop(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .add(R.id.home_fragment_container, fragment).addToBackStack(null).commit()
    }

    override fun onPostSelected(post: Post) {
        val bundle = Bundle()
        bundle.putParcelable("post", post)

        val postFragment = ViewPostFragment()
        postFragment.arguments = bundle

        requireActivity().supportFragmentManager
            .beginTransaction()
            .add(R.id.home_fragment_container, postFragment)
            .addToBackStack("")
            .commit()

    }

    override fun onCanceled(msg: String) {
        homeViewModel.setLoading()
        Quicktoast(requireActivity()).swarn(msg)
    }

    override fun onSuccess(list: List<Post>) {
        homeViewModel.setLoading()
        postAdapter.submitList(list)
    }


}
