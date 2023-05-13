package com.example.storyapp.view.addstory

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.R
import com.example.storyapp.ViewModelFactory
import com.example.storyapp.data.response.RegisterAndUploadResponse
import com.example.storyapp.data.retrofit.ApiConfig
import com.example.storyapp.databinding.ActivityAddStoryBinding
import com.example.storyapp.model.UserPreference
import com.example.storyapp.utils.createCustomTempFile
import com.example.storyapp.utils.reduceFileImage
import com.example.storyapp.utils.uriToFile
import com.example.storyapp.view.liststory.ListStoryActivity
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class AddStoryActivity : AppCompatActivity() {
    private lateinit var addStoryViewModel: AddStoryViewModel

    private var addStoryBinding: ActivityAddStoryBinding? = null
    private lateinit var currentPhotoPath : String
    private var getFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addStoryBinding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(addStoryBinding?.root)
        setupViewModel()

        addStoryBinding?.cameraButton?.setOnClickListener{
            startTakePhoto()
        }

        addStoryBinding?.uploadButton?.setOnClickListener{
            uploadImage()
        }

        addStoryBinding?.galleryButton?.setOnClickListener {
            startGallery()
        }
        supportActionBar?.title = resources.getString(R.string.upload)

    }


    private fun setupViewModel(){
            addStoryViewModel =  ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[AddStoryViewModel::class.java]
    }

    private fun uploadImage(){
        var token : String? = null
        addStoryViewModel.getUser().observe(this){
            userModel->
            token = userModel.token
        }
        val description = addStoryBinding?.addDescription?.text.toString()
        if(description.isEmpty()){
            Toast.makeText(this@AddStoryActivity, resources.getString(R.string.empty_description), Toast.LENGTH_SHORT).show()
        }
        if(getFile !=null){
            val file =  reduceFileImage(getFile as File)
            val requestDescription = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )
            val apiService = ApiConfig().getApiService()
            val uploadImageRequest = apiService.uploadImage("Bearer $token",imageMultipart, requestDescription)
            uploadImageRequest.enqueue(object : Callback<RegisterAndUploadResponse>{
                override fun onResponse(
                    call: Call<RegisterAndUploadResponse>,
                    response: Response<RegisterAndUploadResponse>
                ) {
                    if(response.isSuccessful){
                        val responseBody = response.body()
                        if(responseBody!=null && !responseBody.error){
                            val builder = AlertDialog.Builder(this@AddStoryActivity)
                            builder
                                .setTitle(R.string.success)
                                .setMessage(R.string.upload_success)
                                .setPositiveButton("OK"){
                                        _,_->
                                    val intent = Intent(
                                        this@AddStoryActivity,
                                        ListStoryActivity::class.java
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
                override fun onFailure(call: Call<RegisterAndUploadResponse>, t: Throwable) {
                    Toast.makeText(this@AddStoryActivity, t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }else{
            Toast.makeText(this@AddStoryActivity,resources.getString(R.string.input_picture), Toast.LENGTH_SHORT).show()
        }
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val photoURI : Uri = FileProvider.getUriForFile(
                this@AddStoryActivity,
                "com.example.storyapp",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun startGallery(){
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type="image/*"
        val choose = Intent.createChooser(intent,resources.getString(R.string.upload))
        launcherIntentGallery.launch(choose)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if(it.resultCode== RESULT_OK){
            val myFile = File(currentPhotoPath)
            myFile.let {
                    file->
                getFile = file
                addStoryBinding?.previewImageView?.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        result ->
        if(result.resultCode== RESULT_OK){
            val selectedImg = result.data?.data as Uri
            selectedImg.let {
                uri->
                val myFile = uriToFile(uri, this@AddStoryActivity)
                getFile = myFile
                addStoryBinding?.previewImageView?.setImageURI(uri)
            }
        }
    }
}