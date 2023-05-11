package com.example.storyapp.view.addstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.storyapp.model.UserModel
import com.example.storyapp.model.UserPreference

class AddStoryViewModel(private val userPreference: UserPreference): ViewModel() {
    fun getUser():LiveData<UserModel> {
        return userPreference.getUser().asLiveData()
    }
}