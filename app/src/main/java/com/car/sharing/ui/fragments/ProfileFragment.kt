package com.car.sharing.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.car.sharing.R
import com.car.sharing.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.meet.quicktoast.Quicktoast

class ProfileFragment : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binder: FragmentProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }

        firebaseAuth = FirebaseAuth.getInstance()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binder.user = firebaseAuth.currentUser

        binder.signOutButton.setOnClickListener {
            firebaseAuth.signOut()
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
