package com.example.storyapp.view.register

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
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
import com.example.storyapp.utils.isValidEmail
import com.example.storyapp.view.main.MainActivity
import com.example.storyapp.view.main.MainViewModel


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class RegisterActivity : AppCompatActivity(), AuthenticationCallback {
    private var binding: ActivityRegisterBinding? = null
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding?.root)
        setupView()
        setupViewModel()
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

        mainViewModel.isLoading.observe(this){
            showLoading(it)
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
                    mainViewModel.validateRegister(name, email, password, this)
                }
                email.isEmpty()->{
                    binding?.textInputLayoutEmailRegister?.error = resources.getString(R.string.empty_error)
                }
                password.isEmpty()->{
                    binding?.textInputLayoutPasswordRegister?.error = resources.getString(R.string.empty_error)
                }
                !email.isValidEmail()->{
                    binding?.textInputLayoutEmailRegister?.error = resources.getString(R.string.email_validation)
                    mainViewModel.validateRegister(name, email, password, this)
                }
                else->{
                    mainViewModel.validateRegister(name, email, password, this)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean){
        if(isLoading){
            binding?.progressBar?.visibility  = View.VISIBLE
        }else{
            binding?.progressBar?.visibility = View.GONE
        }
    }

    override fun onError(isLogin: Boolean?, message: String?) {
        if(isLogin == true){
            Toast.makeText(this@RegisterActivity, message, Toast.LENGTH_SHORT).show()
        }else{
            val builder = AlertDialog.Builder(this@RegisterActivity)
            builder
                .setTitle(R.string.success)
                .setMessage(R.string.register_success)
                .setPositiveButton(R.string.login){
                        _,_->
                    val intent = Intent(
                        this@RegisterActivity,
                        MainActivity::class.java
                    )
                    intent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            builder.create()
            builder.show()
        }
    }
}