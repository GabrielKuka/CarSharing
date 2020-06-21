package com.car.sharing.viewmodels

import androidx.lifecycle.ViewModel
import com.car.sharing.models.Rating
import com.car.sharing.repos.PostRepo
import com.car.sharing.utils.IPost
import com.car.sharing.utils.IPostPhotos
import com.car.sharing.utils.IRatingInteraction
import com.car.sharing.utils.IRating

class PostViewModel(private val postRepo: PostRepo = PostRepo()) : ViewModel() {


    fun getCurrentUser() = postRepo.currentUser
    fun getRatingsRef() = postRepo.ratingsRef

    fun retrievePhotos(postId: String, iPostPhotos: IPostPhotos) {
        postRepo.retrievePhotos(postId, iPostPhotos)
    }

    fun addRating(rating: Rating, iRatingInteraction: IRatingInteraction) {
        postRepo.addRating(rating, iRatingInteraction)
    }

    fun fetchRatings(postId: String, iRating: IRating){
        postRepo.fetchRatings(postId, iRating)
    }

    fun fetchPostRating(postId: String, iRatingInteraction: IRatingInteraction){
        postRepo.fetchPostRating(postId, iRatingInteraction)
    }

    fun hasUserRated(postId: String, iRatingInteraction: IRatingInteraction){
         postRepo.hasUserRated(postId, getCurrentUser()?.email.toString(), iRatingInteraction)
    }

    fun deletePost(postId: String, iPost: IPost){
        postRepo.deletePost(postId, iPost)
    }

}