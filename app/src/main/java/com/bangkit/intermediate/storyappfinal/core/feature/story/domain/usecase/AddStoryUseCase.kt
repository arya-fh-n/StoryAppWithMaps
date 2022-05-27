package com.bangkit.intermediate.storyappfinal.core.feature.story.domain.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.bangkit.intermediate.storyappfinal.core.feature.story.data.repository.StoryRepository
import com.bangkit.intermediate.storyappfinal.core.feature.story.domain.response.AddStoryResponse
import com.bangkit.intermediate.storyappfinal.core.util.Result
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AddStoryUseCase @Inject constructor(
    private val storyRepository: StoryRepository
) {
    private val _story = MutableLiveData<AddStoryResponse?>()

    operator fun invoke(token: String, file: MultipartBody.Part, desc: RequestBody, lat: Double?, lon: Double?): LiveData<Result<AddStoryResponse?>?> = liveData {
        emit(Result.Loading)
        try {
            _story.value = storyRepository.addStory(token, file, desc, lat, lon)
            val tempData: LiveData<Result<AddStoryResponse?>?> = _story.map { map ->
                Result.Success(map)
            }
            emitSource(tempData)
        } catch (e: HttpException) {
            emit(Result.Error(e.localizedMessage ?: "An error occurred."))
        } catch (e: IOException) {
            emit(Result.Error("Cannot reach server. Please check your internet connection."))
        }
    }
}