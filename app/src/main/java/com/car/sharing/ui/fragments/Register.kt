package com.car.sharing.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.car.sharing.R
import com.car.sharing.viewmodels.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import com.meet.quicktoast.Quicktoast
import kotlinx.android.synthetic.main.fragment_register.*

class Register : Fragment() {

    private lateinit var authViewModel: AuthViewModel
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }

        authViewModel = requireActivity().run {
            ViewModelProviders.of(this).get(AuthViewModel::class.java)
        }

        firebaseAuth = FirebaseAuth.getInstance()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        register_button.setOnClickListener {
            val emailField = email_field.text.toString().trim()
            val passField = pass_field.text.toString().trim()

            if (authViewModel.areFieldsEmpty(arrayOf(emailField, passField))) {
                Quicktoast(requireActivity()).swarn("There are empty fields.")
                return@setOnClickListener
            }

            firebaseAuth.createUserWithEmailAndPassword(emailField, passField)
                .addOnCanceledListener {
                    Quicktoast(requireActivity()).swarn("Registration Canceled.")
                }.addOnCompleteListener {
                if (!it.isSuccessful) {
                    Quicktoast(requireActivity()).swarn("Registration failed.")
                    return@addOnCompleteListener
                }

                val user = firebaseAuth.currentUser
                user?.sendEmailVerification()?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Quicktoast(requireActivity()).sinfo("Verification Email Sent.")

                        val bundle = bundleOf("email" to emailField)
                        navController.navigate(R.id.action_register_to_logInFragment, bundle)

                    } else {
                        Quicktoast(requireActivity()).swarn("Failed to send email.")
                    }

                }
            }

        }

    }

}
