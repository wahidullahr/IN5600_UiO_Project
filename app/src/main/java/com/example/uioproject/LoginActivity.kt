package com.example.uioproject


import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.MessageDigest

class LoginActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var signButton: Button
    private lateinit var errorMessageTextView: TextView
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        usernameEditText = findViewById(R.id.username_edit_text)
        passwordEditText = findViewById(R.id.password_edit_text)
        loginButton = findViewById(R.id.login_button)
        signButton = findViewById(R.id.sign)
        errorMessageTextView = findViewById(R.id.error_message_text_view)

        apiService = RetrofitClient.instance

        val sharedPreferences: SharedPreferences = getSharedPreferences("login_preferences", MODE_PRIVATE)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            val passwordHash = md5Hash(password)

            Log.d("username", username)
            Log.d("password", password)
            Log.d("passwordHash", passwordHash)

            apiService.methodPostRemoteLogin(username, passwordHash).enqueue(object : Callback<Person> {
                override fun onResponse(call: Call<Person>, response: Response<Person>) {
                    if (response.isSuccessful) {
                        // Login successful, save isLoggedIn value in SharedPreferences
                        with(sharedPreferences.edit()) {
                            putBoolean("isLoggedIn", true)
                            putString("first name", response.body()?.firstName)
                            putString("last name", response.body()?.lastName)
                            putString("email", response.body()?.email)
                            putString("birth year", response.body()?.yearOfBirth)
                            putString("Living City", response.body()?.livingCity)


                            apply()
                        }
                        CurrentUser.setUserInfo(response.body()?.id.orEmpty(),response.body()?.id.orEmpty())



                        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // Login failed, show error message
                        errorMessageTextView.text = getString(R.string.login_error_message)

                        // Add log statements for debugging
                        Log.d("LoginError", "Response code: ${response.code()}")
                        Log.d("LoginError", "Response message: ${response.message()}")
                        Log.d("LoginError", "Response error body: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<Person>, t: Throwable) {
                    // Handle the failure
                    errorMessageTextView.text = getString(R.string.login_error_message)

                    // Add log statement for debugging
                    Log.d("LoginError", "Network error: ${t.localizedMessage}")
                }
            })
        }
        signButton.setOnClickListener{
             val intent = Intent(this, SignUpActivity::class.java)
                            startActivity(intent)
        }

        // ...
    }

    private fun md5Hash(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        val bytes = md.digest(input.toByteArray(Charsets.UTF_8))
        return bytes.joinToString("") { "%02x".format(it) }
    }

}

