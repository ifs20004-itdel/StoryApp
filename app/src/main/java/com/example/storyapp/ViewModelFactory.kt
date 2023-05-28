package com.example.storyapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.model.UserPreference
import com.example.storyapp.view.addstory.AddStoryViewModel
import com.example.storyapp.view.detailstory.DetailStoryViewModel
import com.example.storyapp.view.liststory.ListStoryViewModel
import com.example.storyapp.view.main.MainViewModel
import com.example.storyapp.view.maps.MapViewModel

class ViewModelFactory(private val pref: UserPreference): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(MainViewModel::class.java)->{
                MainViewModel(pref) as T
            }
            modelClass.isAssignableFrom(ListStoryViewModel::class.java)->{
                ListStoryViewModel(pref) as T
            }
            modelClass.isAssignableFrom(DetailStoryViewModel::class.java)->{
                DetailStoryViewModel(pref) as T
            }
            modelClass.isAssignableFrom(AddStoryViewModel::class.java)->{
                AddStoryViewModel(pref) as T
            }
            modelClass.isAssignableFrom(MapViewModel::class.java)->{
                MapViewModel(pref) as T
            }
            else-> throw IllegalArgumentException("Unknown ViewModel class: "+modelClass.name)
        }
    }

}