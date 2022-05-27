package com.bangkit.intermediate.storyappfinal.presentation.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bangkit.intermediate.storyappfinal.R
import com.bangkit.intermediate.storyappfinal.core.util.Internet
import com.bangkit.intermediate.storyappfinal.core.util.UriUtils
import com.bangkit.intermediate.storyappfinal.core.util.Result
import com.bangkit.intermediate.storyappfinal.core.util.UriUtils.Companion.rotateBitmap
import com.bangkit.intermediate.storyappfinal.databinding.ActivityAddStoryBinding
import com.bangkit.intermediate.storyappfinal.presentation.viewmodel.StoryViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

@AndroidEntryPoint
class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val storyViewModel: StoryViewModel by viewModels()

    private var getFile: File? = null

    private var lat: Double? = null
    private var lon: Double? = null

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    resources.getString(R.string.permission_failed),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this@AddStoryActivity)

        val token = intent.getStringExtra(EXTRA_TOKEN)

        getMyLastLocation()

        binding.apply {
            btnCamera.setOnClickListener {
                startCamera()
            }
            btnGallery.setOnClickListener {
                startGallery()
            }
            btnUpload.setOnClickListener {
                if (Internet.isAvailable(this@AddStoryActivity)) {
                    if (etDesc.text.isNullOrBlank()) {
                        etDesc.error = resources.getString(R.string.required)
                        etDesc.requestFocus()
                    } else {
                        etDesc.clearFocus()
                        token?.let {
                            addStory(it)
                        }
                    }
                } else {
                    Toast.makeText(this@AddStoryActivity, resources.getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getMyLastLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    this.lat = location.latitude
                    this.lon = location.longitude
                } else {
                    Toast.makeText(
                        this@AddStoryActivity,
                        "Location is not found. Try again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun addStory(token: String) {
        if (getFile != null) {
            getFile?.let { file ->
                UriUtils.reduceFileImage(file)
                val desc = binding.etDesc.text.toString().toRequestBody("text/plain".toMediaType())
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    file.name,
                    requestImageFile
                )

                storyViewModel.addStory("Bearer $token", imageMultipart, desc, lat, lon).observe(this@AddStoryActivity) { result ->
                        when (result) {
                            is Result.Loading -> {
                                Log.d(TAG, "Loading...")
                                binding.apply {
                                    btnCamera.isClickable = false
                                    btnGallery.isClickable = false
                                    btnUpload.isClickable = false
                                    pbAddStory.visibility = View.VISIBLE
                                }
                            }
                            is Result.Success -> {
                                Toast.makeText(this@AddStoryActivity, resources.getString(R.string.story_created), Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this@AddStoryActivity, MainActivity::class.java))
                                finish()
                                Log.d(TAG, result.data?.message.toString())
                            }
                            is Result.Error -> {
                                binding.apply {
                                    btnCamera.isClickable = true
                                    btnGallery.isClickable = true
                                    btnUpload.isClickable = true
                                    pbAddStory.visibility = View.GONE
                                }
                                Log.d(TAG, result.error)
                            }
                            else -> {}
                        }
                    }
            }
        } else {
            Toast.makeText(this@AddStoryActivity, resources.getString(R.string.insert_image), Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    getMyLastLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    getMyLastLocation()
                }
                else -> {

                }
            }
        }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri

            val myFile = UriUtils.uriToFile(selectedImg, this@AddStoryActivity)

            getFile = myFile

            binding.imgPreview.setImageURI(selectedImg)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            getFile = myFile
            val result = rotateBitmap(
                BitmapFactory.decodeFile(getFile?.path),
                isBackCamera
            )

            binding.imgPreview.setImageBitmap(result)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun startCamera() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCamera.launch(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@AddStoryActivity, MainActivity::class.java))
        finish()
    }

    companion object {
        const val CAMERA_RESULT = 200
        const val EXTRA_TOKEN = "token"
        private const val TAG = "AddStoryActivity"
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}