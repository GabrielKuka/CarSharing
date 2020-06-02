package com.car.sharing.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    fun areFieldsEmpty(fields: Array<String>): Boolean {
        fields.forEach {
            if (it.isEmpty()) {
                return true
            }
        }
        return false
    }


}