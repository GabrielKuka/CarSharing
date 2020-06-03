package com.car.sharing.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.car.sharing.R
import com.car.sharing.viewmodels.AuthViewModel
import org.imaginativeworld.oopsnointernet.NoInternetDialog

class Authentication : AppCompatActivity() {

    private lateinit var authViewModel: AuthViewModel
    private var noNetDialog: NoInternetDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.authentication)

        authViewModel = ViewModelProviders.of(this).get(AuthViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()
        noNetDialog = NoInternetDialog.Builder(this).apply {
            cancelable = false
            onAirplaneModeMessage = getString(R.string.airplane_mode)
            noInternetConnectionMessage = getString(R.string.no_internet)
        }.build()
    }

    override fun onPause() {
        super.onPause()
        noNetDialog?.destroy()
    }


}
