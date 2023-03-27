package com.example.uioproject


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.uioproject.R
import com.example.uioproject.RetrofitClient
import com.yalantis.ucrop.UCrop
import okhttp3.MediaType
import okhttp3.RequestBody
import android.util.Log

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import android.util.Base64
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.ByteArrayOutputStream

class TakePhotoActivity : AppCompatActivity() {


    private lateinit var capturePhotoButton: Button
    private lateinit var editPhotoButton: Button
    private lateinit var submitPhotoButton: Button
    private lateinit var imageView: ImageView
    private var photoUri: Uri? = null

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
        private const val UCROP_REQUEST_CODE = 2
        private const val CAMERA_PERMISSION_REQUEST_CODE = 500

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_take_photo)

        capturePhotoButton = findViewById(R.id.capture_photo_button)
        editPhotoButton = findViewById(R.id.edit_photo_button)
        submitPhotoButton = findViewById(R.id.submit_photo_button)
        imageView = findViewById(R.id.photo_image_view)

        capturePhotoButton.setOnClickListener {
            Log.d("TakePhotoActivity", "Capture Photo button clicked")
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
            } else {
                takePhoto()


        }}

        editPhotoButton.setOnClickListener {
            Log.d("TakePhotoActivity", "Edit Photo button clicked")
            launchPhotoEditor()
        }

        submitPhotoButton.setOnClickListener {
            Log.d("TakePhotoActivity", "Submit Photo button clicked")
            submitPhoto()
        }

    }

    private fun takePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            val photoFile = File.createTempFile("temp_photo", ".jpg", cacheDir)
            photoUri = FileProvider.getUriForFile(this, "$packageName.fileprovider", photoFile)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)

    }

    private fun launchPhotoEditor() {
        photoUri?.let { uri ->
            val destinationUri = Uri.fromFile(File(cacheDir, "edited_photo.jpg"))
            UCrop.of(uri, destinationUri).withAspectRatio(1f, 1f).start(this, UCROP_REQUEST_CODE)
        }
    }

    private fun submitPhoto() {
        photoUri?.let { uri ->
            println("picture")
            val file = File(uri.path)
            val requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file)
            val photo = MultipartBody.Part.createFormData("photo", file.name, requestFile)
            println("picture2")
            val userId = CurrentUser.userId ?: return
            val tagId = CurrentUser.tagId ?: return
            val fileName = file.name
            println("picture3")
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val byteArray = baos.toByteArray()
            val base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT)

            val apiService = RetrofitClient.instance

            apiService.uploadPhoto(photo, userId, tagId, "test.txt", "base64Image")
                .enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>, response: Response<ResponseBody>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(
                                this@TakePhotoActivity,
                                "Photo uploaded successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                this@TakePhotoActivity, "Failed to upload photo", Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Toast.makeText(
                            this@TakePhotoActivity, "Error: ${t.message}", Toast.LENGTH_SHORT
                        ).show()
                        t.printStackTrace()
                    }
                })
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhoto()
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            imageView.setImageURI(photoUri)
        println(photoUri)
        } else if (requestCode == UCROP_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val resultUri = UCrop.getOutput(data!!)
            imageView.setImageURI(resultUri)
            photoUri = resultUri
        }
    }
}