package com.car.sharing.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.car.sharing.R
import com.car.sharing.models.Post
import com.car.sharing.ui.adapters.PostAdapter
import com.car.sharing.utils.ISearch
import com.car.sharing.viewmodels.HomeViewModel
import com.iammert.library.ui.multisearchviewlib.MultiSearchView
import com.meet.quicktoast.Quicktoast
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : Fragment(), PostAdapter.PostInteraction, ISearch {

    private lateinit var postAdapter: PostAdapter
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }

        homeViewModel = requireActivity().run {
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        multiSearchView.setSearchViewListener(searchViewListener)
    }

    private val searchViewListener = object : MultiSearchView.MultiSearchViewListener {
        override fun onItemSelected(index: Int, s: CharSequence) {
        }

        override fun onTextChanged(index: Int, s: CharSequence) {
            homeViewModel.queryPost(s.toString(), this@SearchFragment)
        }

        override fun onSearchComplete(index: Int, s: CharSequence) {
        }

        override fun onSearchItemRemoved(index: Int) {
        }

    }

    private fun initRecyclerView() {
        postAdapter = PostAdapter(this)
        post_rv.adapter = postAdapter

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_search, container, false)
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

    override fun onSuccess(list: List<Post>) {
        homeViewModel.setLoading()
        postAdapter.submitList(list)
    }

    override fun onError(msg: String) {
        homeViewModel.setLoading()
        Quicktoast(requireActivity()).swarn(msg)
    }


}
