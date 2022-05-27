package com.bangkit.intermediate.storyappfinal.core.feature.story.data.repository

import com.bangkit.intermediate.storyappfinal.core.feature.story.data.service.StoryApiService
import com.bangkit.intermediate.storyappfinal.core.feature.story.domain.response.AddStoryResponse
import com.bangkit.intermediate.storyappfinal.core.feature.story.domain.response.StoryResponse
import com.bangkit.intermediate.storyappfinal.core.feature.story.domain.service.StoryRepositoryService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class StoryRepository @Inject constructor(
    private val storyApiService: StoryApiService
): StoryRepositoryService {
    override suspend fun getStory(token: String, page: Int?, size: Int?): StoryResponse {
        return storyApiService.getStory(token, page, size)
    }

    override suspend fun getStoryLocation(token: String, location: Int): StoryResponse {
        return storyApiService.getStoryLocation(token, location)
    }

    override suspend fun addStory(
        token: String,
        file: MultipartBody.Part,
        desc: RequestBody,
        lat: Double?,
        lon: Double?
    ): AddStoryResponse {
        return storyApiService.addStory(token, file, desc, lat, lon)
    }

}