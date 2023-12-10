package com.srhan.bostaandroidtask.data.repository

import com.srhan.bostaandroidtask.data.model.AlbumDTO
import com.srhan.bostaandroidtask.data.model.PhotoDTO
import com.srhan.bostaandroidtask.data.model.UserDTO
import com.srhan.bostaandroidtask.data.remote.ApiService
import com.srhan.bostaandroidtask.domain.repository.Repository
import com.srhan.bostaandroidtask.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RepositoryImp @Inject constructor(private val apiService: ApiService) : Repository {

    override suspend fun getUser(): Flow<Resource<UserDTO>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.getUsers()
            if (response.isSuccessful) {
                response.body()?.let {
                   val user= it.random()
                    emit(Resource.Success(user))
                }
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }

    }.flowOn(Dispatchers.IO)


    override suspend fun getAlbums(userId: Int): Flow<Resource<List<AlbumDTO>>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.getAlbums(userId)
            if (response.isSuccessful) {
                response.body()?.let{
                    emit(Resource.Success(it))
                }
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }

    }.flowOn(Dispatchers.IO)

    override suspend fun getPhotos(albumId: Int): Flow<Resource<List<PhotoDTO>>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.getPhotos(albumId)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(Resource.Success(it))
                }
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }

    }.flowOn(Dispatchers.IO)


}