package com.bangkit.intermediate.storyappfinal.core.feature.auth.domain.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.bangkit.intermediate.storyappfinal.core.feature.auth.data.repository.AuthRepository
import com.bangkit.intermediate.storyappfinal.core.feature.auth.data.repository.UserRepository
import com.bangkit.intermediate.storyappfinal.core.util.Result
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    private val _login = MutableLiveData<String>()

    operator fun invoke(email: String, password: String): LiveData<Result<String>> = liveData {
        emit(Result.Loading)
        try {
            val loginResponse = authRepository.login(email, password)
            if (loginResponse.error) {
                emit(Result.Error(loginResponse.message))
            } else {
                _login.value = loginResponse.message
                loginResponse.loginResult?.let {
                    userRepository.setUser(it)
                }
                val tempData: LiveData<Result<String>> = _login.map { map ->
                    Result.Success(map)
                }
                emitSource(tempData)
            }
        } catch (e: HttpException) {
            emit(Result.Error(e.localizedMessage ?: "An error occurred"))
        } catch (e: IOException) {
            emit(Result.Error("Cannot reach server. Please check your internet connection."))
        }
    }
}