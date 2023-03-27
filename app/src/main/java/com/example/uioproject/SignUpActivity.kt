package com.example.uioproject

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SignUpActivity : AppCompatActivity() {

//    private lateinit var firstNameEditText: EditText
//    private lateinit var lastNameEditText: EditText
//    private lateinit var emailEditText: EditText
//    private lateinit var passwordEditText: EditText
//    private lateinit var confirmPasswordEditText: EditText
//    private lateinit var yearOfBirthEditText: EditText
//    private lateinit var signUpButton: Button
//    private lateinit var apiService: ApiService
//
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_signup)

//        firstNameEditText = findViewById(R.id.firstName)
//        lastNameEditText = findViewById(R.id.lastName)
//        emailEditText = findViewById(R.id.email_signup)
//        passwordEditText = findViewById(R.id.pass_signup)
//        confirmPasswordEditText = findViewById(R.id.confirm_pass_signup)
//        yearOfBirthEditText = findViewById(R.id.yr_birth)
//        signUpButton = findViewById(R.id.signup_btn)
//
//        apiService = RetrofitClient.instance
//
//        signUpButton.setOnClickListener {
//            val firstName = firstNameEditText.text.toString()
//            val lastName = lastNameEditText.text.toString()
//            val email = emailEditText.text.toString()
//            val password = passwordEditText.text.toString()
//            val confirmPassword = confirmPasswordEditText.text.toString()
//            val yearOfBirth = yearOfBirthEditText.text.toString()
//
//            // Validate user input
//            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || yearOfBirth.isEmpty()) {
//                Toast.makeText(this, "Please fill out all fields.", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//            if (password != confirmPassword) {
//                Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//
//            // Create a new user object
//            val user = User(firstName, lastName, email, password, yearOfBirth)
//
//            // Call the sign up API endpoint
//            apiService.methodPostRemoteSignUp(user).enqueue(object : Callback<Person> {
//                override fun onResponse(call: Call<Person>, response: Response<Person>) {
//                    if (response.isSuccessful) {
//                        // Sign up successful, show success message
//                        Toast.makeText(this@SignUpActivity, "Sign up successful.", Toast.LENGTH_SHORT).show()
//
//                        // Redirect to the login page
//                        val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
//                        startActivity(intent)
//                        finish()
//                    } else {
//                        // Sign up failed, show error message
//                        Toast.makeText(this@SignUpActivity, "Sign up failed.", Toast.LENGTH_SHORT).show()
//
//                        // Add log statements for debugging
//                        Log.d("SignUpError", "Response code: ${response.code()}")
//                        Log.d("SignUpError", "Response message: ${response.message()}")
//                        Log.d("SignUpError", "Response error body: ${response.errorBody()?.string()}")
//                    }
//                }
//
//                override fun onFailure(call: Call<Person>, t: Throwable) {
//                    // Handle the failure
//                    Toast.makeText(this@SignUpActivity, "Sign up failed.", Toast.LENGTH_SHORT).show()
//
//                    // Add log statement for debugging
//                    Log.d("SignUpError", "Network error: ${t.localizedMessage}")
//                }
//            })
//        }
  }
//
}
