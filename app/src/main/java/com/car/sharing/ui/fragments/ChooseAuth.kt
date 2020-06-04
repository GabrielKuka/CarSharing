package com.car.sharing.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.car.sharing.R
import com.car.sharing.ui.activities.Home
import com.car.sharing.viewmodels.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.meet.quicktoast.Quicktoast
import kotlinx.android.synthetic.main.fragment_choose_auth.*


class ChooseAuth : Fragment() {

    private val GOOGLE_SIGN_IN = 1
    private lateinit var authViewModel: AuthViewModel
    private lateinit var navController: NavController
    private lateinit var firebaseAuth: FirebaseAuth
    private var mGoogleSignInClient: GoogleSignInClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()

        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        authViewModel = requireActivity().run {
            ViewModelProviders.of(this).get(AuthViewModel::class.java)
        }

    }

    override fun onStart() {
        super.onStart()

        // Check if user is logged in
        val user = FirebaseAuth.getInstance().currentUser

        if (user != null && user.isEmailVerified) {
            // User is logged in
            startActivity(Intent(requireActivity(), Home::class.java))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        email_login_button.setOnClickListener {
            navController.navigate(R.id.action_chooseAuth_to_logInFragment)
        }

        google_login_button.setOnClickListener {
            googleLogin()
        }

        create_acc_tv.setOnClickListener {
            navController.navigate(R.id.action_chooseAuth_to_register)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choose_auth, container, false)
    }

    private fun googleLogin() {
        val intent = mGoogleSignInClient?.signInIntent
        startActivityForResult(intent, GOOGLE_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {

                val account = task.getResult(ApiException::class.java)

                val authCredential = GoogleAuthProvider.getCredential(account?.idToken, null)
                firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener {
                    if (!it.isSuccessful) {
                        Quicktoast(requireActivity()).swarn("Google Login Failed")
                        return@addOnCompleteListener
                    }

                    startActivity(Intent(requireActivity(), Home::class.java))
                }

            } catch (e: ApiException) {
                Quicktoast(requireActivity()).swarn(e.message)
            }
        }

    }
}
