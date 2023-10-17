package com.example.demoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.demoapp.databinding.ActivityLoginBinding
import com.example.demoapp.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth

class Signup : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth=FirebaseAuth.getInstance()

        binding.submitbtn.setOnClickListener {
            val email=binding.Email.text.toString()
            val pass = binding.Password.text.toString()
            val passwordcobnfr = binding.confirmpass.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && passwordcobnfr.isNotEmpty()){
                if(pass==passwordcobnfr){
                    firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener {
                        if (it.isSuccessful){
                            val intent=Intent(this,Login::class.java)
                            startActivity(intent)
                            finish()
                        }else{
                            Toast.makeText(this,it.exception.toString(),Toast.LENGTH_LONG).show()

                        }
                    }
                }else{
                    Toast.makeText(this,"Password is not matching",Toast.LENGTH_LONG).show()

                }

            }else{
                Toast.makeText(this,"field in unfield",Toast.LENGTH_LONG).show()
            }

        }
    }
}