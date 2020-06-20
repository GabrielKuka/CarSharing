package com.car.sharing.utils

import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.car.sharing.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

object Helper {

    fun hideKeyboard(v: View) {
        val inputMethodManager =
            ContextCompat.getSystemService(v.context, InputMethodManager::class.java)!!
        inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)

    }

    @JvmStatic
    @BindingAdapter("imageResource")
    fun setImageResource(imageView: ImageView, imageUrl: String) {
        val context = imageView.context

        val options = RequestOptions()
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)

        Glide.with(context)
            .setDefaultRequestOptions(options)
            .load(imageUrl)
            .into(imageView)
    }

    @JvmStatic
    @BindingAdapter("mainPhoto")
    fun setMainPhoto(imageView: ImageView, imageName: String) {
        val photoUrl = FirebaseDatabase.getInstance().getReference("car_photos")
            .child(imageName.substringBefore(".")).child("url")

        val options = RequestOptions()
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)

        photoUrl.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                // error
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.value != null){
                    Glide.with(imageView.context)
                        .setDefaultRequestOptions(options)
                        .load(snapshot.value.toString())
                        .into(imageView)
                }
            }
        })



    }
}