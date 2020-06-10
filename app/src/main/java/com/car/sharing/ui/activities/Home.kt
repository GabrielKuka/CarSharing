package com.car.sharing.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.car.sharing.R
import com.car.sharing.ui.fragments.HomeFragment
import com.car.sharing.ui.fragments.ProfileFragment
import com.car.sharing.ui.fragments.SearchFragment
import com.car.sharing.viewmodels.HomeViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.home_activity.*

class Home : AppCompatActivity() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        firebaseAuth = FirebaseAuth.getInstance()

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        authStateListener = FirebaseAuth.AuthStateListener {
            val user = firebaseAuth.currentUser

            if (user == null) {
                startActivity(Intent(this, Authentication::class.java))
            }

        }

        bottom_nav.setOnNavigationItemSelectedListener {

            var selectedFragment: Fragment? = null

            when (it.itemId) {
                R.id.home_button -> {
                    selectedFragment = HomeFragment()
                }

                R.id.search_button -> {
                    selectedFragment = SearchFragment()
                }

                R.id.profile_button -> {
                    selectedFragment = ProfileFragment()
                }

            }

            selectedFragment?.let { sF ->
                switchFragment(sF)
            }

            true
        }

        if (savedInstanceState == null) {
            switchFragment(HomeFragment())
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

    private fun switchFragment(destination: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.home_fragment_container, destination)
            .commit()
    }
}
