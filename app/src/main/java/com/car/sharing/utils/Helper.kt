package com.car.sharing.utils

import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.car.sharing.R

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

}