package com.example.storyapp.view.liststory

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.R
import com.example.storyapp.ViewModelFactory
import com.example.storyapp.data.entity.UserStory
import com.example.storyapp.databinding.ActivityListStoryBinding
import com.example.storyapp.model.UserPreference
import com.example.storyapp.view.addstory.AddStoryActivity
import com.example.storyapp.view.detailstory.DetailStoryActivity
import com.example.storyapp.view.main.MainActivity
import com.example.storyapp.view.maps.MapsActivity
import com.faltenreich.skeletonlayout.Skeleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ListStoryActivity : AppCompatActivity() {
    private var binding:ActivityListStoryBinding? = null
    private lateinit var listStoryViewModel: ListStoryViewModel
    private lateinit var skeleton: Skeleton
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityListStoryBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding?.root)

        skeleton = binding?.skeletonStoryList!!
        skeleton.showSkeleton()
        setupViewModel()

        val handler = Handler()
        handler.postDelayed({
            skeleton.showOriginal()
        }, 2000)

        binding?.rvListStory?.apply {
            layoutManager = LinearLayoutManager(context)
            val itemDecoration =
                DividerItemDecoration(context, (layoutManager as LinearLayoutManager).orientation)
            binding?.rvListStory?.addItemDecoration(itemDecoration)
            setHasFixedSize(true)
        }

        binding?.btnAdd?.setOnClickListener{
            val intent = Intent(this@ListStoryActivity, AddStoryActivity::class.java)
            startActivity(intent)
        }
        supportActionBar?.title = resources.getString(R.string.story_list)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.maps->{
                val intent = Intent(this@ListStoryActivity, MapsActivity::class.java)
                startActivity(intent)
            }
            R.id.logout->{
                listStoryViewModel.logout()
                val intent = Intent(this@ListStoryActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupViewModel(){
        listStoryViewModel =  ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore), this)
        )[ListStoryViewModel::class.java]
        listStoryViewModel.getUser().observe(this){
            result ->
            if(result!=null){
                getData(result.token)
            }
        }
    }

    private fun getData(token: String){
        val adapter = ListStoryAdapter()
        binding?.rvListStory?.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter{
                adapter.retry()
            }
        )
        listStoryViewModel.getAllStories("Bearer $token").observe(this){
            adapter.submitData(lifecycle,it)
        }
        adapter.setOnItemClickCallback(object :ListStoryAdapter.OnItemClickCallback{
            override fun onItemClicked(storyResponse: UserStory) {
                val intent = Intent(this@ListStoryActivity,DetailStoryActivity::class.java)
                intent.putExtra("id", storyResponse.id)
                startActivity(intent)
            }
        })
    }
}