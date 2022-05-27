package com.bangkit.intermediate.storyappfinal.core.feature.story.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bangkit.intermediate.storyappfinal.core.feature.story.data.repository.StoryRepository
import com.bangkit.intermediate.storyappfinal.core.feature.story.domain.response.ListStoryItem
import javax.inject.Inject

class StoryPagingSource @Inject constructor(
    private val storyRepository: StoryRepository,
    private val token: String
): PagingSource<Int, ListStoryItem>() {
    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPos ->
            val anchorPage = state.closestPageToPosition(anchorPos)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val page = params.key ?: INIT_PAGE_INDEX
            val response = storyRepository.getStory(token, page, params.loadSize).listStory

            LoadResult.Page(
                data = response,
                prevKey = if (page == 1) null else page -1,
                nextKey = if (response.isNullOrEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    companion object {
        const val INIT_PAGE_INDEX = 1
    }
}