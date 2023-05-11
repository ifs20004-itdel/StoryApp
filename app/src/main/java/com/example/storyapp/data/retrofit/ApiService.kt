package com.example.storyapp.data.retrofit

import com.example.storyapp.data.response.*
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
    fun getAllStories(
        @Header("Authorization") token:String,
        @Query("page") page:Int?,
        @Query("size") size:Int?,
        @Query("location") location: Int? = 0 or 1
    ):Call<AllStoriesResponse>

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
        @Part("description") description: RequestBody
    ): Call<RegisterAndUploadResponse>
}