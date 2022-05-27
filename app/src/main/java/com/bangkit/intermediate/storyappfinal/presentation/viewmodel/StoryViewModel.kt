package com.bangkit.intermediate.storyappfinal.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bangkit.intermediate.storyappfinal.core.feature.story.domain.response.AddStoryResponse
import com.bangkit.intermediate.storyappfinal.core.feature.story.domain.usecase.AddStoryUseCase
import com.bangkit.intermediate.storyappfinal.core.feature.story.domain.usecase.GetStoryUseCase
import com.bangkit.intermediate.storyappfinal.core.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class StoryViewModel @Inject constructor(
    private val getStoryUseCase: GetStoryUseCase,
    private val addStoryUseCase: AddStoryUseCase
): ViewModel() {
    fun getStoryPager(token: String) = getStoryUseCase(token)
    fun getStoryLocation(token: String, location: Int) = getStoryUseCase(token, location)
    fun addStory(token: String, file: MultipartBody.Part, desc: RequestBody, lat: Double?, lon: Double?): LiveData<Result<AddStoryResponse?>?> = addStoryUseCase(token, file, desc, lat, lon)
}