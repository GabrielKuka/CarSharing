package com.car.sharing.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.car.sharing.R
import com.car.sharing.databinding.FragmentProfileBinding
import com.car.sharing.viewmodels.HomeViewModel
import com.meet.quicktoast.Quicktoast

class ProfileFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binder: FragmentProfileBinding

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

        binder.user = homeViewModel.getUser()

        binder.signOutButton.setOnClickListener {
            homeViewModel.signOut()
        }

        binder.settingsButton.setOnClickListener {
            Quicktoast(requireActivity()).sinfo("Settings here")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binder = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

        return binder.root
    }

}
