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
import com.car.sharing.utils.IFetch
import com.car.sharing.viewmodels.HomeViewModel
import com.meet.quicktoast.Quicktoast
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(), PostAdapter.PostInteraction, IFetch {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var postAdapter: PostAdapter

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        provide_car_button.setOnClickListener {
            addFragmentOnTop(AddEditPost())
        }

        use_car_button.setOnClickListener {
            switchFragment(SearchFragment())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    private fun initRecyclerView() {
        postAdapter = PostAdapter(this)
        post_rv.adapter = postAdapter
    }

    private fun switchFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.home_fragment_container, fragment).commit()
    }

    private fun addFragmentOnTop(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .add(R.id.home_fragment_container, fragment).addToBackStack(null).commit()
    }

    override fun onPostSelected(position: Int, item: Post) {
        Quicktoast(requireActivity()).sinfo("Selected")
    }

    override fun onCanceled(msg: String) {
        Quicktoast(requireActivity()).swarn(msg)
    }

    override fun onSuccess(list: List<Post>) {
        postAdapter.submitList(list)
    }

}
