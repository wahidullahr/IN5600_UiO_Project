package com.example.uioproject

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserInfoActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var nameTextView: TextView
    private lateinit var cityTextView: TextView
    private lateinit var birthYearTextView: TextView
    private lateinit var changePasswordButton: Button
    private lateinit var logoutButton: Button
    private lateinit var takePhotoButton: Button
    private lateinit var viewPhotoBtn: Button

    private val apiService = RetrofitClient.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)

        sharedPreferences = getSharedPreferences("login_preferences", Context.MODE_PRIVATE)

        changePasswordButton = findViewById(R.id.change_password_button)
        logoutButton = findViewById(R.id.logout_button)

        getUserInfo()

        changePasswordButton.setOnClickListener {
            changePassword()
        }

        logoutButton.setOnClickListener {
            logout()
        }
        takePhotoButton = findViewById(R.id.take_photo_button)

        takePhotoButton.setOnClickListener {
            val intent = Intent(this, TakePhotoActivity::class.java)
            startActivity(intent)
        }
        viewPhotoBtn = findViewById(R.id.show_photos_button)

        viewPhotoBtn.setOnClickListener {
            val intent = Intent(this, DisplayPhotosActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getUserInfo() {
        val username = sharedPreferences.getString("name", "")
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        println(username)
       println(sharedPreferences.getString("Living City",""))
        println(sharedPreferences.getString("Living City",""))
        println(sharedPreferences.getString("Living City",""))
        println(sharedPreferences.getString("Living City",""))
        println(sharedPreferences.getString("Living City",""))

    }

    private fun changePassword() {
        // Navigate to ChangePasswordActivity
        val intent = Intent(this, ChangePasswordActivity::class.java)
        startActivity(intent)
    }

    private fun logout() {
        val editor = sharedPreferences.edit()
        editor.remove("username")
        editor.apply()

        // Navigate back to LoginActivity
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}
