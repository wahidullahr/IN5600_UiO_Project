package com.example.uioproject

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.MessageDigest

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var oldPasswordEditText: EditText
    private lateinit var newPasswordEditText: EditText
    private lateinit var changePasswordButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        oldPasswordEditText = findViewById(R.id.old_password_edit_text)
        newPasswordEditText = findViewById(R.id.new_password_edit_text)
        changePasswordButton = findViewById(R.id.change_password_button)

        changePasswordButton.setOnClickListener {
            val oldPassword = oldPasswordEditText.text.toString()
            val newPassword = newPasswordEditText.text.toString()

            // Call the changePassword function with the user's input.
            changePassword(oldPassword, newPassword)
        }
    }

    private fun changePassword(oldPassword: String, newPassword: String) {
        val sharedPreferences = getSharedPreferences("login_preferences", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "")

        if (username != null) {
            val apiService = RetrofitClient.instance

            // Hash the old and new passwords
            val oldPasswordHash = md5Hash(oldPassword)
            val newPasswordHash = md5Hash(newPassword)

            apiService.changePassword(username, oldPasswordHash, newPasswordHash).enqueue(object :
                Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        // Handle a successful response, e.g., show a success message or navigate back to UserInfoActivity
                        Toast.makeText(this@ChangePasswordActivity, "Password changed successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        // Handle an error response, e.g., show an error message
                        Toast.makeText(this@ChangePasswordActivity, "Error changing password", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    // Handle the failure, e.g., show an error message
                    Toast.makeText(this@ChangePasswordActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this@ChangePasswordActivity, "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }

    private fun md5Hash(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        val bytes = md.digest(input.toByteArray(Charsets.UTF_8))
        return bytes.joinToString("") { "%02x".format(it) }
    }

}
