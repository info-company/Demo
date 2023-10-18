package com.example.demoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.demoapp.databinding.ActivityInvokBinding

class invok : AppCompatActivity() {
    private lateinit var binding: ActivityInvokBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityInvokBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.invobtn.setOnClickListener {
            val intent= Intent(this,invokinformation::class.java)
            startActivity(intent)
        }
    }
}