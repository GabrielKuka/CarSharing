package com.car.sharing.utils

import com.car.sharing.models.CarPhoto
import com.car.sharing.models.Post
import com.car.sharing.models.Rating

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
    fun onPhotoUploadError(msg: String)
}

interface IPost {

    fun onDeleteError(msg: String)
}

interface ISearch {
    fun onSuccess(list: List<Post>)
    fun onError(msg: String)
}

interface IPostPhotos {
    fun onErrorRetrieve(msg: String)
    fun onSuccessRetrieve(list: MutableList<CarPhoto>)
}

interface IRatingInteraction {
    fun onAddedRating(msg: String)
    fun onErrorRating(msg: String)
    fun onPostRatingFetched(avg: Double)
    fun onHasUserRated(rating: Rating)
}

interface IRating {
    fun onRatingsListFetched(list: List<Rating>)
    fun onErrorFetching(msg: String)
}

