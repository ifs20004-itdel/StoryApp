package com.example.storyapp.di

import android.content.Context
import com.example.storyapp.data.local.StoryRepository
import com.example.storyapp.data.local.database.StoryDatabase
import com.example.storyapp.data.network.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return StoryRepository(database, apiService)
    }
}