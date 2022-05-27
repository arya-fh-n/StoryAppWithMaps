package com.bangkit.intermediate.storyappfinal.core.feature.story.domain.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.bangkit.intermediate.storyappfinal.core.feature.story.data.paging.StoryPagingSource
import com.bangkit.intermediate.storyappfinal.core.feature.story.data.repository.StoryRepository
import com.bangkit.intermediate.storyappfinal.core.feature.story.domain.response.ListStoryItem
import com.bangkit.intermediate.storyappfinal.core.util.Result
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetStoryUseCase @Inject constructor (
    private val storyRepository: StoryRepository
) {
    private val _story = MutableLiveData<List<ListStoryItem?>?>()

    operator fun invoke(token: String): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(pageSize = 8),
            pagingSourceFactory = {
                StoryPagingSource(storyRepository = storyRepository, token = token)
            }
        ).liveData
    }

    operator fun invoke(token: String, location: Int): LiveData<Result<List<ListStoryItem?>?>> = liveData {
        emit(Result.Loading)
        try {
            val response = storyRepository.getStoryLocation(token, location)
            if (!response.error) {
                _story.value = response.listStory
                val tempData: LiveData<Result<List<ListStoryItem?>?>> = _story.map { map ->
                    Result.Success(map)
                }
                emitSource(tempData)
            } else {
                emit(Result.Error(response.message))
            }
        } catch (e: HttpException) {
            emit(Result.Error(e.localizedMessage ?: "An error occurred."))
        } catch (e: IOException) {
            emit(Result.Error("Cannot reach server. Please check your internet connection."))
        }
    }
}