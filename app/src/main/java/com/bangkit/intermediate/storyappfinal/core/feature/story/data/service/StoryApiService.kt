package com.bangkit.intermediate.storyappfinal.core.feature.story.data.service

import com.bangkit.intermediate.storyappfinal.core.feature.story.domain.response.AddStoryResponse
import com.bangkit.intermediate.storyappfinal.core.feature.story.domain.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface StoryApiService {
    @GET("stories")
    suspend fun getStory(
        @Header("Authorization") token: String,
        @Query("page") page: Int?,
        @Query("size") size: Int?
    ): StoryResponse

    @GET("stories")
    suspend fun getStoryLocation(
        @Header("Authorization") token: String,
        @Query("location") location: Int
    ): StoryResponse

    @Multipart
    @POST("stories")
    suspend fun addStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: Double?,
        @Part("lon") lon: Double?,
    ): AddStoryResponse
}