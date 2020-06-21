package com.car.sharing.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.car.sharing.R
import com.car.sharing.models.Rating
import com.car.sharing.ui.adapters.RatingAdapter
import com.car.sharing.utils.IRating
import com.car.sharing.viewmodels.PostViewModel
import com.meet.quicktoast.Quicktoast
import kotlinx.android.synthetic.main.fragment_ratings.*


class RatingsFragment : Fragment(), IRating {

    private lateinit var postViewModel: PostViewModel
    private lateinit var ratingAdapter: RatingAdapter
    private var postId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            postId = it.getString("postId", "")
        }

        postViewModel = requireActivity().run {
            ViewModelProviders.of(this).get(PostViewModel::class.java)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        postViewModel.fetchRatings(postId, this)

        swipe_layout.setOnRefreshListener {
            postViewModel.fetchRatings(postId, this)
            swipe_layout.isRefreshing = false
        }

    }

    private fun initRecyclerView() {
        ratingAdapter = RatingAdapter()
        ratings_rv.adapter = ratingAdapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_ratings, container, false)
    }

    override fun onRatingsListFetched(list: List<Rating>) {
        ratingAdapter.submitList(list)
    }

    override fun onErrorFetching(msg: String) {
        Quicktoast(requireContext()).swarn(msg)
    }

}
