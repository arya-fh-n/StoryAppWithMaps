package com.bangkit.intermediate.storyappfinal.core.feature.auth.domain.service

import com.bangkit.intermediate.storyappfinal.core.feature.auth.domain.response.User
import kotlinx.coroutines.flow.Flow

interface UserRepositoryService {

    fun getUser(): Flow<User?>

    suspend fun setUser(user: User?)
}