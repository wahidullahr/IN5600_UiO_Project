package com.example.uioproject


import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.yalantis.ucrop.UCrop
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


class TakePhotoActivity : AppCompatActivity(), OnMapReadyCallback {


    private var counter = 0

    private lateinit var capturePhotoButton: Button
    private lateinit var submitPhotoButton: Button
    private lateinit var imageView: ImageView
    private var photoUri: Uri? = null
    private lateinit var descriptionPhoto: EditText
    private lateinit var people: EditText
    private lateinit var minusbtn: ImageButton
    private lateinit var plusBtn: ImageButton
    private lateinit var indexview: TextView
    private lateinit var scrollview : ScrollView

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
        private const val UCROP_REQUEST_CODE = 2
        private const val CAMERA_PERMISSION_REQUEST_CODE = 500

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_take_photo)

        capturePhotoButton = findViewById(R.id.capture_photo_button)
        submitPhotoButton = findViewById(R.id.submit_photo_button)
        imageView = findViewById(R.id.photo_image_view)
        descriptionPhoto = findViewById(R.id.des_photo)
        people = findViewById(R.id.photo_people)
        plusBtn = findViewById(R.id.index_btn)
        minusbtn = findViewById(R.id.index_btn2)
        indexview = findViewById(R.id.index_view)
        scrollview = findViewById(R.id.scroll_view)


        capturePhotoButton.setOnClickListener {
            Log.d("TakePhotoActivity", "Capture Photo button clicked")
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE
                )
            } else {
                takePhoto()


            }
        }


        minusbtn.setOnClickListener {
            Log.d("TakePhotoActivity", "button clicked")
            if (counter > 0) {
                counter--
                indexview.text = counter.toString()

            }
        }
        plusBtn.setOnClickListener {
            Log.d("TakePhotoActivity", "button clicked")
            if (counter < 4) {
                counter++
                indexview.text = counter.toString()

            }
        }




        submitPhotoButton.setOnClickListener {
            Log.d("TakePhotoActivity", "Submit Photo button clicked")

            val descriptionOfPhoto = descriptionPhoto.text.toString()
            val peopleInPhoto = people.text.toString()

            Log.d("Description", descriptionOfPhoto)
            Log.d("people in Photo", peopleInPhoto)
            submitPhoto()

        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)


    }
    var location: LatLng = LatLng(0.0,0.0)
    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.addMarker(
            MarkerOptions()
                .position(location)
                .title("Marker")
        )
        googleMap.setOnCameraMoveListener { scrollview.requestDisallowInterceptTouchEvent(true)}
        googleMap.setOnMapClickListener {
            println("Print my current location" + it.latitude)
            location = it
            googleMap.clear()
            googleMap.addMarker(MarkerOptions().position(it))



        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            launchPhotoEditor()
        } else if (requestCode == UCROP_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val resultUri = UCrop.getOutput(data!!)
            imageView.setImageURI(resultUri)
            photoUri = resultUri
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

    fun Uri.toFileOrNull() : File? {
        return try {
            this.toFile()
        } catch (e : Exception) {
            e.printStackTrace()
            null
        }
    }



    private fun submitPhoto() {
        if(photoUri?.toFileOrNull()?.exists()!=true)return

        photoUri?.let { uri ->
            println("picture")
            val file = File(uri.path)
            val requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file)
            val photo = MultipartBody.Part.createFormData("photo", file.name, requestFile)
            val userId = CurrentUser.userId ?: return
            val tagId = CurrentUser.tagId ?: return
            val fileName = System.currentTimeMillis().toString() + ".jpg"
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val byteArray = baos.toByteArray()
            val base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT)


            val apiService = RetrofitClient.instance

            apiService.uploadPhoto(photo, userId, tagId, fileName, base64Image)
                .enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>, response: Response<ResponseBody>
                    ) {
                        if (response.isSuccessful) {
                            savePhotoToLocalStorage(uri, fileName)
                            Toast.makeText(
                                this@TakePhotoActivity,
                                "Photo uploaded successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            uploadTag(fileName)
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

    private fun uploadTag(photoName: String) {
        val userId = CurrentUser.userId ?: return
        val tagId = CurrentUser.tagId ?: return
        val apiService = RetrofitClient.instance

        apiService.uploadTag(
            userId = userId,
            indexUpdateTag = indexview.text.toString(),
            updateTagDes = descriptionPhoto.text.toString(),
            updateTagLoc = "" + location?.latitude + "," + location?.longitude,
            updateTagPho = photoName,
            newTagPeopleName = people.text.toString()
        ).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>, response: Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@TakePhotoActivity, "Tag uploaded successfully", Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this@TakePhotoActivity, "Failed to upload Tag", Toast.LENGTH_SHORT
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

    private fun savePhotoToLocalStorage(uri: Uri, fileName: String): File? {

        val contentResolver: ContentResolver = contentResolver

        /** open the user-picked file for reading */
        val inputStream = contentResolver.openInputStream(uri)

        /** open the output-file */
        val destinationFile = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName)
        val outputStream = FileOutputStream(destinationFile)
        println(destinationFile)

        /** copy the file contents */
        val buffer = ByteArray(1024)
        var len: Int
        if (inputStream != null) {
            while (inputStream.read(buffer).also { len = it } != -1) outputStream.write(
                buffer, 0, len
            )

        }
        inputStream?.close()
        outputStream.close()

        /** The content of the [uri] are now copied into a temporary file */
        return destinationFile
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhoto()
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }


}