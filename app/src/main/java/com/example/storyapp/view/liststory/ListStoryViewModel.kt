package com.example.storyapp.view.liststory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp.data.entity.UserStory
import com.example.storyapp.data.local.StoryRepository
import com.example.storyapp.model.UserModel
import com.example.storyapp.model.UserPreference
import kotlinx.coroutines.launch

class ListStoryViewModel(private val userPreference: UserPreference, private val repository: StoryRepository):ViewModel() {

    fun logout(){
        viewModelScope.launch {
            userPreference.logout()
        }
    }
    fun getUser():LiveData<UserModel>{
        return userPreference.getUser().asLiveData()
    }

    fun getAllStories(token: String): LiveData<PagingData<UserStory>> =
        repository.getStory(token).cachedIn(viewModelScope)
}