package com.srhan.bostaandroidtask.domain.repository

import com.srhan.bostaandroidtask.data.model.AlbumDTO
import com.srhan.bostaandroidtask.data.model.PhotoDTO
import com.srhan.bostaandroidtask.data.model.UserDTO
import com.srhan.bostaandroidtask.util.Resource
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun getUser(): Flow<Resource<UserDTO>>
    suspend fun getAlbums(userId: Int): Flow<Resource<List<AlbumDTO>>>
    suspend fun getPhotos(albumId: Int): Flow<Resource<List<PhotoDTO>>>

}