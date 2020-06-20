package com.car.sharing.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.car.sharing.R
import com.car.sharing.databinding.RatingItemBinding
import com.car.sharing.models.Rating

class RatingAdapter(private val ratingInteraction: RatingInteraction? = null) :
    RecyclerView.Adapter<RatingAdapter.RatingViewHolder>() {

    fun submitList(list: List<Rating>) {
        differ.submitList(list)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private fun getRatingAt(position: Int): Rating{
        return differ.currentList[position]
    }


    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Rating>() {

        override fun areItemsTheSame(oldItem: Rating, newItem: Rating): Boolean {
            return oldItem.rateId == newItem.rateId
        }

        override fun areContentsTheSame(oldItem: Rating, newItem: Rating): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatingViewHolder {

        val binder: RatingItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.rating_item,
            parent,
            false
        )

        return RatingViewHolder(binder)
    }

    override fun onBindViewHolder(holder: RatingViewHolder, position: Int) {

        holder.binder.rating = getRatingAt(position)

    }

    class RatingViewHolder
        (
        internal val binder: RatingItemBinding
    ) : RecyclerView.ViewHolder(binder.root)

    interface RatingInteraction {
        fun onRatingSelected(rating: Rating)
    }
}

