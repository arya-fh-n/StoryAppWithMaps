package com.bangkit.intermediate.storyappfinal.core.feature.auth.data.repository

import com.bangkit.intermediate.storyappfinal.core.feature.auth.data.preferences.UserPreference
import com.bangkit.intermediate.storyappfinal.core.feature.auth.domain.response.User
import com.bangkit.intermediate.storyappfinal.core.feature.auth.domain.service.UserRepositoryService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userPreference: UserPreference
): UserRepositoryService {
    override fun getUser(): Flow<User?> {
        return userPreference.getUser()
    }

    override suspend fun setUser(user: User?) {
        return userPreference.setUser(user)
    }

}