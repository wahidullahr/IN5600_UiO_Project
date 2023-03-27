package com.example.uioproject

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
    }

    private fun setupRecyclerView(photosData: List<String>) {
        // Initialize the custom RecyclerView.Adapter with the photos data
        val photosAdapter = PhotosAdapter(photosData)

        // Set the adapter and layout manager to the RecyclerView
        photosRecyclerView.adapter = photosAdapter
        photosRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun loadPhotosData() {
        val sharedPreferences = getSharedPreferences("login_preferences", Context.MODE_PRIVATE)
        val apiService = RetrofitClient.instance
        val username = sharedPreferences.getString("name", "").orEmpty()


        apiService.getPhotos(username).enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                if (response.isSuccessful) {
                    val photosData = response.body() ?: listOf()
                    setupRecyclerView(photosData)
                } else {
                    println(response.errorBody()?.string())
                    // Handle the error
                }
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                // Handle the failure
            }
        })
    }
}
