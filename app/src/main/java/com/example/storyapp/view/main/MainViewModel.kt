package com.example.storyapp.view.main

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.*
import com.example.storyapp.data.response.LoginResponse
import com.example.storyapp.data.response.RegisterAndUploadResponse
import com.example.storyapp.data.retrofit.ApiConfig
import com.example.storyapp.model.UserModel
import com.example.storyapp.model.UserPreference
import com.example.storyapp.utils.AuthenticationCallback
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref: UserPreference):ViewModel() {
    fun getUser(): LiveData<UserModel>{
        return pref.getUser().asLiveData()
    }
    fun login(){
        viewModelScope.launch {
            pref.login()
        }
    }
    fun saveUser(user: UserModel){
        viewModelScope.launch {
            pref.saveUser(user)
        }
    }

    fun validateLogin(email:String, password: String, stateCallback: AuthenticationCallback){
        val apiService = ApiConfig().getApiService()
        val json = """
            {
            "email": "$email",
            "password": "$password"
            }
        """.trimIndent()
        val body = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val login = apiService.loginUser(body)
        login.enqueue(object :Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if(response.isSuccessful){
                    val responseBody = response.body()
                    if(responseBody !=null && !responseBody.error){
                        login()
                        val logged = responseBody.loginResult
                        saveUser(
                            UserModel(
                                logged.userId,
                                logged.name,
                                logged.token,
                                true
                            )
                        )
                    }else{
                        stateCallback.onError(true)
                    }
                }else{
                    stateCallback.onError(true)
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e(ContentValues.TAG,"OnFailure: ${t.message.toString()}")
                stateCallback.onError(true)
            }
        })
    }

    fun validateRegister(name:String, email:String, password: String, stateCallback: AuthenticationCallback){
        val apiService = ApiConfig().getApiService()
        val json = """
            { 
                "name" : "$name",
                "email": "$email",
                "password": "$password"
            }
        """.trimIndent()
        val body = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val register = apiService.registerUser(body)
        register.enqueue(object : Callback<RegisterAndUploadResponse> {
            override fun onResponse(
                call: Call<RegisterAndUploadResponse>,
                response: Response<RegisterAndUploadResponse>
            ) {
                if(response.isSuccessful){
                    val responseBody = response.body()
                    if(responseBody!=null && !responseBody.error){
                        stateCallback.onError(false)
                    }else{
                        stateCallback.onError(true)
                    }
                }else{
                    stateCallback.onError(true)
                }
            }
            override fun onFailure(call: Call<RegisterAndUploadResponse>, t: Throwable) {
                Log.e(ContentValues.TAG,"OnFailure: ${t.message.toString()}")
                stateCallback.onError(true)
            }
        })
    }
}