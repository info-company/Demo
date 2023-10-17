package com.example.demoapp

//noinspection SuspiciousImport
import android.R
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.demoapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth= FirebaseAuth.getInstance()


        val Spy = binding.spinner
        // Create an ArrayAdapter for the spinner
        val genderOptions = arrayOf("Select Gender", "Male", "Female")
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, genderOptions)
        Spy.adapter = adapter

    binding.textView.setOnClickListener {
    val intent = Intent(this,Signup::class.java)
    startActivity(intent)
        finish()
}
        binding.button.setOnClickListener {
            val Email = binding.email.text.toString()
            val password =binding.pass.text.toString()
            val spiner = binding.spinner.selectedItem.toString()
            if(Email.isNotEmpty()&&password.isNotEmpty() && spiner.isNotEmpty()){

                firebaseAuth.signInWithEmailAndPassword(Email,password).addOnCompleteListener {
                    if (it.isSuccessful){

                        // Check the selected gender
                        when (Spy.selectedItem.toString()) {
                            "Male" -> {
                                val intent = Intent(this,Retaler::class.java)
                                startActivity(intent)
                                finish()
                            }
                            "Female" -> {
                                val intent = Intent(this,Dealer::class.java)
                                startActivity(intent)
                                finish()

                            }
                            else -> {
                                Toast.makeText(this,"You haven't choise spinner",Toast.LENGTH_LONG).show()
                            }
                        }
                    }else{
                        Toast.makeText(this,it.exception.toString(),Toast.LENGTH_LONG).show()

                    }
                }

            }else{
                Toast.makeText(this,"Fields Must be fill !!",Toast.LENGTH_LONG).show()
            }



    }
}}