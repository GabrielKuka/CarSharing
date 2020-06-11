package com.car.sharing.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.car.sharing.R
import com.car.sharing.databinding.PostItemBinding
import com.car.sharing.models.Post

class PostAdapter(private val postInteraction: PostInteraction? = null) :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    fun submitList(list: List<Post>) {
        differ.submitList(list)
    }

    private fun getPostAt(position: Int): Post {
        return differ.currentList[position]
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {

        val binder: PostItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.post_item,
            parent,
            false
        )

        return PostViewHolder(binder)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.binder.post = getPostAt(position)
        holder.binder.interaction = postInteraction
    }

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Post>() {

        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.owner == newItem.owner
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.equals(newItem)
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    class PostViewHolder(
        internal val binder: PostItemBinding
    ) : RecyclerView.ViewHolder(binder.root)

    interface PostInteraction {
        fun onPostSelected(position: Int, item: Post)
    }
}

