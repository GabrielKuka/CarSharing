package com.car.sharing.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.car.sharing.R
import com.car.sharing.ui.activities.Home
import com.car.sharing.viewmodels.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import com.meet.quicktoast.Quicktoast
import kotlinx.android.synthetic.main.fragment_login.*


class LogInFragment : Fragment() {

    private lateinit var authViewModel: AuthViewModel
    private lateinit var firebaseAuth: FirebaseAuth
    private var email: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            email = it.getString("email").toString()
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
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!email.isNullOrEmpty()) {
            email_field.setText(email)
        }

        login_button.setOnClickListener {
            val emailField = email_field.text.toString().trim()
            val passField = pass_field.text.toString().trim()

            if (authViewModel.areFieldsEmpty(arrayOf(emailField, passField))) {
                Quicktoast(requireActivity()).swarn("There are empty fields.")
                return@setOnClickListener
            }

            firebaseAuth.signInWithEmailAndPassword(emailField, passField).addOnCompleteListener {
                if (it.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    if (user != null && user.isEmailVerified) {
                        startActivity(Intent(requireActivity(), Home::class.java))
                    }
                } else {
                    Quicktoast(requireActivity()).swarn(it.exception?.message)
                }
            }
        }

    }


}
