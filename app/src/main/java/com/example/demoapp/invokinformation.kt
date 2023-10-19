package com.example.demoapp

//noinspection SuspiciousImport
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.example.demoapp.databinding.ActivityInvokinformationBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.core.View
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class invokinformation : AppCompatActivity() {
    private lateinit var binding: ActivityInvokinformationBinding
    private lateinit var filePickerLauncher: ActivityResultLauncher<Intent>
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        binding=ActivityInvokinformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.uplodeinv.setOnClickListener {  }


        retrieveUserNamesFromFirestore()
    }
    private fun retrieveUserNamesFromFirestore() {
        val chiedeler = binding.retalernames

        val usersCollection = firestore.collection("ruser") // Replace with your Firestore collection name

        usersCollection.get()
            .addOnSuccessListener { querySnapshot ->
                val userNameList = mutableListOf<String>()

                for (document in querySnapshot) {
                    val userData = document.data
                    val userName = userData["name"] as String // Replace "name" with the actual field name
                    userNameList.add(userName)
                }

                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, userNameList)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                chiedeler.adapter = adapter
            }
            .addOnFailureListener { e ->
                Log.e("FirestoreError", "Error adding document", e)
                Toast.makeText(this, "Failed: " + e.message, Toast.LENGTH_LONG).show()
            }


        binding.retalernames.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: android.view.View?,
                position: Int,
                id: Long
            ) {
                val selectedName = parent?.getItemAtPosition(position).toString()
                retrieveAccountDataForName(selectedName)            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle the case where nothing is selected (if needed).
            }
        }


        //////////////////////////////////////////////
        ///////////////////////////////////////////////
        //On Button click Database Ragister
        binding.submit.setOnClickListener {
            val INVO = binding.invno.text.toString()
            val Date = binding.date.text.toString()
            val RetalerName = binding.retalernames.selectedItem.toString()
            val Amount = binding.amount.text.toString()
            val QYT = binding.qyt.text.toString()

            if (INVO.isNotEmpty() && Date.isNotEmpty() && RetalerName.isNotEmpty() && Amount.isNotEmpty() && QYT.isNotEmpty()) {
                val db = Firebase.firestore

                // Define the data you want to add or check for duplicates
                val Iuser = hashMapOf(
                    "invo" to INVO,
                    "Date" to Date,
                    "Retalername" to RetalerName,
                    "Amount" to Amount,
                    "QYT" to QYT
                )

                // Create a query to check for duplicate data
                db.collection("Iuser")
                    .whereEqualTo("invo", INVO)
                    .whereEqualTo("Date", Date)
                    .whereEqualTo("Retalername", RetalerName)
                    .whereEqualTo("Amount", Amount)
                    .whereEqualTo("QYT", QYT)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        if (querySnapshot.isEmpty) {
                            // No duplicate data found, you can add it now.
                            db.collection("Iuser")
                                .add(Iuser)
                                .addOnSuccessListener { documentReference ->
                                    Toast.makeText(this, "Invoice is added to the database", Toast.LENGTH_LONG).show()
                                    val intent= Intent(this,invok::class.java)
                                    startActivity(intent)
                                    finish()

                                }
                                .addOnFailureListener { e ->
                                    Log.e("FirestoreError", "Error adding document", e)
                                    Toast.makeText(this, "Failed: " + e.message, Toast.LENGTH_LONG).show()
                                }
                        } else {
                            // Duplicate data found, show an error.
                            Toast.makeText(this, "This data is already uploaded", Toast.LENGTH_LONG).show()
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e("FirestoreError", "Error checking for duplicates", e)
                        Toast.makeText(this, "Failed: " + e.message, Toast.LENGTH_LONG).show()
                    }
            } else {
                Toast.makeText(this, "Field is Empty", Toast.LENGTH_LONG).show()
            }
            // Add this code inside the 'binding.submit' click listener in the invokinformation activity
            val intent = Intent(this, retalerDasbord::class.java)
            intent.putExtra("userName", RetalerName) // Pass the user's name
            startActivity(intent)
            finish()

        }

    }



    private fun retrieveAccountDataForName(name: String) {
        val accountsCollection = firestore.collection("Iuser") // Replace with your Firestore collection name for accounts

        accountsCollection
            .whereEqualTo("name", name)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {

                    Toast.makeText(this, "Account Balance: ", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Account data not found for selected name", Toast.LENGTH_LONG).show()
                }
            }
            .addOnFailureListener { e ->
                Log.e("FirestoreError", "Error retrieving account data", e)
                Toast.makeText(this, "Failed: " + e.message, Toast.LENGTH_LONG).show()
            }
    }




}