package com.example.storyapp.view.liststory

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.response.AllStoriesResponse
import com.example.storyapp.data.retrofit.ApiConfig
import com.example.storyapp.model.UserModel
import com.example.storyapp.model.UserPreference
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListStoryViewModel(private val userPreference: UserPreference):ViewModel() {


    private val _story = MutableLiveData<AllStoriesResponse?>()
    val story: LiveData<AllStoriesResponse?> = _story

    fun logout(){
        viewModelScope.launch {
            userPreference.logout()
        }
    }

    fun getUser():LiveData<UserModel>{
        return userPreference.getUser().asLiveData()
    }

    fun getAllStories(token: String){
        val client = ApiConfig().getApiService()
        val getAllStories = client.getAllStories("Bearer $token",null, null, null)
        getAllStories.enqueue(object :Callback<AllStoriesResponse>{
            override fun onResponse(
                call: Call<AllStoriesResponse>,
                response: Response<AllStoriesResponse>
            ) {
                if(response.isSuccessful){
                    val responseBody = response.body()
                    if(responseBody !=null){
                        _story.value = responseBody
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
        private const val TAG = ".ListStoryViewModel"
    }
}