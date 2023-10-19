package com.example.demoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class retalerDasbord : AppCompatActivity() {
    private lateinit var rrecyclerView: RecyclerView
    private lateinit var userlist: ArrayList<User>
    private val firestore = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retaler_dasbord)
        rrecyclerView = findViewById(R.id.rrecyclerView)
        rrecyclerView.layoutManager = LinearLayoutManager(this)
        userlist = ArrayList()

        val progressBar = findViewById<ProgressBar>(R.id.progressBarr)
        progressBar.visibility = View.VISIBLE
        rrecyclerView.visibility = View.GONE

        val userName =intent.getStringExtra("username") // Get the user's name from the intent

        if (userName != null) {
            val db = Firebase.firestore
            val query = db.collection("Iuser").whereEqualTo("Retalername", userName)

            query.addSnapshotListener { querySnapshot, error ->
                if (error != null) {
                    Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_LONG).show()
                    return@addSnapshotListener
                }

                userlist.clear()

                if (querySnapshot != null) {
                    for (document in querySnapshot.documents) {
                        val user = document.toObject(User::class.java)
                        if (user != null) {
                            userlist.add(user)
                        }
                    }

                    val adapter = MyAdapter(userlist)
                    rrecyclerView.adapter = adapter

                    progressBar.visibility = View.GONE
                    rrecyclerView.visibility = View.VISIBLE
                } else {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, "No data found", Toast.LENGTH_LONG).show()
                }
            }
        } else {
            // Handle the case where the user's name was not passed in the intent
            Toast.makeText(this, "User's name not provided.", Toast.LENGTH_LONG).show()
        }
    }


}