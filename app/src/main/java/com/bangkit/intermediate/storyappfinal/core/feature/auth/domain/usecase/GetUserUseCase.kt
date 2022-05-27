package com.bangkit.intermediate.storyappfinal.core.feature.auth.domain.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.bangkit.intermediate.storyappfinal.core.feature.auth.data.repository.UserRepository
import com.bangkit.intermediate.storyappfinal.core.feature.auth.domain.response.User
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(): LiveData<User?> = liveData {
        emitSource(userRepository.getUser().asLiveData())
    }
}