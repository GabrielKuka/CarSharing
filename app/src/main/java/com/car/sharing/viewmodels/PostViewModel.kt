package com.car.sharing.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.car.sharing.models.Rating
import com.car.sharing.repos.PostRepo
import com.car.sharing.utils.IPostPhotos
import com.car.sharing.utils.IRating
import com.car.sharing.utils.IRatingsList

class PostViewModel(private val postRepo: PostRepo = PostRepo()) : ViewModel() {


    fun getCurrentUser() = postRepo.currentUser
    fun getRatingsRef() = postRepo.ratingsRef

    fun retrievePhotos(postId: String, iPostPhotos: IPostPhotos) {
        postRepo.retrievePhotos(postId, iPostPhotos)
    }

    fun addRating(rating: Rating, iRating: IRating) {
        postRepo.addRating(rating, iRating)
    }

    fun fetchRatings(postId: String, iRatingsList: IRatingsList){
        postRepo.fetchRatings(postId, iRatingsList)
    }

}