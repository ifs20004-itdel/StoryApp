package com.example.storyapp.view.main

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.*
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.ViewModelFactory
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.model.UserPreference
import com.example.storyapp.utils.generateLinks
import com.example.storyapp.view.register.RegisterActivity
import java.util.Locale
import androidx.datastore.preferences.core.Preferences
import com.example.storyapp.view.liststory.ListStoryActivity


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity(){

    private var binding : ActivityMainBinding? = null
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        val registerLabel  = binding?.registerLabel

        registerLabel?.generateLinks(
            Pair(if (Locale.getDefault().country.equals("US")) "Register" else "Daftar", View.OnClickListener {
                val intent = Intent(this@MainActivity, RegisterActivity::class.java)
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
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[MainViewModel::class.java]

        mainViewModel.getUser().observe(this){
         user ->
            if(user.isLogin){
                val intent = Intent(this@MainActivity, ListStoryActivity::class.java )
                startActivity(intent)
            }else{
                startActivity(Intent(this@MainActivity,MainActivity::class.java))
                finish()
            }
        }
    }

    private fun setupAction(){
        binding?.btnLogin?.setOnClickListener{
            val email = binding?.loginEmail?.text.toString()
            val password = binding?.loginPassword?.text.toString()

            mainViewModel.validateLogin(email, password)
//Perlu penambahan
            mainViewModel.login()
            AlertDialog.Builder(this).apply {
                setTitle("Success!")
                setMessage("Anda berhasil login.")
                setPositiveButton("OK"){
                    _,_->
                    val intent = Intent(context, ListStoryActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
            }

        }
    }

}