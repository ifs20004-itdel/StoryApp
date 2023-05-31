package com.example.storyapp.data.local.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.storyapp.data.entity.UserStory

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: List<UserStory>)

    @Query("SELECT * FROM user_story")
    fun getAllStory(): PagingSource<Int, UserStory>

    @Query("DELETE FROM user_story")
    suspend fun deleteAll()
}