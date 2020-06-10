package com.car.sharing.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.car.sharing.R
import com.car.sharing.databinding.FragmentAddEditPostBinding
import com.car.sharing.models.Post
import com.car.sharing.viewmodels.HomeViewModel
import com.meet.quicktoast.Quicktoast
import java.util.*

class AddEditPost : Fragment() {

    private lateinit var binder: FragmentAddEditPostBinding
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
        homeViewModel = requireActivity().run {
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()

        binder.carName.requestFocus()

        binder.provideDone.setOnClickListener {
            val carName = binder.carName.editText?.text?.toString()?.trim()
            val carDescription = binder.carDescription.editText?.text?.toString()?.trim()
            val price = binder.price.editText?.text?.toString()?.trim()?.toDouble()

            if (homeViewModel.areFieldsEmpty(listOf(carName, carDescription, price.toString()))) {
                Quicktoast(requireActivity()).swarn("There are empty fields.")
                return@setOnClickListener
            }

            addPost(carName!!, carDescription!!, price!!)

        }
    }

    private fun initObservers() {
        homeViewModel.isLoading().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            binder.isLoading = it
        })
    }

    private fun addPost(carName: String, carDescription: String, price: Double) {
        homeViewModel.setLoading()
        val id = UUID.randomUUID().toString()

        // Add the post
        val post = Post(
            carName,
            carDescription,
            price,
            homeViewModel.getUser()?.email!!
        )


        homeViewModel.getPostRef().child(id).setValue(post).addOnCompleteListener {
            if (!it.isSuccessful) {
                Quicktoast(requireActivity()).swarn("Failed to add the post")
            } else {
                Quicktoast(requireActivity()).linfo("Post added")
                requireActivity().supportFragmentManager.popBackStack()
            }

            homeViewModel.setLoading()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binder =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_edit_post, container, false)

        return binder.root
    }


}
