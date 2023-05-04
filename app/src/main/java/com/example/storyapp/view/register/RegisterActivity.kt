package com.example.storyapp.view.register

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.*
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import com.example.storyapp.databinding.ActivityRegisterBinding
import com.example.storyapp.utils.generateLinks
import com.example.storyapp.view.main.MainActivity
import java.util.*

class RegisterActivity : AppCompatActivity() {
    private var binding: ActivityRegisterBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding?.root)
        setupView()

        val loginLabel = binding?.loginLabel
        loginLabel?.generateLinks(
            Pair("Login", View.OnClickListener {
                val intent = Intent(this@RegisterActivity, MainActivity::class.java)
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
}