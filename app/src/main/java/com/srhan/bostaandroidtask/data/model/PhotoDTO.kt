package com.srhan.bostaandroidtask.data.model

import com.srhan.bostaandroidtask.domain.model.Photo

data class PhotoDTO(
    val albumId: Int,
    val id: Int,
    val thumbnailUrl: String,
    val title: String,
    val url: String
)

fun PhotoDTO.asPhoto(): Photo =
    Photo(
        thumbnailUrl = thumbnailUrl,
        title = title,
        url = url
    )