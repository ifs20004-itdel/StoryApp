package com.example.storyapp.data.local

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.example.storyapp.data.entity.UserStory
import com.example.storyapp.data.local.database.StoryDatabase
import com.example.storyapp.data.network.retrofit.ApiService

class StoryRepository(private val storyDatabase: StoryDatabase, private val apiService: ApiService) {
    fun getStory(token: String): LiveData<PagingData<UserStory>>{
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService, token),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }
}