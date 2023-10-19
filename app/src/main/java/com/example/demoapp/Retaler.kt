package com.example.demoapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.demoapp.databinding.ActivityRetalerBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Retaler : AppCompatActivity() {
private lateinit var binding: ActivityRetalerBinding
    private val firestore = FirebaseFirestore.getInstance()
    private val states = arrayOf("Select State", "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh", "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jharkhand", "Karnataka", "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal")
    private val citiesMap = mapOf(
        "Andhra Pradesh" to arrayOf("Select City", "Visakhapatnam", "Vijayawada", "Guntur"),
        "Arunachal Pradesh" to arrayOf("Select City", "Itanagar", "Naharlagun"),
        "Assam" to arrayOf("Select City", "Guwahati", "Jorhat", "Silchar"),
        "Bihar" to arrayOf("Select City", "Patna", "Gaya", "Muzaffarpur"),
        "Chhattisgarh" to arrayOf("Select City", "Raipur", "Bhilai", "Bilaspur"),
        "Goa" to arrayOf("Select City", "Panaji", "Margao", "Vasco da Gama"),
        "Gujarat" to arrayOf("Select City", "Ahmedabad", "Surat", "Vadodara"),
        "Haryana" to arrayOf("Select City", "Chandigarh", "Faridabad", "Gurgaon"),
        "Himachal Pradesh" to arrayOf("Select City", "Shimla", "Mandi", "Solan"),
        "Jharkhand" to arrayOf("Select City", "Ranchi", "Jamshedpur", "Dhanbad"),
        "Karnataka" to arrayOf("Select City", "Bangalore", "Mysore", "Hubli"),
        "Kerala" to arrayOf("Select City", "Thiruvananthapuram", "Kochi", "Kozhikode"),
        "Madhya Pradesh" to arrayOf("Select City", "Bhopal", "Indore", "Jabalpur"),
        "Maharashtra" to arrayOf("Select City", "Mumbai", "Pune", "Nagpur"),
        "Manipur" to arrayOf("Select City", "Imphal", "Thoubal", "Bishnupur"),
        "Meghalaya" to arrayOf("Select City", "Shillong", "Tura", "Jowai"),
        "Mizoram" to arrayOf("Select City", "Aizawl", "Lunglei", "Saiha"),
        "Nagaland" to arrayOf("Select City", "Kohima", "Dimapur", "Mokokchung"),
        "Odisha" to arrayOf("Select City", "Bhubaneswar", "Cuttack", "Puri"),
        "Punjab" to arrayOf("Select City", "Amritsar", "Ludhiana", "Jalandhar"),
        "Rajasthan" to arrayOf("Select City", "Jaipur", "Jodhpur", "Udaipur"),
        "Sikkim" to arrayOf("Select City", "Gangtok", "Namchi", "Mangan"),
        "Tamil Nadu" to arrayOf("Select City", "Chennai", "Coimbatore", "Madurai"),
        "Telangana" to arrayOf("Select City", "Hyderabad", "Warangal", "Nizamabad"),
        "Tripura" to arrayOf("Select City", "Agartala", "Dharmanagar", "Udaipur"),
        "Uttar Pradesh" to arrayOf("Select City", "Lucknow", "Kanpur", "Varanasi"),
        "Uttarakhand" to arrayOf("Select City", "Dehradun", "Haridwar", "Roorkee"),
        "West Bengal" to arrayOf("Select City", "Kolkata", "Asansol", "Siliguri")
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        binding = ActivityRetalerBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val spy = binding.state
        val city= binding.City
        // Create an ArrayAdapter for the state spinner
        val stateAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, states)
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spy.adapter = stateAdapter


        spy.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedState = states[position]
                if (selectedState != "Select State") {
                    val cities = citiesMap[selectedState]
                    if (cities != null) {
                        val cityAdapter = ArrayAdapter(this@Retaler, android.R.layout.simple_spinner_item, cities)
                        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        city.adapter = cityAdapter
                    } else {
                        showToast("No cities found for the selected state.")
                    }
                } else {
                    city.adapter = null
                    showToast("Please select a state.")
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        retrieveUserNamesFromFirestore()

        //On Button click Database Ragister
        binding.Ragister.setOnClickListener {
            val City = binding.City.selectedItem.toString()
            val Name = binding.Name.text.toString()
            val State = binding.state.selectedItem.toString()
            val Business = binding.Business.text.toString()
            val DealersName = binding.spinnerD.selectedItem.toString()

            if (City.isNotEmpty() && Name.isNotEmpty() && State.isNotEmpty() && Business.isNotEmpty() && DealersName.isNotEmpty()) {
                val db = Firebase.firestore

                // Create a reference to the "ruser" collection
                val collectionRef = db.collection("ruser")

                // Check if a document with the same data already exists
                collectionRef
                    .whereEqualTo("name", Name)
                    .whereEqualTo("State", State)
                    .whereEqualTo("City", City)
                    .whereEqualTo("Businessname", Business)
                    .whereEqualTo("DealersName", DealersName)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        if (querySnapshot.isEmpty) {
                            // No document with the same data exists, you can add it now.
                            val Ruser = hashMapOf(
                                "name" to Name,
                                "State" to State,
                                "City" to City,
                                "Businessname" to Business,
                                "DealersName" to DealersName
                            )

                            // Add a new document with a generated ID
                            collectionRef
                                .add(Ruser)
                                .addOnSuccessListener { documentReference ->
                                    Toast.makeText(this, "Retailer is added to the database", Toast.LENGTH_LONG).show()
                                    val intent= Intent(this, retalerDasbord::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                .addOnFailureListener { e ->
                                    Log.e("FirestoreError", "Error adding document", e)
                                    Toast.makeText(this, "Failed: " + e.message, Toast.LENGTH_LONG).show()
                                }
                        } else {
                            // A document with the same data already exists. Show an error.
                            Toast.makeText(this, "This data is already uploaded", Toast.LENGTH_LONG).show()
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e("FirestoreError", "Error checking for existing document", e)
                        Toast.makeText(this, "Failed: " + e.message, Toast.LENGTH_LONG).show()
                    }
            } else {
                Toast.makeText(this, "Fields are empty", Toast.LENGTH_LONG).show()
            }
        }



    }
    private fun retrieveUserNamesFromFirestore() {
        val chiedeler = binding.spinnerD

        val usersCollection = firestore.collection("users") // Replace with your Firestore collection name

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
                Toast.makeText(this, "Failed: " + e.message, Toast.LENGTH_LONG).show()                    }

    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}