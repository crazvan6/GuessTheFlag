package com.example.guesstheflag

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ResultActivity : AppCompatActivity() {

    private var userName: TextView? = null
    private var imgReward: ImageView? = null
    private var txtScore: TextView? = null
    private var txtGreating: TextView? = null
    private var btnPlay: TextView? = null
    private var btnProfile: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        userName = findViewById(R.id.userName)
        imgReward  = findViewById(R.id.imgReward)
        txtScore = findViewById(R.id.txtScore)
        txtGreating = findViewById(R.id.txtGreating)
        btnPlay = findViewById(R.id.btnReplay)
        btnProfile = findViewById(R.id.btnProfile)

        setName()
        txtScore?.text = "${UserData.getLastScore()} / 10"

        when(UserData.getLastScore()){
            in 0..1 -> {
                imgReward?.setBackgroundResource(R.drawable.ic_candy)
            }
            in 2..5 ->{
                imgReward?.setBackgroundResource(R.drawable.ic_bronze_medal)
            }
            in 6..7 ->{
                imgReward?.setBackgroundResource(R.drawable.ic_silver_medal)
            }
            in 8..9->{
                imgReward?.setBackgroundResource(R.drawable.ic_gold_medal)
            }
            10 ->{
                imgReward?.setBackgroundResource(R.drawable.ic_trophy)
            }
        }

        btnPlay?.setOnClickListener{
            startActivity(Intent(this, CountDownActivity::class.java))
            overridePendingTransition(R.animator.slide_in_right, R.animator.slide_in_left)
            finish()
        }
        btnProfile?.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
            overridePendingTransition(R.animator.slide_in_right, R.animator.slide_in_left)
            finish()
        }

    }

    private fun setName(){
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val firestore = FirebaseFirestore.getInstance()
        val userDocRef = firestore.collection("users").document(userId!!)

        userDocRef.get()
            .addOnSuccessListener { documentSnapShot ->
                if(documentSnapShot.exists()){
                    val data = documentSnapShot.data

                    if(data != null && data.contains("name")){

                        val name = data["name"] as String
                        userName?.text = name
                    }
                }
            }
    }
}