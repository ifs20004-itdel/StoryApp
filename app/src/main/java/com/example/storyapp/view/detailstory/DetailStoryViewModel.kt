package com.example.storyapp.view.detailstory

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.storyapp.data.network.response.DetailStoryResponse
import com.example.storyapp.data.network.response.StoryResponse
import com.example.storyapp.data.network.retrofit.ApiConfig
import com.example.storyapp.model.UserModel
import com.example.storyapp.model.UserPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailStoryViewModel(private val userPreference: UserPreference):ViewModel() {

    private val _detail = MutableLiveData<StoryResponse>()
    val detail : LiveData<StoryResponse> = _detail

    fun getUser():LiveData<UserModel>{
        return userPreference.getUser().asLiveData()
    }

    fun getStoryDetail(id: String, token: String){
        val client = ApiConfig.getApiService()
        val getStoryDetail = client.getDetailStory("Bearer $token", id)
        getStoryDetail.enqueue(object : Callback<DetailStoryResponse>{
            override fun onResponse(
                call: Call<DetailStoryResponse>,
                response: Response<DetailStoryResponse>
            ) {
                if(response.isSuccessful){
                    val responseBody  = response.body()
                    if(responseBody !=null){
                        _detail.value = responseBody.story
                    }else{
                        Log.e(TAG, response.message())
                    }
                }
            }
            override fun onFailure(call: Call<DetailStoryResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
    companion object{
        private const val TAG = ".DetailStoryViewModel"
    }
}