package com.example.storyapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserStoryModel(
    val name: String,
    val photoUrl:String,
    val description: String,
    val date: String
):Parcelable
