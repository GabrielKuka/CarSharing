package com.car.sharing.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.car.sharing.R
import com.car.sharing.viewmodels.AuthViewModel

class Authentication : AppCompatActivity() {

    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.authentication)

        authViewModel = ViewModelProviders.of(this).get(AuthViewModel::class.java)

    }


}
