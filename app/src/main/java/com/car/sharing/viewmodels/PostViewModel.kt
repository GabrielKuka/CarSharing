package com.car.sharing.viewmodels

import androidx.lifecycle.ViewModel
import com.car.sharing.repos.PostRepo
import com.car.sharing.utils.IPostPhotos
import com.google.firebase.database.FirebaseDatabase

class PostViewModel(private val postRepo: PostRepo = PostRepo()): ViewModel() {
    private val dbRef = FirebaseDatabase.getInstance()
    private val photosRef = dbRef.getReference("car_photos")

    fun retrievePhotos(postId: String, iPostPhotos: IPostPhotos){
        val query = photosRef.orderByChild("postId").equalTo(postId)
        postRepo.retrievePhotos(query, iPostPhotos)
    }

}