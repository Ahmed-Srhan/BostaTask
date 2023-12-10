package com.srhan.bostaandroidtask.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.srhan.bostaandroidtask.data.model.asPhoto
import com.srhan.bostaandroidtask.domain.model.Photo
import com.srhan.bostaandroidtask.domain.usecase.GetPhotosUseCase
import com.srhan.bostaandroidtask.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumDetailsViewModel @Inject constructor(private val getPhotosUseCase: GetPhotosUseCase) :
    ViewModel() {
    private val _photos = MutableStateFlow<Resource<List<Photo>>?>(null)
    val photos get() = _photos.asStateFlow()


    private val _filteredPhotos = MutableStateFlow<List<Photo>>(emptyList())
    val filteredPhotos = _filteredPhotos.asStateFlow()


    fun getPhotos(albumId: Int) = viewModelScope.launch {
        getPhotosUseCase(albumId).onEach { photoResult ->
            when (photoResult) {
                is Resource.Loading -> _photos.emit(Resource.Loading())
                is Resource.Success -> {

                    val result = photoResult.data
                    result?.let {
                        val filteredList = it.map { photoDTO ->
                            photoDTO.asPhoto()
                        }
                        _photos.emit(Resource.Success(filteredList))
                        _filteredPhotos.value = filteredList

                    }

                }

                is Resource.Error -> _photos.emit(Resource.Error(photoResult.message.toString()))
            }
        }.launchIn(this)


    }

    fun filterPhotos(photos: List<Photo>, query: String) = viewModelScope.launch {
        val filtered = if (query.isEmpty()) {
            photos
        } else {
            photos.filter { it.title.contains(query, ignoreCase = true) }
        }
        _filteredPhotos.value = filtered
    }

}