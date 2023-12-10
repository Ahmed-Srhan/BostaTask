package com.srhan.bostaandroidtask.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.srhan.bostaandroidtask.data.model.asAlbum
import com.srhan.bostaandroidtask.data.model.asUser
import com.srhan.bostaandroidtask.domain.model.Album
import com.srhan.bostaandroidtask.domain.model.User
import com.srhan.bostaandroidtask.domain.usecase.GetAlbumsUseCase
import com.srhan.bostaandroidtask.domain.usecase.GetUserUseCase
import com.srhan.bostaandroidtask.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val getAlbumsUseCase: GetAlbumsUseCase

) : ViewModel() {
    private val _user = MutableStateFlow<Resource<User>?>(null)
    val user get() = _user.asStateFlow()

    private val _albums = MutableStateFlow<Resource<List<Album>>?>(null)
    val albums get() = _albums.asStateFlow()


    init {
        getUser()
    }

    private fun getUser() = viewModelScope.launch {
        getUserUseCase().onEach { userResult ->
            when (userResult) {
                is Resource.Loading -> _user.emit(Resource.Loading())
                is Resource.Success -> {
                    val result = userResult.data?.asUser()
                    _user.emit(Resource.Success(result!!))

                }

                is Resource.Error -> _user.emit(Resource.Error(userResult.message.toString()))
            }
        }.launchIn(this)
    }

    fun getAlbums(userId: Int) = viewModelScope.launch {
        getAlbumsUseCase(userId).onEach { albumsResult ->
            when (albumsResult) {
                is Resource.Loading -> _albums.emit(Resource.Loading())
                is Resource.Success -> {
                    val result = albumsResult.data
                    result?.let {
                        val filteredList = it.map { photoDTO ->
                            photoDTO.asAlbum()
                        }
                        _albums.emit(Resource.Success(filteredList))

                    }

                }

                is Resource.Error -> _albums.emit(Resource.Error(albumsResult.message.toString()))
            }
        }.launchIn(this)
    }


}