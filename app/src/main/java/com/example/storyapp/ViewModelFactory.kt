package com.example.storyapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.model.UserPreference
import com.example.storyapp.view.liststory.ListStoryViewModel
import com.example.storyapp.view.main.MainViewModel

class ViewModelFactory(private val pref: UserPreference): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(MainViewModel::class.java)->{
                MainViewModel(pref) as T
            }
            modelClass.isAssignableFrom(ListStoryViewModel::class.java)->{
                ListStoryViewModel(pref) as T
            }
            else-> throw IllegalArgumentException("Unknown ViewModel class: "+modelClass.name)
        }
    }

}