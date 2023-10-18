package com.example.demoapp

//noinspection SuspiciousImport
import android.R
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.demoapp.databinding.ActivityInvokinformationBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
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


        // Initialize the ActivityResultLauncher
        filePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val selectedFileUri: Uri? = data?.data

                // Check if the selected file has the allowed MIME type (DOCX or PDF)
                if (selectedFileUri != null && isFileTypeAllowed(selectedFileUri)) {
                    // Handle the selected file here, e.g., upload it to Firebase Firestore
                    uploadFileToFirestore(selectedFileUri)
                }
            }
        }

        retrieveUserNamesFromFirestore()


        binding.uplodeinv.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"  // Allow all file types initially
            intent.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("application/pdf", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
            filePickerLauncher.launch(intent)

        }
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



        //////////////////////////////////////////////
        ///////////////////////////////////////////////
        //On Button click Database Ragister
        binding.submit.setOnClickListener {
            val INVO = binding.invno.text.toString()
            val Date = binding.date.text.toString()
            val RetalerName =binding.retalernames.selectedItem.toString()
            val Amount = binding.amount.text.toString()
            val QYT = binding.qyt.text.toString()
            //val pdf = binding.invocpdf.text.tostring

            if(INVO.isNotEmpty()&&Date.isNotEmpty()&&RetalerName.isNotEmpty()&&Amount.isNotEmpty()&&QYT.isNotEmpty()){
                val db = Firebase.firestore

                val Iuser = hashMapOf(
                    "invo" to binding.invno.text.toString(),
                    "Date" to binding.date.text.toString(),
                    "Retalername" to binding.retalernames.selectedItem.toString(),
                    "Amount" to binding.amount.text.toString(),
                    "QYT" to binding.qyt.text.toString()
                )

                // Add a new document with a generated ID
                db.collection("Iuser")
                    .add(Iuser)
                    .addOnSuccessListener { documentReference ->
                        Toast.makeText(this,"Invo is added to database",Toast.LENGTH_LONG).show()
                    }
                    .addOnFailureListener { e ->
                        Log.e("FirestoreError", "Error adding document", e)
                        Toast.makeText(this, "Failed: " + e.message, Toast.LENGTH_LONG).show()                    }

            }else{
                Toast.makeText(this,"Field is Empty",Toast.LENGTH_LONG).show()
            }
        }

    }


    private fun isFileTypeAllowed(fileUri: Uri): Boolean {
        val contentResolver: ContentResolver = contentResolver
        val mime = contentResolver.getType(fileUri)
        return mime == "application/pdf" || mime == "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    }

    private fun uploadFileToFirestore(fileUri: Uri) {
        // Implement Firebase Firestore file upload logic here
        // You'll need to initialize Firebase Firestore and upload the file to a specific collection.
        // Refer to the Firebase Firestore documentation for more details.
    }




}