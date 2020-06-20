package com.car.sharing.ui.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.car.sharing.R
import com.car.sharing.models.CarPhoto
import com.iitr.kaishu.nsidedprogressbar.NSidedProgressBar

class PhotoViewPagerAdapter(private val context: Context, private val photos: List<CarPhoto>) :
    RecyclerView.Adapter<PhotoViewPagerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.view_pager_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentPhoto = photos[position]

        val options = RequestOptions()
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)

        Glide.with(context).setDefaultRequestOptions(options).load(currentPhoto.url).fitCenter()
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.pBar?.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.pBar?.visibility = View.GONE
                    return false
                }
            }).into(holder.imageView!!)
    }

    override fun getItemCount(): Int {
        return photos.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView? = null
        var pBar: NSidedProgressBar? = null

        init {
            imageView = itemView.findViewById(R.id.view_pager_photo)
            pBar = itemView.findViewById(R.id.pBar)
        }
    }
}