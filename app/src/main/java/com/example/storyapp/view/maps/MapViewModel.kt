package com.example.storyapp.view.maps

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.storyapp.data.response.AllStoriesResponse
import com.example.storyapp.data.response.StoryResponse
import com.example.storyapp.data.retrofit.ApiConfig
import com.example.storyapp.model.UserModel
import com.example.storyapp.model.UserPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapViewModel(private val userPreference: UserPreference):ViewModel() {

    private val _story = MutableLiveData<List<StoryResponse>>()
    val story: LiveData<List<StoryResponse>> = _story

    fun getUser():LiveData<UserModel>{
        return userPreference.getUser().asLiveData()
    }

    fun getAllLatLng(token: String){
        val client = ApiConfig().getApiService()
        val getAllStories = client.getAllStories("Bearer $token",null, null, 1)
        getAllStories.enqueue(object :Callback<AllStoriesResponse>{
            override fun onResponse(
                call: Call<AllStoriesResponse>,
                response: Response<AllStoriesResponse>
            ) {
                if(response.isSuccessful){
                    val responseBody = response.body()
                    if(responseBody !=null){
                        _story.value = responseBody.listStory
                    }else{
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }
            }
            override fun onFailure(call: Call<AllStoriesResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object{
        private const val TAG = ".MapViewModel"
    }
}