package com.nuryadincjr.storyapp.view.location

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat.getDrawable
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.nuryadincjr.storyapp.R
import com.nuryadincjr.storyapp.data.Result
import com.nuryadincjr.storyapp.data.factory.StoriesFactory
import com.nuryadincjr.storyapp.data.model.SettingsPreference
import com.nuryadincjr.storyapp.data.model.SettingsPreference.Companion.dataStore
import com.nuryadincjr.storyapp.data.remote.response.StoryItem
import com.nuryadincjr.storyapp.databinding.ActivityMapsBinding
import com.nuryadincjr.storyapp.util.Constant
import com.nuryadincjr.storyapp.view.detail.DetailsStoryActivity
import okhttp3.internal.format

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private val mapsViewModel: MapsViewModel by viewModels {
        val preference = SettingsPreference.getInstance(dataStore)
        StoriesFactory.getInstance(this, preference)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        setMapStyle()

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        onSubscribe()
        getMyLocation()

        mMap.setOnInfoWindowClickListener { marker ->
            val storyItem = marker.tag as StoryItem
            startActivity(storyItem)
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun onSubscribe() {
        mapsViewModel.apply {
            getUser().observe(this@MapsActivity) { user ->
                setToken(user.token)
                getStories().observe(this@MapsActivity) {
                    onResult(it)
                }
            }
        }
    }

    private fun onResult(result: Result<List<StoryItem>>) {
        binding.apply {
            when (result) {
                is Result.Loading -> {
                }
                is Result.Success -> {
                    val listStory = result.data
                    for (item in listStory) {
                        setLocationStory(item)
                    }
                }
                is Result.Error -> {
                    Toast.makeText(this@MapsActivity, result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setMapStyle() {
        try {
            mapsViewModel.getTheme().observe(this@MapsActivity) {
                val success = if (it) {
                    mMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                            this,
                            R.raw.night_map_style
                        )
                    )
                } else {
                    mMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                            this,
                            R.raw.light_map_style
                        )
                    )
                }

                if (!success) Log.e(TAG, getString(R.string.error_style_parsing))
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, format(getString(R.string.error_style_find), exception))
        }
    }

    private fun setLocationStory(
        item: StoryItem,
    ) {
        val latLng = LatLng(item.lat!!, item.lon!!)
        val markerOptions = MarkerOptions()
            .position(latLng)
            .title(item.name)
            .snippet(item.description)
            .icon(vectorToBitmap(R.drawable.ic_location_dot))

        mMap.apply {
            addMarker(markerOptions)?.tag = item
            animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        }
    }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun vectorToBitmap(@DrawableRes id: Int): BitmapDescriptor {
        val vectorDrawable = getDrawable(resources, id, null)
        if (vectorDrawable == null) {
            Log.e("BitmapHelper", "Resource not found")
            return BitmapDescriptorFactory.defaultMarker()
        }

        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    private fun startActivity(storyItem: StoryItem) {
        val intent = Intent(this, DetailsStoryActivity::class.java)
        intent.putExtra(Constant.DATA_STORY, storyItem)
        startActivity(intent)
    }
}