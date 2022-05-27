package com.bangkit.intermediate.storyappfinal.core.feature.auth.data.repository

import com.bangkit.intermediate.storyappfinal.core.feature.auth.data.service.AuthApiService
import com.bangkit.intermediate.storyappfinal.core.feature.auth.domain.response.LoginResponse
import com.bangkit.intermediate.storyappfinal.core.feature.auth.domain.response.RegisterResponse
import com.bangkit.intermediate.storyappfinal.core.feature.auth.domain.service.AuthRepositoryService
import javax.inject.Inject

class AuthRepository @Inject constructor (
    private val authApiService: AuthApiService
): AuthRepositoryService {

    override suspend fun login(email: String, password: String): LoginResponse {
        return authApiService.login(email, password)
    }

    override suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return authApiService.register(name, email, password)
    }
}