package com.bangkit.intermediate.storyappfinal.core.feature.auth.domain.usecase

import com.bangkit.intermediate.storyappfinal.core.feature.auth.data.repository.UserRepository
import com.bangkit.intermediate.storyappfinal.core.feature.auth.domain.response.User
import javax.inject.Inject

class SetUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: User?) {
        userRepository.setUser(user)
    }
}