package com.srhan.bostaandroidtask.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.srhan.bostaandroidtask.databinding.PhotoListItemBinding
import com.srhan.bostaandroidtask.domain.model.Photo

class PhotoAdapter : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    var onImageClick: ((String) -> Unit)? = null

    private class ItemDiffCallback : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, ItemDiffCallback())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        return PhotoViewHolder(
            PhotoListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo = differ.currentList[position]
        holder.bind(photo)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class PhotoViewHolder(private val itemBinding: PhotoListItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(photo: Photo) {
            itemBinding.apply {
                Glide.with(root).load(photo.url).into(image)
                root.setOnClickListener {
                    onImageClick?.invoke(photo.url)
                }
            }

        }

    }
}