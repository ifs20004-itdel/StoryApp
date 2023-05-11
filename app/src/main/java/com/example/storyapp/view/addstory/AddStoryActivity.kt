package com.example.storyapp.view.addstory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityAddStoryBinding

class AddStoryActivity : AppCompatActivity() {
    private var addStoryBinding: ActivityAddStoryBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addStoryBinding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(addStoryBinding?.root)

        supportActionBar?.title = resources.getString(R.string.story_add)
    }
}