package com.car.sharing.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.car.sharing.models.Post
import com.car.sharing.repos.HomeRepo
import com.car.sharing.utils.IAddEdit
import com.car.sharing.utils.IFetch

class HomeViewModel(private val homeRepo: HomeRepo = HomeRepo()) : ViewModel() {

    private val _isLoading: MutableLiveData<Boolean> =
        MutableLiveData<Boolean>().apply { value = false }

    fun getUser() = homeRepo.currentUser
    fun getPostRef() = homeRepo.postRef

    fun isLoading(): LiveData<Boolean> = _isLoading

    fun setLoading() {
        _isLoading.value = !_isLoading.value!!
    }

    fun signOut() {
        homeRepo.signOut()
    }

    fun fetchAllPosts(iFetch: IFetch) {
        homeRepo.fetchAllPosts(iFetch)
    }

    fun addPost(pair: Pair<String, Post>, iAddEdit: IAddEdit) {
        setLoading()
        homeRepo.addPost(pair, iAddEdit)
    }

    fun areFieldsEmpty(fields: List<String?>): Boolean {

        for (field in fields) {
            if (field.isNullOrEmpty()) {
                return true
            }
        }
        return false
    }
}