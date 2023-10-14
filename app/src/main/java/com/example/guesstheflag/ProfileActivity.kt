package com.example.guesstheflag

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest
import android.graphics.BitmapFactory
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.example.guesstheflag.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import android.net.Uri
import android.provider.CalendarContract.CalendarCache.URI
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.squareup.picasso.Picasso
import java.net.URL


class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var auth: FirebaseAuth

    private val REQUEST_CODE_STORAGE_PERMISSION = 123
    private val REQUEST_CODE_IMAGE_PICK = 124

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        loadProfileImage()
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        val user = Firebase.auth.currentUser
        if (user == null) {
            startActivity(Intent(this, OptionsActivity::class.java))
            overridePendingTransition(R.animator.slide_in_left, R.animator.slide_in_right)
            finish()
        }

        binding.btnIcon.setOnClickListener {

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                pickImageFromGallery()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_CODE_STORAGE_PERMISSION
                )
            }

        }

        binding.btnPlay.setOnClickListener {
            startActivity(Intent(this, CountDownActivity::class.java))
            overridePendingTransition(R.animator.slide_in_left, R.animator.slide_in_right)
            finish()
        }

        binding.btnSignOut.setOnClickListener {
            Firebase.auth.signOut()

            android.os.Handler().postDelayed({
                startActivity(Intent(this, OptionsActivity::class.java))
                overridePendingTransition(R.animator.slide_in_left, R.animator.slide_in_right)
                finish()
            }, 500)
        }
    }


    private fun pickImageFromGallery() {
        val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
        galleryIntent.type = "image/*"
        startActivityForResult(galleryIntent, REQUEST_CODE_IMAGE_PICK)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_IMAGE_PICK && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri = data.data
            if (selectedImageUri != null)
                binding.userIcon.setImageURI(selectedImageUri)
                saveImageUrlToDatabase(selectedImageUri)
            }
        }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun saveImageUrlToDatabase(imageUri: Uri?) {

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val firestore = FirebaseFirestore.getInstance()
        val userDocRef = firestore.collection("users").document(userId!!)

        userDocRef.update("profileImg", imageUri?.toString())
            .addOnFailureListener { exception ->
                Log.e("E:",exception.toString())
            }
    }

    private fun loadProfileImage() {

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val firestore = FirebaseFirestore.getInstance()
        val userDocRef = firestore.collection("users").document(userId!!)

        userDocRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {

                    val data = documentSnapshot.data
                    if (data != null){
                        if(data.contains("name") && data.contains("points")) {
                            val name = data["name"] as String
                            val points = data["points"] as Long

                            binding.userName.text = "Name: $name"
                            binding.userPoints.text = "Points: $points"
                        }
                        if(data.contains("profileImg")){
                            val image = data["profileImg"] as String

                            val img = image.toUri()
                            Glide.with(this)
                                .load(img)
                                .into(binding.userIcon)
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("E:", exception.toString())
            }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImageFromGallery()
            } else {
                showToast("Permission dennied!")
            }
        }
    }

}
