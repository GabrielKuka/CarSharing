package com.car.sharing.repos

import com.car.sharing.utils.AuthInteraction
import com.car.sharing.utils.IAccountRecover
import com.car.sharing.utils.IRegister
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class AuthRepo {
    private val firebaseAuth = FirebaseAuth.getInstance()

    fun logInWithEmail(keys: Pair<String, String>, authInteraction: AuthInteraction) {
        firebaseAuth.signInWithEmailAndPassword(keys.first, keys.second).addOnCompleteListener {
            if (it.isSuccessful) {
                val user = firebaseAuth.currentUser
                if (user != null && user.isEmailVerified) {
                    authInteraction.onLogInWithEmailSuccess()
                } else {
                    authInteraction.onErrorLogIn("This account is not yet verified. Check your emails to verify it.")
                }
            } else {
                it.exception?.message?.let { errorMsg -> authInteraction.onErrorLogIn(errorMsg) }
            }

        }
    }

    fun register(fullName: String, keys: Pair<String, String>, iRegister: IRegister){

        firebaseAuth.createUserWithEmailAndPassword(keys.first, keys.second)
            .addOnCanceledListener {
                iRegister.onErrorRegister("Registration Canceled.")
            }.addOnCompleteListener {
                if (!it.isSuccessful) {
                    iRegister.onErrorRegister("Registration failed.")
                    return@addOnCompleteListener
                }

                val user = firebaseAuth.currentUser
                user?.sendEmailVerification()?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        iRegister.onRegisterSuccess()

                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setDisplayName(fullName).build()
                        user.updateProfile(profileUpdates)

                    } else {
                        iRegister.onErrorRegister("Failed to send email.")
                    }

                }
            }
    }

    fun recoverAccount(email: String, iRecover: IAccountRecover){
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener {
            if (it.isSuccessful) {
                iRecover.onEmailSent()
            } else {
                iRecover.onRecoverError(it.exception?.message!!)
            }
        }
    }

}