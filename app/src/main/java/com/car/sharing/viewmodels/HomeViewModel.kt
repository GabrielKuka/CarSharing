package com.car.sharing.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.car.sharing.models.CarPhoto
import com.car.sharing.models.Post
import com.car.sharing.repos.HomeRepo
import com.car.sharing.utils.IAddEdit
import com.car.sharing.utils.IFetch
import com.car.sharing.utils.ISearch

class HomeViewModel(private val homeRepo: HomeRepo = HomeRepo()) : ViewModel() {

    private val _isLoading: MutableLiveData<Boolean> =
        MutableLiveData<Boolean>().apply { value = false }

    private val _carPhotos: MutableLiveData<MutableList<CarPhoto>> =
        MutableLiveData<MutableList<CarPhoto>>().apply {
            value = mutableListOf()
        }

    fun getFirebaseAuth() = homeRepo.firebaseAuth
    fun getUser() = homeRepo.currentUser
    fun getPostRef() = homeRepo.postRef

    fun getCarPhotos(): LiveData<MutableList<CarPhoto>> = _carPhotos

    fun addCarPhoto(carPhoto: CarPhoto) {
        _carPhotos.value?.add(carPhoto)
        _carPhotos.notifyObserver()
    }

    fun removeCarPhoto(carPhoto: CarPhoto) {
        _carPhotos.value!!.remove(carPhoto)
        _carPhotos.notifyObserver()
    }

    fun isLoading(): LiveData<Boolean> = _isLoading

    fun setLoading() {
        _isLoading.value = !_isLoading.value!!
    }

    fun signOut() {
        homeRepo.signOut()
    }

    fun fetchAllPosts(iFetch: IFetch) {
        setLoading()
        homeRepo.fetchAllPosts(iFetch)
    }

    fun queryPost(text: String, iSearch: ISearch) {
        setLoading()
        homeRepo.queryPost(text, iSearch)
    }

    fun addPost(post: Post, photos: List<CarPhoto>, iAddEdit: IAddEdit) {

        // 1. For each photo, add the postId !!
        photos.forEach { photo -> photo.postId = post.postId }

        // 2. Upload each photo
        photos.forEach { photo -> homeRepo.uploadPhoto(photo, iAddEdit) }

        // 3. Add the post
        homeRepo.addPost(post, iAddEdit)

    }

    fun areFieldsEmpty(fields: List<String?>): Boolean {

        for (field in fields) {
            if (field.isNullOrEmpty()) {
                return true
            }
        }
        return false
    }

    private fun <T> MutableLiveData<T>.notifyObserver() {
        this.value = this.value
    }
}