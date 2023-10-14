package com.example.guesstheflag

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.TranslateAnimation
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.postDelayed
import com.google.android.material.card.MaterialCardView
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class OptionsActivity : AppCompatActivity() {

    private var btnRegisterOption: TextView? = null
    private var btnLoginOption: TextView? = null
    private var btnRegister: TextView? = null
    private var btnLogin: TextView? = null
    private var btnForgotPassword: TextView? = null
    private var registerForm: MaterialCardView? = null
    private var loginForm: MaterialCardView? = null
    private var optionsForm: MaterialCardView? = null
    private var registerToggled: Boolean = false
    private var loginToggled: Boolean = false
    private var registerEmail: EditText? = null
    private var userName: EditText? = null
    private var registerPassword: EditText? = null
    private var loginEmail: EditText? = null
    private var loginPassword: EditText? = null

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_options)
        FirebaseApp.initializeApp(this)
        auth = Firebase.auth

        val screenHeight = resources.displayMetrics.heightPixels
        val screenWidth = resources.displayMetrics.widthPixels

        btnRegister = findViewById(R.id.btnRegister)
        btnLogin = findViewById(R.id.btnLogin)
        btnRegisterOption = findViewById(R.id.btnRegisterOption)
        btnLoginOption = findViewById(R.id.btnLoginOption)
        btnForgotPassword = findViewById(R.id.btnForgotPassword)
        registerForm = findViewById(R.id.registerForm)
        loginForm = findViewById(R.id.loginForm)
        optionsForm = findViewById(R.id.optionsForm)

        val animationFromTop = TranslateAnimation(0f, 0f, -screenHeight.toFloat(), 0f)
        animationFromTop.duration = 1500
        animationFromTop.fillAfter = true

        val animationFromLeft = TranslateAnimation( -screenWidth.toFloat(), 0f, 0f, 0f)
        animationFromLeft.duration = 500
        animationFromLeft.fillAfter = true

        val animationToRight = TranslateAnimation(0f, screenWidth.toFloat(), 0f, 0f)
        animationToRight.duration = 500
        animationToRight.fillAfter = true

        optionsForm?.startAnimation(animationFromTop)


        btnRegisterOption?.setOnClickListener {
            registerToggled = true

            if(loginToggled){
                loginForm?.startAnimation(animationToRight)

                android.os.Handler().postDelayed({
                    loginForm?.visibility = View.GONE
                    registerForm?.visibility = View.VISIBLE
                    registerForm?.startAnimation(animationFromLeft)
                    registerForm?.bringToFront()
                }, 500)

                loginToggled = false
            }
            else{
                registerForm?.visibility = View.VISIBLE
                registerForm?.startAnimation(animationFromLeft)
            }
        }

        btnLoginOption?.setOnClickListener {
            loginToggled = true

            if(registerToggled){
                registerForm?.startAnimation(animationToRight)

                android.os.Handler().postDelayed({
                    registerForm?.visibility = View.GONE
                    loginForm?.visibility = View.VISIBLE
                    loginForm?.startAnimation(animationFromLeft)
                    loginForm?.bringToFront()
                },500)

                registerToggled = false
            }
            else{
                loginForm?.visibility = View.VISIBLE
                loginForm?.startAnimation(animationFromLeft)
            }
        }


        btnRegister?.setOnClickListener {
            registerEmail = findViewById(R.id.registerEmail)
            userName = findViewById(R.id.userName)
            registerPassword = findViewById(R.id.registerPassword)

            val email: String = registerEmail?.text.toString()
            val username: String = userName?.text.toString()
            val password: String = registerPassword?.text.toString()
            UserData.setUserName(username)

            if (isValidInput(email, username, password)) {
                checkIfEmailExists(email, username, password)
            }
        }
        btnLogin?.setOnClickListener {
            loginEmail = findViewById(R.id.loginEmail)
            loginPassword = findViewById(R.id.loginPassword)

            val email: String = loginEmail?.text.toString()
            val password: String = loginPassword?.text.toString()

            if(isDataLoginValid(email, password)) loginUserWithEmailAndPassword(email, password)
        }

        btnForgotPassword?.setOnClickListener {
            loginEmail = findViewById(R.id.loginEmail)

            val email: String = loginEmail?.text.toString()
            if(email.isNotEmpty()){
                val auth = FirebaseAuth.getInstance()

                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            showToast("Check your email!")
                        }
                    }

            }
            else{
                showToast("Type your email address!")
            }
        }

    }
    private fun isValidInput(email: String, username: String, password: String): Boolean {

        if (email.isEmpty()){
            showToast("Type Your Email!")
            return false
        }
        if (username.isEmpty()){
            showToast("Type Your Username!")
            return false
        }
        return isPasswordValid(password)
    }

    private fun isPasswordValid(password: String) : Boolean{

        val passwordPattern = Regex("^.{6,}$")
        if (passwordPattern.matches(password)){
            if(password.isEmpty()){
                showToast("Type a password!")
                return false
            }
            return true
        }
        else{
            showToast("Password must contain at least 6 characters!")
            return false
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun registerUserWithEmailAndPassword(email: String, password: String, username: String) {
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    auth.currentUser?.sendEmailVerification()
                        ?.addOnCompleteListener {
                            showToast("Check your email to confirm and login!")

                            val userId = FirebaseAuth.getInstance().currentUser?.uid
                            val firestore = FirebaseFirestore.getInstance()
                            val userDocRef = firestore.collection("users").document(userId!!)

                            val data = hashMapOf(
                                "name" to username,
                                "points" to 0,
                                "profileImg" to "",
                                "candyAchieved" to false,
                                "bronzeAchieved" to false,
                                "silverAchieved" to false,
                                "goldAchieved" to false,
                                "trophyAchieved" to false

                            )
                            userDocRef.set(data)
                        }
                        ?.addOnFailureListener{
                            showToast("Please try again!")
                        }

                } else {
                    val errorMessage = task.exception?.message
                    if (errorMessage != null) {
                        showToast("Registration error: $errorMessage")
                    }
                }
            }
    }

    private fun checkIfEmailExists(email: String, username: String, password: String) {
        val auth = FirebaseAuth.getInstance()

        auth.fetchSignInMethodsForEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val signInMethods = task.result?.signInMethods
                    if (signInMethods.isNullOrEmpty()) {
                        registerUserWithEmailAndPassword(email, password, username)
                    }
                } else {
                    val errorMessage = task.exception?.message

                    if (errorMessage != null) {
                        showToast("$errorMessage")
                    }
                }
            }
    }

    private fun loginUserWithEmailAndPassword(email: String, password: String) {
        val auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    val isVerified = auth.currentUser?.isEmailVerified
                    if(isVerified!!) {
                        showToast("Successfully logged in!")
                        startActivity(Intent(this, ProfileActivity::class.java))
                        overridePendingTransition(
                            R.animator.slide_in_left,
                            R.animator.slide_in_right
                        )
                        finish()
                    }
                    else{
                        showToast("Account not verified!")
                    }
                } else {
                    val errorMessage = task.exception?.message
                    if (errorMessage != null) {
                        showToast("$errorMessage")
                    }
                }
            }
    }

    private fun isDataLoginValid(email: String, password: String): Boolean{

        return if(email.isEmpty()){

            showToast("Type your mail!")
            false
        } else if(password.isEmpty()){

            showToast("Type your password!")
            false
        } else true
    }

}