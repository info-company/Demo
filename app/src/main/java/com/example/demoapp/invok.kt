package com.example.demoapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.demoapp.databinding.ActivityInvokBinding
import com.google.firebase.firestore.FirebaseFirestore

class invok : AppCompatActivity() {

    private val firestore = FirebaseFirestore.getInstance()
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