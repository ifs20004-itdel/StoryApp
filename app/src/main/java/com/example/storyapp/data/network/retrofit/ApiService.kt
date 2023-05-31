package com.example.storyapp.data.network.retrofit

import com.example.storyapp.data.network.*
import com.example.storyapp.data.network.response.AllStoriesResponse
import com.example.storyapp.data.network.response.DetailStoryResponse
import com.example.storyapp.data.network.response.LoginResponse
import com.example.storyapp.data.network.response.RegisterAndUploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("register")
    fun registerUser(
        @Body registerUser: RequestBody
    ):Call<RegisterAndUploadResponse>

    @POST("login")
    fun loginUser(
        @Body loginUser: RequestBody
    ):Call<LoginResponse>

    @GET("stories")
    suspend fun getAllStories(
        @Header("Authorization") token:String,
        @Query("page") page:Int?,
        @Query("size") size:Int?,
        @Query("location") location: Int? = null
    ): AllStoriesResponse

    @GET("stories/{id}")
    fun getDetailStory(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Call<DetailStoryResponse>

    @Multipart
    @POST("stories")
    fun uploadImage(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: Float?,
        @Part("lon") lon: Float?
    ): Call<RegisterAndUploadResponse>
}