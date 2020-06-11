package com.car.sharing.utils

import com.car.sharing.models.Post

interface AuthInteraction {
    fun onErrorLogIn(msg: String)
    fun onLogInWithEmailSuccess()
}

interface IRegister {
    fun onErrorRegister(msg: String)
    fun onRegisterSuccess()
}

interface IAccountRecover {
    fun onEmailSent()
    fun onRecoverError(msg: String)
}

interface IFetch {
    fun onCanceled(msg: String)

    fun onSuccess(list: List<Post>)
}

interface IAddEdit {
    fun onSuccess(msg: String)

    fun onError(msg: String)
}