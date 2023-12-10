package com.srhan.bostaandroidtask.domain.usecase

import com.srhan.bostaandroidtask.domain.repository.Repository
import javax.inject.Inject

class GetPhotosUseCase @Inject constructor(private val repository: Repository) {

    suspend operator fun invoke(albumId: Int) = repository.getPhotos(albumId)
}