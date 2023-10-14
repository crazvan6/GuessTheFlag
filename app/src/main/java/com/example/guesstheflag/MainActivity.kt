package com.example.guesstheflag

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val user = Firebase.auth.currentUser
        if (user != null) {
            android.os.Handler().postDelayed({
                startActivity(Intent(this, ProfileActivity::class.java))
                overridePendingTransition(R.animator.slide_in_left, R.animator.slide_in_right)
                finish()
            }, 2000)
        } else {
            android.os.Handler().postDelayed({
                startActivity(Intent(this, OptionsActivity::class.java))
                overridePendingTransition(R.animator.slide_in_left, R.animator.slide_in_right)
                finish()
            }, 2000)
        }
    }
}