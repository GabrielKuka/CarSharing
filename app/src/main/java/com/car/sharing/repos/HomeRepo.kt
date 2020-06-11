package com.car.sharing.repos

import com.car.sharing.models.Post
import com.car.sharing.utils.IAddEdit
import com.car.sharing.utils.IFetch
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeRepo {
    private val firebaseAuth = FirebaseAuth.getInstance()
    internal val currentUser = firebaseAuth.currentUser

    private val rootNode = FirebaseDatabase.getInstance()
    internal val postRef = rootNode.getReference("posts")

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

    fun addPost(pair: Pair<String, Post>, iAddEdit: IAddEdit) {
        postRef.child(pair.first).setValue(pair.second).addOnCompleteListener {
            if (!it.isSuccessful) {
                iAddEdit.onError("Failed to add the post")
            } else {
                iAddEdit.onSuccess("Post Added")

            }
        }
    }

    fun signOut() {
        firebaseAuth.signOut()
    }
}