package com.car.sharing.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.car.sharing.repos.AuthRepo
import com.car.sharing.utils.AuthInteraction
import com.car.sharing.utils.IAccountRecover
import com.car.sharing.utils.IRegister

class AuthViewModel(private val authRepo: AuthRepo = AuthRepo()) : ViewModel() {

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply {
        value = false
    }

    fun logInWithEmail(pair: Pair<String, String>, authInteraction: AuthInteraction) {
        setLoading()
        authRepo.logInWithEmail(pair, authInteraction)
    }

    fun register(pair: Pair<String, String>, iRegister: IRegister) {
        setLoading()
        authRepo.register(pair, iRegister)
    }

    fun recoverAccount(email: String, iRecover: IAccountRecover){
        setLoading()
        authRepo.recoverAccount(email, iRecover)
    }

    fun isLoading(): LiveData<Boolean> {
        return _isLoading
    }

    fun setLoading() {
        _isLoading.value = !_isLoading.value!!
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