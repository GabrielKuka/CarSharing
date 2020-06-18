package com.car.sharing.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.car.sharing.R
import com.car.sharing.models.CarPhoto

class PhotoViewPagerAdapter(private val context: Context, private val photos: List<CarPhoto>) :
    RecyclerView.Adapter<PhotoViewPagerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.view_pager_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentPhoto = photos[position]
        Glide.with(context).load(currentPhoto.url).optionalFitCenter().into(holder.imageView!!)
    }

    override fun getItemCount(): Int {
        return photos.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView? = null

        init {
            imageView = itemView.findViewById(R.id.view_pager_photo)
        }
    }
}