package com.example.storyapp.data.retrofit

import com.example.storyapp.data.response.LoginResponse
import com.example.storyapp.data.response.RegisterResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
interface ApiService {

    @POST("register")
    fun registerUser(
        @Body registerUser: RequestBody
    ):Call<RegisterResponse>

    @POST("login")
    fun loginUser(
        @Body loginUser: RequestBody
    ):Call<LoginResponse>
}