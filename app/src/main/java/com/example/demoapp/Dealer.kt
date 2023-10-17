package com.example.demoapp

import android.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isNotEmpty
import com.example.demoapp.databinding.ActivityDealerBinding
import com.example.demoapp.databinding.ActivitySignupBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Dealer : AppCompatActivity() {
    private lateinit var binding: ActivityDealerBinding
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
        binding=ActivityDealerBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val highe = binding.state
        val locals= binding.City
        // Create an ArrayAdapter for the state spinner
        val stateAdapter = ArrayAdapter(this, R.layout.simple_spinner_item, states)
        stateAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        highe.adapter = stateAdapter


        highe.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedState = states[position]
                if (selectedState != "Select State") {
                    val cities = citiesMap[selectedState]
                    if (cities != null) {
                        val cityAdapter = ArrayAdapter(this@Dealer, R.layout.simple_spinner_item, cities)
                        cityAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
                        locals.adapter = cityAdapter
                    } else {
                        showToast("No cities found for the selected state.")
                    }
                } else {
                    locals.adapter = null
                    showToast("Please select a state.")
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }


        //on btn click
        binding.Ragister.setOnClickListener {
            val State=binding.state.selectedItem.toString()
            val City=binding.City.selectedItem.toString()
            val Name =binding.Name.text.toString()
            val Businessname =binding.Business.text.toString()


            if(State.isNotEmpty()&& City.isNotEmpty()&& Name.isNotEmpty() && Businessname.isNotEmpty()){
                val db = Firebase.firestore

                val user = hashMapOf(
                    "name" to binding.Name.text.toString(),
                    "State" to binding.state.selectedItem.toString(),
                    "City" to binding.City.selectedItem.toString(),
                    "Businessname" to binding.Business.text.toString()
                )

// Add a new document with a generated ID
                db.collection("users")
                    .add(user)
                    .addOnSuccessListener { documentReference ->
Toast.makeText(this,"Done you are ragisted",Toast.LENGTH_LONG).show()
                    }
                    .addOnFailureListener { e ->
                        Log.e("FirestoreError", "Error adding document", e)
                        Toast.makeText(this, "Failed: " + e.message, Toast.LENGTH_LONG).show()                    }

            }else{
                Toast.makeText(this,"Filled the full form !!",Toast.LENGTH_LONG).show()
            }
        }
    }



    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}