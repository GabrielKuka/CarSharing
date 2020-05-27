package com.car.sharing.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.car.sharing.R
import com.car.sharing.viewmodels.AuthViewModel
import kotlinx.android.synthetic.main.fragment_choose_auth.*


class ChooseAuth : Fragment() {

    private lateinit var authViewModel: AuthViewModel
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        authViewModel = activity?.run {
            ViewModelProviders.of(this).get(AuthViewModel::class.java)
        }!!

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        email_login_button.setOnClickListener {
            navController.navigate(R.id.action_chooseAuth_to_logInFragment)
        }

        create_acc_tv.setOnClickListener {
            navController.navigate(R.id.action_chooseAuth_to_register)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choose_auth, container, false)
    }

}
