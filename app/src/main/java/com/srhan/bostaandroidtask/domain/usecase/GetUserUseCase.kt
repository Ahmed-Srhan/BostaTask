package com.srhan.bostaandroidtask.domain.usecase

import com.srhan.bostaandroidtask.domain.repository.Repository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(private val repository: Repository) {

    suspend operator fun invoke() = repository.getUser()
}