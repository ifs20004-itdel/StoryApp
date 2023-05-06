package com.example.storyapp.view.register

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.*
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.R
import com.example.storyapp.ViewModelFactory
import com.example.storyapp.databinding.ActivityRegisterBinding
import com.example.storyapp.model.UserPreference
import com.example.storyapp.utils.AuthenticationCallback
import com.example.storyapp.utils.generateLinks
import com.example.storyapp.view.liststory.ListStoryActivity
import com.example.storyapp.view.main.MainActivity
import com.example.storyapp.view.main.MainViewModel
import java.util.*

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class RegisterActivity : AppCompatActivity(), AuthenticationCallback {
    private var binding: ActivityRegisterBinding? = null
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding?.root)
        setupView()
        setupAction()

        val loginLabel = binding?.loginLabel
        loginLabel?.generateLinks(
            Pair(resources.getString(R.string.login), View.OnClickListener {
                val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            })
        )
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
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[MainViewModel::class.java]

        mainViewModel.getUser().observe(this){
                user ->
            if(user.state){
                val intent = Intent(this@RegisterActivity, ListStoryActivity::class.java )
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }

    private fun setupAction(){
        binding?.btnRegister?.setOnClickListener{
            val name = binding?.registerName?.text.toString()
            val email = binding?.registerEmail?.text.toString()
            val password = binding?.registerPassword?.text.toString()
            when{
                name.isEmpty()->{
                    binding?.textInputLayoutNameRegister?.error = resources.getString(R.string.empty_error)
                }
                email.isEmpty()->{
                    binding?.textInputLayoutEmailRegister?.error = resources.getString(R.string.empty_error)
                }
                password.isEmpty()->{
                    binding?.textInputLayoutPasswordRegister?.error = resources.getString(R.string.empty_error)
                }
                else->{
                    mainViewModel.validateRegister(name,email,password, this)
                }
            }
        }
    }

    override fun onError(isLogin: Boolean?) {
        TODO("Not yet implemented")
    }
}