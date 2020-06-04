package com.car.sharing.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.car.sharing.R
import com.car.sharing.databinding.FragmentRegisterBinding
import com.car.sharing.ui.dialogs.TextDialog
import com.car.sharing.viewmodels.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import com.meet.quicktoast.Quicktoast
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : Fragment() {
    private lateinit var binder: FragmentRegisterBinding
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

        binder = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)

        return binder.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        initObservers()

        register_button.setOnClickListener {
            registerAccount()
        }

    }

    private fun initObservers() {
        authViewModel.isLoading().observe(viewLifecycleOwner, Observer {
            binder.isLoading = it
        })
    }

    private fun registerAccount() {
        val emailField = email_field.text.toString().trim()
        val passField = pass_field.text.toString().trim()

        if (authViewModel.areFieldsEmpty(arrayOf(emailField, passField))) {
            Quicktoast(requireActivity()).swarn("There are empty fields.")
            return
        }
        authViewModel.setLoading()
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
                        showTextDialog("An email has been sent to the address provided. Check that email to verify your account")

                        val bundle = bundleOf("email" to emailField)
                        navController.navigate(R.id.action_register_to_logInFragment, bundle)

                    } else {
                        Quicktoast(requireActivity()).swarn("Failed to send email.")
                    }

                }
                authViewModel.setLoading()
            }

    }

    private fun showTextDialog(msg: String) {
        TextDialog(msg).show(requireActivity().supportFragmentManager, "")
    }

}