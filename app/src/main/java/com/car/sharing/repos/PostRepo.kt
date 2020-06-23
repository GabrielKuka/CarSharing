package com.car.sharing.repos

import android.net.Uri
import com.car.sharing.models.CarPhoto
import com.car.sharing.models.Post
import com.car.sharing.models.Rating
import com.car.sharing.utils.*
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class PostRepo {

    private val storageRef = FirebaseStorage.getInstance()
    private val dbRef = FirebaseDatabase.getInstance()
    private val photosRef = dbRef.getReference("car_photos")
    internal val ratingsRef = dbRef.getReference("post_ratings")
    internal val currentUser = FirebaseAuth.getInstance().currentUser

    // Delete post methods

    fun deletePost(postId: String, iPost: IPost) {
        dbRef.getReference("posts").child(postId).removeValue().addOnCompleteListener {
            if (!it.isSuccessful) {
                // failed to delete
                iPost.onDeleteError("Failed to delete this post!")
                return@addOnCompleteListener
            }
            deleteRatings(postId)
            deletePostPhotos(postId)

            iPost.onDeleteSuccess("Post Deleted!")
        }.addOnFailureListener {
            iPost.onDeleteError(it.message.toString())
        }
    }

    private fun deletePostPhotos(postId: String) {
        val photosNames = mutableListOf<String>()
        photosRef.orderByChild("postId").equalTo(postId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    // error
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    for (item in snapshot.children) {
                        val name = item.child("name").value.toString()
                        photosNames.add(name)
                    }
                    snapshot.children.forEach { it ->
                        photosRef.child(
                            it.child("name").value.toString().substringBefore(".")
                        ).removeValue()
                    }

                    for (photoName in photosNames) {
                        storageRef.getReference("car_photos/$photoName").delete()
                    }

                }
            })
    }

    private fun deleteRatings(postId: String) {

        ratingsRef.orderByChild("postId").equalTo(postId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    // error
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach { it ->
                        ratingsRef.child(it.child("rateId").value.toString()).removeValue()
                    }

                }
            })
    }


    // Edit Post methods

    fun updatePost(post: Post, carPhotos: List<CarPhoto>, iAddEdit: IAddEdit) {
        val query = dbRef.getReference("posts").child(post.postId)
        query.setValue(post).addOnFailureListener { iAddEdit.onError(it.message.toString()) }
        processPhotos(post.postId, carPhotos, iAddEdit)
    }

    private fun processPhotos(postId: String, carPhotos: List<CarPhoto>, iAddEdit: IAddEdit) {
        val toBeUploaded = mutableListOf<CarPhoto>()
        val uploadedPhotos = mutableListOf<CarPhoto>()
        val excludedPhotos = mutableListOf<CarPhoto>()
        val toBeDeletedPhotos = mutableListOf<CarPhoto>()

        val query = photosRef.orderByChild("postId").equalTo(postId)


        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                iAddEdit.onError(p0.message)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (item in snapshot.children) {
                    val currentPhoto = item.getValue(CarPhoto::class.java)
                    uploadedPhotos.add(currentPhoto!!)

                    carPhotos.forEach loop@{ it ->
                        if (it.url == currentPhoto.url) {
                            excludedPhotos.add(it)
                            return@loop
                        }
                    }

                }

                if(carPhotos == uploadedPhotos){
                    return
                }

                toBeDeletedPhotos.addAll(uploadedPhotos.minus(excludedPhotos))
                toBeUploaded.addAll(carPhotos.minus(excludedPhotos))

                if (toBeDeletedPhotos.isNotEmpty()) {
                    toBeDeletedPhotos.forEach {
                        photosRef.child(it.name.substringBefore(".")).removeValue()
                        storageRef.getReference("car_photos/${it.name}")
                            .delete()
                    }
                }

                if(toBeUploaded.isNotEmpty()){
                    uploadPhotos(toBeUploaded)
                    iAddEdit.onSuccess("Success!")
                    return
                }
                iAddEdit.onSuccess("Success!")
            }
        })


    }

    private fun uploadPhotos(toBeUploaded: List<CarPhoto>) {
        toBeUploaded.forEach {
            storageRef.getReference("car_photos").child(it.name).putFile(Uri.parse(it.url))
                .addOnSuccessListener { uploadTask ->

                    val task: Task<Uri> = uploadTask.storage.downloadUrl

                    while (!task.isSuccessful);
                    val downloadUrl: Uri? = task.result

                    it.url = downloadUrl.toString()
                    photosRef.child(it.name.substringBefore(".")).setValue(it)

                }
        }
    }



    fun fetchRatings(postId: String, iRating: IRating) {
        val query = ratingsRef.orderByChild("postId").equalTo(postId)

        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                iRating.onErrorFetching(p0.message)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val ratingsList = mutableListOf<Rating>()

                for (item in snapshot.children) {
                    val rating = item.getValue(Rating::class.java)
                    ratingsList.add(rating!!)
                }

                iRating.onRatingsListFetched(ratingsList)
            }
        })
    }

    fun fetchPostRating(postId: String, iRatingInteraction: IRatingInteraction) {
        val query = ratingsRef.orderByChild("postId").equalTo(postId)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                iRatingInteraction.onErrorRating(p0.message)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.children.count() == 0) {
                    iRatingInteraction.onPostRatingFetched(0.0)
                } else {
                    var sum = 0.0
                    for (item in snapshot.children) {
                        val rating: Double = item.child("rateNumber").value.toString().toDouble()
                        sum += rating
                    }

                    val avg = Helper.roundOffDecimal((sum / snapshot.children.count()))
                    updatePostRating(postId, avg!!)
                }
            }
        })
    }

    fun updatePostRating(postId: String, rateNumber: Double) {
        val query = dbRef.getReference("posts").child(postId)

        val map: MutableMap<String, Any> = mutableMapOf()
        map["rateNumber"] = rateNumber

        query.updateChildren(map)

    }

    fun addRating(rating: Rating, iRatingInteraction: IRatingInteraction) {
        ratingsRef.child(rating.rateId).setValue(rating)
            .addOnCompleteListener {
                if (!it.isSuccessful) {
                    iRatingInteraction.onErrorRating("Failed to add a rating")
                    return@addOnCompleteListener
                }

                iRatingInteraction.onAddedRating("Added Successfully")
            }
            .addOnFailureListener {
                iRatingInteraction.onErrorRating("Error adding a rating")
            }
    }

    fun hasUserRated(postId: String, email: String, iRatingInteraction: IRatingInteraction) {

        val query = ratingsRef.orderByChild("reviewerEmail").equalTo(email)

        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                iRatingInteraction.onErrorRating(p0.message)
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                for (item in snapshot.children) {
                    val currentRatePostId = item.child("postId").value.toString()
                    if (postId == currentRatePostId) {
                        // Success
                        val currentRating = item.getValue(Rating::class.java)
                        iRatingInteraction.onHasUserRated(currentRating!!)
                        break
                    }
                }
            }
        })

    }

    fun retrievePhotos(postId: String, iPostPhotos: IPostPhotos) {

        val query = photosRef.orderByChild("postId").equalTo(postId)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
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