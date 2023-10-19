package com.example.demoapp



import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class invok : AppCompatActivity() {
    private lateinit var recycler: RecyclerView
    private lateinit var userlist: ArrayList<User>
    private val firestore = FirebaseFirestore.getInstance()
    var loadingPB: ProgressBar? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invok)

        recycler = findViewById(R.id.recycler)
        recycler.layoutManager = LinearLayoutManager(this)
        userlist = ArrayList()

        val db = Firebase.firestore
        val progressBar = findViewById<ProgressBar>(R.id.idProgressBar)

        // Initially, show the ProgressBar and hide the RecyclerView
        progressBar.visibility = View.VISIBLE
        recycler.visibility = View.GONE

        val query = db.collection("Iuser")

        // Attach a snapshot listener to the query
        query.addSnapshotListener { querySnapshot, error ->
            if (error != null) {
                // Handle the error gracefully and add retry logic
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_LONG).show()
                return@addSnapshotListener
            }

            userlist.clear() // Clear the existing list to avoid duplicates

            if (querySnapshot != null) {
                for (document in querySnapshot.documents) {
                    val user = document.toObject(User::class.java)
                    if (user != null) {
                        userlist.add(user)
                    }
                }

                val adapter = MyAdapter(userlist) // Replace "MyAdapter" with your actual adapter class.
                recycler.adapter = adapter

                // Hide the ProgressBar and show the RecyclerView
                progressBar.visibility = View.GONE
                recycler.visibility = View.VISIBLE
            } else {
                // Hide the ProgressBar and show a message
                progressBar.visibility = View.GONE
                Toast.makeText(this, "No data found", Toast.LENGTH_LONG).show()
            }
        }

        val btn = findViewById<Button>(R.id.invobtn)
        btn.setOnClickListener {
            val intent = Intent(this, invokinformation::class.java)
            startActivity(intent)
        }
    }

}
