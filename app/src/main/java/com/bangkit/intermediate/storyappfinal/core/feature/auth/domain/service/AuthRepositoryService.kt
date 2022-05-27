package com.bangkit.intermediate.storyappfinal.core.feature.auth.domain.service

import com.bangkit.intermediate.storyappfinal.core.feature.auth.domain.response.LoginResponse
import com.bangkit.intermediate.storyappfinal.core.feature.auth.domain.response.RegisterResponse

interface AuthRepositoryService {

    suspend fun login(email: String, password: String): LoginResponse

    suspend fun register(name: String, email: String, password: String): RegisterResponse
}