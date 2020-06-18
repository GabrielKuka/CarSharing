package com.car.sharing.repos

import android.net.Uri
import com.car.sharing.models.CarPhoto
import com.car.sharing.models.Post
import com.car.sharing.utils.IAddEdit
import com.car.sharing.utils.IFetch
import com.car.sharing.utils.ISearch
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class HomeRepo {

    private val storageRef = FirebaseStorage.getInstance().getReference("car_photos")

    private val firebaseAuth = FirebaseAuth.getInstance()
    internal val currentUser = firebaseAuth.currentUser

    private val rootNode = FirebaseDatabase.getInstance()
    internal val postRef = rootNode.getReference("posts")
    private val carPhotosRef = rootNode.getReference("car_photos")

    fun uploadPhoto(carPhoto: CarPhoto, iAddEdit: IAddEdit) {
        val fileReference = storageRef.child(carPhoto.name)

        fileReference.putFile(Uri.parse(carPhoto.url))
            .addOnSuccessListener {
             //   val pair = Pair(it.uploadSessionUri.toString(), carPhoto.postId)

                val task: Task<Uri> = it.storage.downloadUrl

                while(!task.isSuccessful);
                val downloadUrl: Uri? = task.result

                carPhoto.url = downloadUrl.toString()
                carPhotosRef.child(carPhoto.name.substringBefore(".")).setValue(carPhoto)

            }
            .addOnFailureListener { iAddEdit.onPhotoUploadError(it.message!!) }
            .addOnProgressListener { }
    }

    fun fetchAllPosts(iFetch: IFetch) {
        postRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                iFetch.onCanceled(p0.message)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val postList = mutableListOf<Post>()
                for (item in snapshot.children) {
                    postList.add(item.getValue(Post::class.java)!!)
                }

                iFetch.onSuccess(postList)
            }
        })
    }

    fun addPost(post: Post, iAddEdit: IAddEdit) {
        postRef.child(post.postId).setValue(post).addOnCompleteListener {
            if (!it.isSuccessful) {
                iAddEdit.onError("Failed to add the post")
            } else {
                iAddEdit.onSuccess("Post Added")
            }
        }
    }

    fun queryPost(text: String, iSearch: ISearch) {
        val query = postRef.orderByChild("carName").startAt(text)

        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                iSearch.onError("Search Failed")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val postList = mutableListOf<Post>()
                for (item in snapshot.children) {
                    postList.add(item.getValue(Post::class.java)!!)
                }

                iSearch.onSuccess(postList)
            }
        })

    }

    fun signOut() {
        firebaseAuth.signOut()
    }
}