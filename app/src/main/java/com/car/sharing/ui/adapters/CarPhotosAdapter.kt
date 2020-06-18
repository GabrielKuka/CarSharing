package com.car.sharing.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.car.sharing.R
import com.car.sharing.databinding.CarPhotoItemBinding
import com.car.sharing.models.CarPhoto

class CarPhotosAdapter(private val interaction: CarPhotoInteraction? = null) :
    RecyclerView.Adapter<CarPhotosAdapter.CarPhotoViewHolder>() {

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private fun getCarPhotoAt(position: Int): CarPhoto {
        return differ.currentList[position]
    }

    fun submitList(list: List<CarPhoto>) {
        differ.submitList(null)
        differ.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarPhotoViewHolder {

        val binder: CarPhotoItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.car_photo_item,
            parent,
            false
        )

        return CarPhotoViewHolder(binder)
    }

    override fun onBindViewHolder(holder: CarPhotoViewHolder, position: Int) {
        holder.binder.interaction = interaction
        holder.binder.carPhoto = getCarPhotoAt(position)
    }


    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CarPhoto>() {

        override fun areItemsTheSame(oldItem: CarPhoto, newItem: CarPhoto): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: CarPhoto, newItem: CarPhoto): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    class CarPhotoViewHolder
        (
        internal val binder: CarPhotoItemBinding
    ) : RecyclerView.ViewHolder(binder.root)

    interface CarPhotoInteraction {
        fun onCarPhotoSelected(carPhoto: CarPhoto)
        fun removePhoto(carPhoto: CarPhoto)
    }
}

