package com.car.sharing.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class HomeViewModel : ViewModel() {

    private val _isLoading: MutableLiveData<Boolean> =
        MutableLiveData<Boolean>().apply { value = false }

    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val rootNode = FirebaseDatabase.getInstance()
    private val postRef = rootNode.getReference("posts")

    fun getUser() = currentUser
    fun getPostRef() = postRef

    fun isLoading(): LiveData<Boolean> = _isLoading

    fun setLoading() {
        _isLoading.value = !_isLoading.value!!
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