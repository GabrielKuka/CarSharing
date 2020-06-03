package com.car.sharing.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.car.sharing.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.home_activity.*

class Home : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        firebaseAuth = FirebaseAuth.getInstance()

        authStateListener = FirebaseAuth.AuthStateListener {
            val user = firebaseAuth.currentUser

            if (user == null) {
                startActivity(Intent(this, Authentication::class.java))
            }

        }

        logout_button.setOnClickListener {
            firebaseAuth.signOut()
        }
    }


    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener(authStateListener)
    }

    override fun onStop() {
        super.onStop()
        firebaseAuth.removeAuthStateListener(authStateListener)
    }
}
