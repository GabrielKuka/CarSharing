package com.car.sharing.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.car.sharing.R
import com.car.sharing.databinding.FragmentLoginBinding
import com.car.sharing.ui.activities.Home
import com.car.sharing.ui.dialogs.TextDialog
import com.car.sharing.viewmodels.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import com.meet.quicktoast.Quicktoast
import kotlinx.android.synthetic.main.fragment_login.*


class LogInFragment : Fragment() {
    private lateinit var binder: FragmentLoginBinding
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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        requireActivity().window
            .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        binder = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        return binder.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binder.emailField.requestFocus()

        if (email.isNotEmpty()) {
            binder.emailField.setText(email)
            binder.passField.requestFocus()
        }

        initObservers()

        binder.loginButton.setOnClickListener {

            logInWithEmail()
        }

    }

    private fun initObservers() {
        authViewModel.isLoading().observe(viewLifecycleOwner, Observer {
            binder.isLoading = it
        })
    }

    private fun logInWithEmail() {
        val emailField = email_field.text.toString().trim()
        val passField = pass_field.text.toString().trim()

        if (authViewModel.areFieldsEmpty(arrayOf(emailField, passField))) {
            Quicktoast(requireActivity()).swarn("There are empty fields.")
            return
        }

        authViewModel.setLoading()
        firebaseAuth.signInWithEmailAndPassword(emailField, passField).addOnCompleteListener {
            if (it.isSuccessful) {
                val user = firebaseAuth.currentUser
                if (user != null && user.isEmailVerified) {

                    val intent = Intent(requireActivity(), Home::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)

                    startActivity(intent)
                } else {
                    showTextDialog("This account is not yet verified. Check your emails to verify it.")
                }
            } else {
                it.exception?.message?.let { errorMsg -> showTextDialog(errorMsg) }
            }
            authViewModel.setLoading()
        }
    }

    private fun showTextDialog(msg: String) {
        TextDialog(msg).show(requireActivity().supportFragmentManager, "")
    }


}
