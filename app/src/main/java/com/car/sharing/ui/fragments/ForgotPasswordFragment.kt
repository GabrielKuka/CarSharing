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
import com.car.sharing.databinding.FragmentForgotPasswordBinding
import com.car.sharing.ui.dialogs.TextDialog
import com.car.sharing.viewmodels.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import com.meet.quicktoast.Quicktoast

class ForgotPasswordFragment : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binder: FragmentForgotPasswordBinding
    private lateinit var authViewModel: AuthViewModel

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

        binder =
            DataBindingUtil.inflate(inflater, R.layout.fragment_forgot_password, container, false)

        return binder.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binder.emailField.requestFocus()

        initObservers()

        binder.sendEmail.setOnClickListener {
            val emailValue = binder.emailField.editText?.text.toString().trim()

            if (authViewModel.areFieldsEmpty(arrayOf(emailValue))) {
                Quicktoast(requireActivity()).swarn("There are empty fields!")
                return@setOnClickListener
            }

            sendEmail(emailValue)
        }
    }

    private fun initObservers() {
        authViewModel.isLoading().observe(viewLifecycleOwner, Observer {
            binder.isLoading = it
        })
    }

    private fun sendEmail(email: String) {
        authViewModel.setLoading()
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener {
            if (it.isSuccessful) {
                showTextDialog("An email has been sent to your address. Check it out.")
            } else {
                showTextDialog(it.exception?.message!!)
            }
            authViewModel.setLoading()
        }
    }

    private fun showTextDialog(msg: String) {
        TextDialog(msg).show(requireActivity().supportFragmentManager, "")
    }

}
