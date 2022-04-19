package com.nuryadincjr.storyapp.view.added

import android.Manifest.permission.*
import android.animation.AnimatorSet
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
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
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import com.nuryadincjr.storyapp.R
import com.nuryadincjr.storyapp.data.Result
import com.nuryadincjr.storyapp.data.factory.StoriesFactory
import com.nuryadincjr.storyapp.data.model.SettingsPreference
import com.nuryadincjr.storyapp.data.model.SettingsPreference.Companion.dataStore
import com.nuryadincjr.storyapp.data.remote.response.PostResponse
import com.nuryadincjr.storyapp.databinding.ActivityAddStoryBinding
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

class AddStoryActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var currentPhotoPath: String
    private var latLng: LatLng? = null
    private var getFile: File? = null

    private val addStoryViewModel: AddStoryViewModel by viewModels {
        val preference = SettingsPreference.getInstance(dataStore)
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

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[ACCESS_FINE_LOCATION] ?: false -> {
                    getMyLastLocation()
                }
                permissions[ACCESS_COARSE_LOCATION] ?: false -> {
                    getMyLastLocation()
                }
                else -> {}
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
            R.id.tv_location -> getMyLastLocation()
            R.id.iv_clear_location -> clearLocation()
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
                REQUEST_CODE,
            )
        }

        binding.apply {
            btnCamera.setOnClickListener(this@AddStoryActivity)
            btnGallery.setOnClickListener(this@AddStoryActivity)
            btnUpload.setOnClickListener(this@AddStoryActivity)
            tvLocation.setOnClickListener(this@AddStoryActivity)
            ivClearLocation.setOnClickListener(this@AddStoryActivity)
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
        val descInp = binding.tietDescription.text
        when {
            getFile == null -> {
                Toast.makeText(this, getString(R.string.error_file), Toast.LENGTH_SHORT).show()
            }
            descInp.isNullOrEmpty() -> {
                Toast.makeText(this, getString(R.string.error_description), Toast.LENGTH_SHORT)
                    .show()
            }
            else -> {
                binding.progressBar.visibility = View.VISIBLE

                val file = reduceFileImage(getFile as File)
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val photo = MultipartBody.Part.createFormData("photo", file.name, requestImageFile)
                val description = descInp.toString().toRequestBody("text/plain".toMediaType())
                val latitude = latLng?.latitude
                val longitude = latLng?.longitude

                addStoryViewModel.apply {
                    getUser().observe(this@AddStoryActivity) { user ->
                        setToken(user.token)
                    }

                    onUpload(
                        photo,
                        description,
                        latitude,
                        longitude
                    ).observe(this@AddStoryActivity) {
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
            val location = alphaAnim(tvLocation)
            val upload = alphaAnim(btnUpload)

            val buttonSet = AnimatorSet().apply {
                playTogether(camera, gallery)
            }

            AnimatorSet().apply {
                playSequentially(image, buttonSet, description, location, upload)
                start()
            }
        }
    }

    private fun getMyLastLocation() {
        if (checkPermission(ACCESS_FINE_LOCATION) &&
            checkPermission(ACCESS_COARSE_LOCATION)
        ) {
            val fusedLocationClient = getFusedLocationProviderClient(this)
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    startLocation(location)
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.error_disable_location),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    ACCESS_FINE_LOCATION,
                    ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun startLocation(location: Location) {
        latLng = LatLng(location.latitude, location.longitude)
        binding.tvLocation.text = latLng.toString()
        binding.ivClearLocation.visibility = View.VISIBLE
    }

    private fun clearLocation() {
        binding.tvLocation.text = getString(R.string.add_location)
        binding.ivClearLocation.visibility = View.GONE
        latLng = null
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(CAMERA)
        private const val REQUEST_CODE = 1
    }
}