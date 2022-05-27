package com.bangkit.intermediate.storyappfinal.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.bangkit.intermediate.storyappfinal.core.feature.auth.domain.usecase.LoginUseCase
import com.bangkit.intermediate.storyappfinal.core.feature.auth.domain.usecase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase
): ViewModel() {
    fun login(email: String, password: String) = loginUseCase(email, password)
    fun register(name: String, email: String, password: String) = registerUseCase(name, email, password)
}