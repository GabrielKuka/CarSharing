package com.car.sharing.repos

import com.car.sharing.models.CarPhoto
import com.car.sharing.models.Rating
import com.car.sharing.utils.IPostPhotos
import com.car.sharing.utils.IRating
import com.car.sharing.utils.IRatingsList
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PostRepo {

    private val dbRef = FirebaseDatabase.getInstance()
    private val photosRef = dbRef.getReference("car_photos")
    internal val ratingsRef = dbRef.getReference("post_ratings")
    internal val currentUser = FirebaseAuth.getInstance().currentUser

    fun fetchRatings(postId: String, iRatingsList: IRatingsList) {
        val query = ratingsRef.orderByChild("postId").equalTo(postId)

        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                iRatingsList.onErrorFetching(p0.message)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val ratingsList = mutableListOf<Rating>()

                for (item in snapshot.children) {
                    val rating = item.getValue(Rating::class.java)
                    ratingsList.add(rating!!)
                }

                iRatingsList.onFetched(ratingsList)
            }
        })
    }

    fun addRating(rating: Rating, iRating: IRating) {
        ratingsRef.child(rating.rateId).setValue(rating)
            .addOnCompleteListener {
                if (!it.isSuccessful) {
                    iRating.onErrorRating("Failed to add a rating")
                    return@addOnCompleteListener
                }

                iRating.onAddedRating("Added Successfully")
            }
            .addOnFailureListener {
                iRating.onErrorRating("Error adding a rating")
            }
    }

    fun retrievePhotos(postId: String, iPostPhotos: IPostPhotos) {

        val query = photosRef.orderByChild("postId").equalTo(postId)

        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                iPostPhotos.onErrorRetrieve(p0.message)
            }

            override fun onDataChange(data: DataSnapshot) {
                val list = mutableListOf<CarPhoto>()
                for (postSnapshot in data.children) {
                    val carPhoto = postSnapshot.getValue(CarPhoto::class.java)
                    list.add(carPhoto!!)
                }

                iPostPhotos.onSuccessRetrieve(list)
            }
        })
    }

}