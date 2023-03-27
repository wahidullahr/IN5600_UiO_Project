package com.example.uioproject

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // Initialize the required resources (e.g., load SharedPreferences)
        val sharedPreferences = getSharedPreferences("Shared", MODE_PRIVATE)

        // Check if there is an existing user login
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        // Set a delay before navigating to the next activity
        Handler().postDelayed({
            val intent: Intent

            // If the user is already logged in, navigate to MainActivity
            if (isLoggedIn) {
                intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
            } else {
                // If the user is not logged in, navigate to LoginActivity
                intent = Intent(this@SplashScreenActivity, LoginActivity::class.java)
            }
            startActivity(intent)
            finish()
        }, 10000) // 10000 ms (10 seconds) delay before navigating to the next activity
    }
}
