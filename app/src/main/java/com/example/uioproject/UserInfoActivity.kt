package com.example.uioproject



import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class UserInfoActivity: AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)


        val firstNameEditText = findViewById<EditText>(R.id.firstname_txt)
        val lastNameEditText = findViewById<EditText>(R.id.lastname_txt)
        val emailEditText = findViewById<EditText>(R.id.email_txt)
        val cityEditText = findViewById<EditText>(R.id.city_txt)
        val birthEditText = findViewById<EditText>(R.id.birth_txt)
        val updateButton: Button = findViewById(R.id.Button)


        val sharedPreferences = getSharedPreferences("login_preferences", MODE_PRIVATE)
        val firstName = sharedPreferences.getString("first name", "")
        val lastName = sharedPreferences.getString("last name", "")
        val email = sharedPreferences.getString("email", "")
        val city = sharedPreferences.getString("Living City", "")
        val birthYear = sharedPreferences.getString("birth year", "")


        firstNameEditText.setText(firstName)
        lastNameEditText.setText(lastName)
        emailEditText.setText(email)
        cityEditText.setText(city)
        birthEditText.setText(birthYear)


        updateButton.setOnClickListener {
            val sharedPreferences = getSharedPreferences("login_preferences", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("first name", firstNameEditText.text.toString())
            editor.putString("last name", lastNameEditText.text.toString())
            editor.putString("email", emailEditText.text.toString())
            editor.putString("Living City", cityEditText.text.toString())
            editor.putString("birth year", birthEditText.text.toString())
            editor.apply()

            Toast.makeText(this@UserInfoActivity, "User info updated", Toast.LENGTH_SHORT).show()

        }


    }
}