package com.srhan.bostaandroidtask.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.srhan.bostaandroidtask.data.model.AlbumDTO
import com.srhan.bostaandroidtask.data.model.asAlbum
import com.srhan.bostaandroidtask.databinding.AlbumListItemBinding
import com.srhan.bostaandroidtask.domain.model.Album

class AlbumAdapter : RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    var onAlbumClick: ((Album) -> Unit)? = null

    private class ItemDiffCallback : DiffUtil.ItemCallback<Album>() {
        override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, ItemDiffCallback())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        return AlbumViewHolder(
            AlbumListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val albums = differ.currentList[position]
        holder.bind(albums)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class AlbumViewHolder(private val itemBinding: AlbumListItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(album: Album) {
            itemBinding.apply {
                albumNameTv.text = album.title

                root.setOnClickListener {
                    onAlbumClick?.invoke(album)
                }
            }

        }

    }
}