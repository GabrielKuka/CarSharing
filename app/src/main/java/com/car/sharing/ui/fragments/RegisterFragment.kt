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
import com.car.sharing.utils.IRegister
import com.car.sharing.viewmodels.AuthViewModel
import com.meet.quicktoast.Quicktoast

class RegisterFragment : Fragment(), IRegister {
    private lateinit var binder: FragmentRegisterBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }

        authViewModel = requireActivity().run {
            ViewModelProviders.of(this).get(AuthViewModel::class.java)
        }

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

        binder.emailField.requestFocus()

        initObservers()

        binder.registerButton.setOnClickListener {
            registerAccount()
        }

        binder.alreadyUser.setOnClickListener {
            navController.navigate(R.id.action_register_to_logInFragment)
        }

    }

    private fun initObservers() {
        authViewModel.isLoading().observe(viewLifecycleOwner, Observer {
            binder.isLoading = it
        })
    }

    private fun registerAccount() {
        val emailField = binder.emailField.editText?.text.toString().trim()
        val passField = binder.passField.editText?.text.toString().trim()

        if (authViewModel.areFieldsEmpty(arrayOf(emailField, passField))) {
            Quicktoast(requireActivity()).swarn("There are empty fields.")
            return
        }

        val keys = Pair(emailField, passField)
        authViewModel.register(keys, this)
    }

    private fun showTextDialog(msg: String) {
        TextDialog(msg).show(requireActivity().supportFragmentManager, "")
    }

    override fun onErrorRegister(msg: String) {
        authViewModel.setLoading()
        showTextDialog(msg)
    }

    override fun onRegisterSuccess() {
        authViewModel.setLoading()
        val emailField = binder.emailField.editText?.text.toString().trim()

        showTextDialog("An email has been sent to the address provided. Check that email to verify your account")
        val bundle = bundleOf("email" to emailField)
        navController.navigate(R.id.action_register_to_logInFragment, bundle)
    }

}