package com.example.uioproject

import android.content.Context
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

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
        val destinationFile = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val pathList = destinationFile?.listFiles()?.map { it.absolutePath }
        setupRecyclerView(pathList.orEmpty())

    }
}
