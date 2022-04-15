package com.nuryadincjr.storyapp.view.added

import android.Manifest
import android.animation.AnimatorSet
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.nuryadincjr.storyapp.R
import com.nuryadincjr.storyapp.data.Result
import com.nuryadincjr.storyapp.data.factory.StoriesFactory
import com.nuryadincjr.storyapp.data.model.UsersPreference
import com.nuryadincjr.storyapp.data.remote.response.PostResponse
import com.nuryadincjr.storyapp.databinding.ActivityAddStoryBinding
import com.nuryadincjr.storyapp.util.Constant.PREF_SESSION
import com.nuryadincjr.storyapp.util.Constant.alphaAnim
import com.nuryadincjr.storyapp.util.createTempFile
import com.nuryadincjr.storyapp.util.reduceFileImage
import com.nuryadincjr.storyapp.util.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREF_SESSION)

class AddStoryActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var currentPhotoPath: String
    private var getFile: File? = null

    private val addStoryViewModel: AddStoryViewModel by viewModels {
        val preference = UsersPreference.getInstance(dataStore)
        StoriesFactory.getInstance(this, preference)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            val result = BitmapFactory.decodeFile(myFile.path)

            getFile = myFile
            addStoryViewModel.setFile(myFile)
            binding.imageView.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val selectedImg: Uri = it.data?.data as Uri
            val myFile = uriToFile(selectedImg, this)

            getFile = myFile
            addStoryViewModel.setFile(myFile)
            binding.imageView.setImageURI(selectedImg)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_story)

        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        onSubscribe()
        playAnimation()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_camera -> startTakePhoto()
            R.id.btn_gallery -> startGallery()
            R.id.btn_upload -> startUploadStory()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            if (!allPermissionsGranted()) {
                Toast.makeText(this, getString(R.string.error_permission), Toast.LENGTH_SHORT)
                    .show()
                finish()
            }
        }
    }

    private fun onSubscribe() {
        addStoryViewModel.getFile().observe(this@AddStoryActivity) {
            getFile = it
            val result = BitmapFactory.decodeFile(it?.path)
            binding.imageView.setImageBitmap(result)
        }
    }

    private fun setupView() {
        supportActionBar?.apply {
            title = getString(R.string.title_add_story)
            setDisplayHomeAsUpEnabled(true)
            elevation = 0f
        }

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE
            )
        }

        binding.apply {
            btnCamera.setOnClickListener(this@AddStoryActivity)
            btnGallery.setOnClickListener(this@AddStoryActivity)
            btnUpload.setOnClickListener(this@AddStoryActivity)
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(this, packageName, it)

            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun startGallery() {
        val intent = Intent().apply {
            action = Intent.ACTION_GET_CONTENT
            type = "image/*"
        }
        val chooser = Intent.createChooser(intent, getString(R.string.title_gallery))
        launcherIntentGallery.launch(chooser)
    }

    private fun startUploadStory() {
        val description = binding.tietDescription.text
        when {
            getFile == null -> {
                Toast.makeText(this, getString(R.string.error_file), Toast.LENGTH_SHORT).show()
            }
            description.isNullOrEmpty() -> {
                Toast.makeText(this, getString(R.string.error_description), Toast.LENGTH_SHORT)
                    .show()
            }
            else -> {
                binding.progressBar.visibility = View.VISIBLE

                val file = reduceFileImage(getFile as File)
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val myStory = description.toString().toRequestBody("text/plain".toMediaType())
                val photo = MultipartBody.Part.createFormData("photo", file.name, requestImageFile)

                addStoryViewModel.apply {
                    getUser().observe(this@AddStoryActivity) { user ->
                        setToken(user.token)
                    }

                    onUpload(photo, myStory).observe(this@AddStoryActivity) {
                        onResult(it)
                    }
                }
            }
        }
    }

    private fun onResult(result: Result<PostResponse>) {
        when (result) {
            is Result.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
            }
            is Result.Success -> {
                val addStoryResponse = result.data
                val message = addStoryResponse.message

                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                finish()
            }
            is Result.Error -> {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun playAnimation() {
        binding.apply {
            val image = alphaAnim(imageView)
            val camera = alphaAnim(btnCamera)
            val gallery = alphaAnim(btnGallery)
            val description = alphaAnim(tilDescription)
            val upload = alphaAnim(btnUpload)

            val buttonSet = AnimatorSet().apply {
                playTogether(camera, gallery)
            }

            AnimatorSet().apply {
                playSequentially(image, buttonSet, description, upload)
                start()
            }
        }
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE = 1
    }
}