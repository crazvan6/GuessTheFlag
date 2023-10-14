package com.example.guesstheflag

import android.animation.Animator
import android.animation.AnimatorInflater
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class RewardsActivity : AppCompatActivity() {

    private var imgCandy: ImageView? = null
    private var imgBronzeMedal: ImageView? = null
    private var imgSilverMedal: ImageView? = null
    private var imgGoldMedal: ImageView? = null
    private var imgTrophy: ImageView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_rewards2)
        loadRewards()
        imgCandy = findViewById(R.id.imgCandy)
        imgBronzeMedal = findViewById(R.id.imgBronzeMedal)
        imgSilverMedal = findViewById(R.id.imgSilverMedal)
        imgGoldMedal = findViewById(R.id.imgGoldMedal)
        imgTrophy = findViewById(R.id.imgTrophy)


        val shrinkInAnimation = AnimationUtils.loadAnimation(this, R.animator.shrink_in)
        val shrinkOutAnimation = AnimationUtils.loadAnimation(this, R.animator.shrink_out)

        android.os.Handler().postDelayed({
            startActivity(Intent(this, ResultActivity::class.java))
            overridePendingTransition(R.animator.slide_in_left, R.animator.slide_in_right)
            finish()
        }, 3500)

        when(UserData.getLastScore()){
            in 0..1 -> {
                imgCandy?.setImageResource(R.drawable.ic_question_mark)
                imgCandy?.startAnimation(shrinkInAnimation)
                android.os.Handler().postDelayed({
                    imgCandy?.setImageDrawable(null)
                    imgCandy?.setImageResource(R.drawable.ic_candy)
                    imgCandy?.startAnimation(shrinkOutAnimation)
                }, 1500)

            }
            in 2..5 ->{
                imgBronzeMedal?.setImageResource(R.drawable.ic_question_mark)
                imgBronzeMedal?.startAnimation(shrinkInAnimation)
                android.os.Handler().postDelayed({
                    imgBronzeMedal?.setImageDrawable(null)
                    imgBronzeMedal?.setImageResource(R.drawable.ic_bronze_medal)
                    imgBronzeMedal?.startAnimation(shrinkOutAnimation)
                }, 1500)
            }
            in 6..7 ->{
                imgSilverMedal?.setImageResource(R.drawable.ic_question_mark)
                imgSilverMedal?.startAnimation(shrinkInAnimation)
                android.os.Handler().postDelayed({
                    imgSilverMedal?.setImageDrawable(null)
                    imgSilverMedal?.setImageResource(R.drawable.ic_silver_medal)
                    imgSilverMedal?.startAnimation(shrinkOutAnimation)
                }, 1500)
            }
            in 8..9->{
                imgGoldMedal?.setImageResource(R.drawable.ic_question_mark)
                imgGoldMedal?.startAnimation(shrinkInAnimation)
                android.os.Handler().postDelayed({
                    imgGoldMedal?.setImageDrawable(null)
                    imgGoldMedal?.setImageResource(R.drawable.ic_gold_medal)
                    imgGoldMedal?.startAnimation(shrinkOutAnimation)
                }, 1500)
            }
            10 ->{
                imgTrophy?.setImageResource(R.drawable.ic_question_mark)
                imgTrophy?.startAnimation(shrinkInAnimation)
                android.os.Handler().postDelayed({
                    imgTrophy?.setImageDrawable(null)
                    imgTrophy?.setImageResource(R.drawable.ic_trophy)
                    imgTrophy?.startAnimation(shrinkOutAnimation)
                }, 1500)
            }
        }

    }

    private fun loadRewards(){

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val firestore = FirebaseFirestore.getInstance()
        val userDocRef = firestore.collection("users").document(userId!!)

        userDocRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {

                    val data = documentSnapshot.data
                    if (data != null){
                            val candyAchieved = data["candyAchieved"] as Boolean
                            val bronzeAchieved = data["bronzeAchieved"] as Boolean
                            val silverAchieved = data["silverAchieved"] as Boolean
                            val goldAchieved = data["goldAchieved"] as Boolean
                            val trophyAchieved = data["trophyAchieved"] as Boolean

                            if(candyAchieved) {

                                imgCandy?.setImageDrawable(null)
                                imgCandy?.setImageResource(R.drawable.ic_candy)
                            }
                            else{
                                imgCandy?.setImageDrawable(null)
                                imgCandy?.setImageResource(R.drawable.ic_question_mark)
                            }
                            if(bronzeAchieved) {
                                imgBronzeMedal?.setImageDrawable(null)
                                imgBronzeMedal?.setImageResource(R.drawable.ic_bronze_medal)
                            }
                            else{
                                imgBronzeMedal?.setImageDrawable(null)
                                imgBronzeMedal?.setImageResource(R.drawable.ic_question_mark)
                            }
                            if(silverAchieved) {
                                imgSilverMedal?.setImageDrawable(null)
                                imgSilverMedal?.setImageResource(R.drawable.ic_silver_medal)
                            }
                            else{
                                imgSilverMedal?.setImageDrawable(null)
                                imgSilverMedal?.setImageResource(R.drawable.ic_question_mark)
                            }
                            if(goldAchieved) {
                                imgGoldMedal?.setImageDrawable(null)
                                imgGoldMedal?.setImageResource(R.drawable.ic_gold_medal)
                            }
                            else{
                                imgSilverMedal?.setImageDrawable(null)
                                imgSilverMedal?.setImageResource(R.drawable.ic_question_mark)
                            }
                            if(trophyAchieved) {
                                imgTrophy?.setImageDrawable(null)
                                imgTrophy?.setImageResource(R.drawable.ic_trophy)
                            }
                            else{
                                imgSilverMedal?.setImageDrawable(null)
                                imgSilverMedal?.setImageResource(R.drawable.ic_question_mark)
                            }

                    }
                }
            }
            .addOnFailureListener { exception ->
                showToast(exception.toString())
            }
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
