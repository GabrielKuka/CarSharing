package com.car.sharing.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply {
        value = false
    }

    fun areFieldsEmpty(fields: Array<String>): Boolean {
        fields.forEach {
            if (it.isEmpty()) {
                return true
            }
        }
        return false
    }


}