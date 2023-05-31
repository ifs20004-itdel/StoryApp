package com.example.storyapp.view.main

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.*
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.R
import com.example.storyapp.ViewModelFactory
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.model.UserPreference
import com.example.storyapp.utils.AuthenticationCallback
import com.example.storyapp.utils.generateLinks
import com.example.storyapp.view.liststory.ListStoryActivity
import com.example.storyapp.view.register.RegisterActivity


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity(), AuthenticationCallback{

    private lateinit var binding : ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val registerLabel  = binding.registerLabel
        registerLabel.generateLinks(
            Pair(resources.getString(R.string.register) , View.OnClickListener {
                val intent = Intent(this@MainActivity, RegisterActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            })
        )
        setupView()
        setupViewModel()
        setupAction()

    }
    private fun setupView(){
        @Suppress("DEPRECATION")
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }else{
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupViewModel(){
        mainViewModel =  ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore), this)
        )[MainViewModel::class.java]

        mainViewModel.getUser().observe(this){
         user ->
            if(user.state){
                val intent = Intent(this@MainActivity, ListStoryActivity::class.java )
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
        mainViewModel.isLoading.observe(this){
            isLoading->
            showLoading(isLoading)
        }
    }
    private fun setupAction(){
        binding.btnLogin.setOnClickListener{
            val email = binding.loginEmail.text.toString()
            val password = binding.loginPassword.text.toString()
            when{
                email.isEmpty()->{
                    binding.textInputLayoutEmail.error = resources.getString(R.string.empty_error)
                }
                password.isEmpty()->{
                    binding.textInputLayoutPassword.error = resources.getString(R.string.empty_error)
                }
                else->{
                    mainViewModel.validateLogin(email,password, this)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean){
        if(isLoading){
            binding.progressBar.visibility  = View.VISIBLE
            binding.progressBar.progress = 0
            binding.progressBar.max = 100
        }else{
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onError(isLogin: Boolean?, message: String?) {
        Toast.makeText(this@MainActivity, message.toString(), Toast.LENGTH_SHORT).show()
    }
}