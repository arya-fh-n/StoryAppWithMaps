package com.bangkit.intermediate.storyappfinal.core.feature.auth.data.service

import com.bangkit.intermediate.storyappfinal.core.feature.auth.domain.response.LoginResponse
import com.bangkit.intermediate.storyappfinal.core.feature.auth.domain.response.RegisterResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthApiService {

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

}