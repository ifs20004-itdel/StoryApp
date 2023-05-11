package com.example.storyapp.view.detailstory

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.storyapp.R
import com.example.storyapp.ViewModelFactory
import com.example.storyapp.data.response.StoryResponse
import com.example.storyapp.databinding.ActivityDetailStoryBinding
import com.example.storyapp.model.UserPreference
import com.example.storyapp.utils.withDateFormat
import com.faltenreich.skeletonlayout.Skeleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class DetailStoryActivity : AppCompatActivity() {
    private var detailStoryBinding: ActivityDetailStoryBinding? = null
    private lateinit var skeleton: Skeleton
    private lateinit var detailStoryViewModel: DetailStoryViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailStoryBinding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(detailStoryBinding?.root)

        skeleton = detailStoryBinding?.skeletonDetailStory!!
        skeleton.showSkeleton()

        @Suppress("DEPRECATION")
        val handler = Handler()
        handler.postDelayed({
            skeleton.showOriginal()
        }, 2000)
        setupViewModel()
        supportActionBar?.title = resources.getString(R.string.detail_story)
    }

    private fun setupViewModel(){
        detailStoryViewModel = ViewModelProvider(
            this, ViewModelFactory(UserPreference.getInstance(dataStore))
        )[DetailStoryViewModel::class.java]

        detailStoryViewModel.getUser().observe(this){
            result->
            val id = intent.getStringExtra("id") as String
            detailStoryViewModel.getStoryDetail(id, result.token)
        }
        detailStoryViewModel.detail.observe(this){
            setDetailStory(it)
        }
    }

    private fun setDetailStory(storyResponse: StoryResponse){
        detailStoryBinding?.storyImg?.let {
            Glide.with(this)
                .load(storyResponse.photoUrl)
                .into(it)
        }
        detailStoryBinding?.detailName?.text = storyResponse.name
        detailStoryBinding?.detailDate?.text = storyResponse.createdAt.withDateFormat()
        detailStoryBinding?.detailDescription?.text = storyResponse.description
    }

}