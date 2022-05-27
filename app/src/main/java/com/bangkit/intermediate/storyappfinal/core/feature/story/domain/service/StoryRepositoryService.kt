package com.bangkit.intermediate.storyappfinal.core.feature.story.domain.service

import com.bangkit.intermediate.storyappfinal.core.feature.story.domain.response.AddStoryResponse
import com.bangkit.intermediate.storyappfinal.core.feature.story.domain.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface StoryRepositoryService {
    suspend fun getStory(token: String, page: Int?, size: Int?): StoryResponse
    suspend fun getStoryLocation(token: String, location: Int): StoryResponse
    suspend fun addStory(token: String, file: MultipartBody.Part, desc: RequestBody, lat: Double?, lon: Double?): AddStoryResponse
}