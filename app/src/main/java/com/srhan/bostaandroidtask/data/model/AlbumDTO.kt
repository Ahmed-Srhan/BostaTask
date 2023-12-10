package com.srhan.bostaandroidtask.data.model

import com.srhan.bostaandroidtask.domain.model.Album


data class AlbumDTO(
    val id: Int,
    val title: String,
    val userId: Int
)
fun AlbumDTO.asAlbum(): Album =
    Album(
        id = id,
        title = title
    )

