package com.example.demoapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.demoapp.databinding.ActivityInvokBinding
import com.example.demoapp.databinding.ActivityUsernameBinding

class UsernameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUsernameBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityUsernameBinding.inflate(layoutInflater)
        setContentView(binding.root)

       binding. submitButton.setOnClickListener {
            val enteredUsername = binding.usernameEditText.text.toString()

            // Check if the entered username is not empty
            if (enteredUsername.isNotEmpty()) {
                // You can save the username to a shared preference, a database, or any other storage mechanism.
                // For simplicity, we'll pass it to a new activity.
                val intent = Intent(this, retalerDasbord::class.java)
                intent.putExtra("username", enteredUsername)
                startActivity(intent)
            } else {
              binding.  usernameEditText.error = "Please enter a username"
            }
        }
    }
}
