package com.example.uioproject


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.util.Base64
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DisplayPhotosActivity : AppCompatActivity() {

    private lateinit var photosRecyclerView: RecyclerView
    private lateinit var username: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_photos)

        photosRecyclerView = findViewById(R.id.photos_recycler_view)

        loadPhotosData()
        getTagsList()
    }

    private fun setupRecyclerView(photosData: List<Bitmap>,tags: Tags) {
        // Initialize the custom RecyclerView.Adapter with the photos data
        val photosAdapter = PhotosAdapter(photosData,tags)

        // Set the adapter and layout manager to the RecyclerView
        photosRecyclerView.adapter = photosAdapter
        photosRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun loadPhotosData() {
        val destinationFile = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val pathList = destinationFile?.listFiles()?.map { it.absolutePath }


    }

    private fun getTagsList() {
        val userId = CurrentUser.userId ?: return

        val apiService = RetrofitClient.instance

        apiService.getMethodMyTags(
            id = userId
        )
            .enqueue(object : Callback<Tags> {
                override fun onResponse(
                    call: Call<Tags>, response: Response<Tags>
                ) {
                    for (index in 0..4) {
                        println("Response" + response.body()?.getTagDes(index))
                        println("Response" + response.body()?.getTagPeopleName(index))
                    }
                    loadAllPhoto(tags = response.body() ?: return)
                }

                override fun onFailure(call: Call<Tags>, t: Throwable) {
                    Toast.makeText(
                        this@DisplayPhotosActivity, "Error: ${t.message}", Toast.LENGTH_SHORT
                    ).show()
                    t.printStackTrace()
                }
            })


    }

    private fun loadAllPhoto(tags: Tags) {

        val apiService = RetrofitClient.instance
        lifecycleScope.launchWhenCreated {
            try {
                val responsesList = mutableListOf<String?>()


                repeat(5){
                    val response = apiService.getPhoto(fileName = tags.getTagPhoto(it)).body()?.string()
                    responsesList.add(response)

                }
                val bitmapList = responsesList.mapNotNull {
                    val imageBytes = Base64.decode(it.orEmpty(), Base64.DEFAULT)
                    BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                }



                setupRecyclerView(bitmapList,tags)

            } catch (e: Exception) {

                Toast.makeText(
                    this@DisplayPhotosActivity, "Error: ${e.message}", Toast.LENGTH_SHORT
                ).show()
                e.printStackTrace()
            }

        }
    }
}
