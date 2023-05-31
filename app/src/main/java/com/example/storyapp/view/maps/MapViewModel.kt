package com.example.storyapp.view.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.storyapp.data.network.response.StoryResponse
import com.example.storyapp.data.network.retrofit.ApiConfig
import com.example.storyapp.model.UserModel
import com.example.storyapp.model.UserPreference

class MapViewModel(private val userPreference: UserPreference):ViewModel() {

    private val _story = MutableLiveData<List<StoryResponse>>()
    val story: LiveData<List<StoryResponse>> = _story

    fun getUser():LiveData<UserModel>{
        return userPreference.getUser().asLiveData()
    }

    suspend fun getAllLatLng(token: String){
        val client = ApiConfig.getApiService()
        val getAllStories = client.getAllStories("Bearer $token",null, null, 1)
        val responseData = getAllStories.listStory
        _story.value = responseData
    }
}