package com.example.guesstheflag

import android.util.Log
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

object UserData {
    private var userScores: ArrayList<Int?> = ArrayList()
    private var userName: String = ""

    private var candyAchieved: Boolean = false
    private var bronzeAchieved: Boolean = false
    private var silverAchieved: Boolean = false
    private var goldAchieved: Boolean = false
    private var trophyAchieved: Boolean = false


    fun getLastScore(): Int? {
        return userScores[userScores.size - 1]
    }

    fun getUserName(): String{
        return userName
    }

    fun getCustomAchievement(score: Int?, callback: (Boolean) -> Unit) {

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val firestore = FirebaseFirestore.getInstance()
        val userDocRef = firestore.collection("users").document(userId!!)

        userDocRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val data = documentSnapshot.data
                    if (data != null) {
                    
                        val candyAchieved = data["candyAchieved"] as Boolean
                        val bronzeAchieved = data["bronzeAchieved"] as Boolean
                        val silverAchieved = data["silverAchieved"] as Boolean
                        val goldAchieved = data["goldAchieved"] as Boolean
                        val trophyAchieved = data["trophyAchieved"] as Boolean

                        // Verificați realizările în funcție de scor
                        when (score) {
                            in 0..1 -> callback(candyAchieved)
                            in 2..5 -> callback(bronzeAchieved)
                            in 6..7 -> callback(silverAchieved)
                            in 8..9 -> callback(goldAchieved)
                            10 -> callback(trophyAchieved)
                            else -> callback(true) 
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("E:", exception.toString())
                callback(true) 
            }
    }

    fun setCustomAchievement(score: Int?){

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val firestore = FirebaseFirestore.getInstance()
        val userDocRef = firestore.collection("users").document(userId!!)

        when(score){
            in 0..1 -> {
                userDocRef.update("candyAchieved", true)
            }
            in 2..5 ->{
                userDocRef.update("bronzeAchieved", true)
            }
            in 6..7 ->{
                userDocRef.update("silverAchieved", true)
            }
            in 8..9->{
                userDocRef.update("goldAchieved", true)
            }
            10 ->{
                userDocRef.update("trophyAchieved", true)
            }
        }
    }

    fun setUserName(name: String){
        userName = name
    }

    fun addScore(score: Int?){
        userScores.add(score)
    }

}
