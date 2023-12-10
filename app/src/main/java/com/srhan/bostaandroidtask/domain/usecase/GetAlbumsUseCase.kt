package com.srhan.bostaandroidtask.domain.usecase

import com.srhan.bostaandroidtask.domain.repository.Repository
import javax.inject.Inject

class GetAlbumsUseCase @Inject constructor(private val repository: Repository) {

    suspend operator fun invoke(usrId: Int) = repository.getAlbums(usrId)
}