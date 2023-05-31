package com.example.storyapp.data.local

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.storyapp.data.entity.UserStory
import com.example.storyapp.data.local.database.RemoteKeys
import com.example.storyapp.data.local.database.StoryDatabase
import com.example.storyapp.data.network.retrofit.ApiService

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator (
    private val database: StoryDatabase,
    private val apiService: ApiService,
    private val token : String,
        ): RemoteMediator<Int, UserStory>()  {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UserStory>
    ): MediatorResult {
        val page = when(loadType){
            LoadType.REFRESH ->{
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }
            LoadType.PREPEND->{
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND->{
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }
        return try{
            val response = apiService.getAllStories(token, page, state.config.pageSize)
            val responseData = response.listStory
            val endOfPaginationReached = responseData.isEmpty()
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.remoteKeysDao().deleteRemoteKeys()
                    database.storyDao().deleteAll()
                }
                val prevKey = if(page==1) null else page-1
                val nextKey = if(endOfPaginationReached) null else page+1
                val keys = responseData.map {
                    RemoteKeys(id = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                database.remoteKeysDao().insertAll(keys)
                val storyList = mutableListOf<UserStory>()
                responseData.forEach{
                        result->
                    val userStory = UserStory(result.id, result.photoUrl, result.createdAt, result.name)
                    storyList.add(userStory)
                }
                database.storyDao().insertStory(storyList)
            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        }catch (exception: Exception){
            Log.e("dada",exception.toString())
            MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem (state: PagingState<Int, UserStory>): RemoteKeys?{
        return state.pages.lastOrNull{
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let {
            data ->
            database.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, UserStory>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            database.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, UserStory>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.remoteKeysDao().getRemoteKeysId(id)
            }
        }
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH    }

    private companion object{
        const val INITIAL_PAGE_INDEX = 1
    }
}