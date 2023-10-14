package com.example.guesstheflag

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationSet
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class QuestionsActivity : AppCompatActivity() {
    private var optionOne : TextView? = null
    private var optionTwo : TextView? = null
    private var optionThree : TextView? = null
    private var optionFourth : TextView? = null
    private var flagImg : ImageView? = null
    private var progressBar: ProgressBar? = null
    private var qIndex : Int? = 0
    private var selectedOption : Int? = 0
    private var incorrectAnswer : Boolean? = false
    private var qScore: Int? = 0
    private var currentProgress: Int = 1
    private var progressDuration: Long = 1000

    //@SuppressLint("ObjectAnimatorBinding")
    @SuppressLint("ObjectAnimatorBinding")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questions)

        val screenWidth = resources.displayMetrics.widthPixels
        val screenHeight = resources.displayMetrics.heightPixels

        val animationToLeftOne = TranslateAnimation(0f, -screenWidth.toFloat(), 0f, 0f)
        animationToLeftOne.duration = 1000
        animationToLeftOne.fillAfter = true

        val animationToRightTwo = TranslateAnimation(0f, screenWidth.toFloat(), 0f, 0f)
        animationToRightTwo.duration = 1000
        animationToRightTwo.fillAfter = true
        animationToRightTwo.startOffset = 300

        val animationToLeftThree = TranslateAnimation(0f, -screenWidth.toFloat(), 0f, 0f)
        animationToLeftThree.duration = 1000
        animationToLeftThree.fillAfter = true
        animationToLeftThree.startOffset = 600

        val animationToRightFourth = TranslateAnimation(0f, screenWidth.toFloat(), 0f, 0f)
        animationToRightFourth.duration = 1000
        animationToRightFourth.fillAfter = true
        animationToRightFourth.startOffset = 900

        val animationFromLeftOne = TranslateAnimation(-screenWidth.toFloat(), 0f, 0f, 0f)
        animationFromLeftOne.duration = 1000
        animationFromLeftOne.fillAfter = true

        val animationFromRightTwo = TranslateAnimation(screenWidth.toFloat(), 0f, 0f, 0f)
        animationFromRightTwo.duration = 1000
        animationFromRightTwo.fillAfter = true
        animationFromRightTwo.startOffset = 300

        val animationFromLeftThree = TranslateAnimation(-screenWidth.toFloat(), 0f, 0f, 0f)
        animationFromLeftThree.duration = 1000
        animationFromLeftThree.fillAfter = true
        animationFromLeftThree.startOffset = 600

        val animationFromRightFourth = TranslateAnimation(screenWidth.toFloat(), 0f, 0f, 0f)
        animationFromRightFourth.duration = 1000
        animationFromRightFourth.fillAfter = true
        animationFromRightFourth.startOffset = 900

        val animationFromBottom = TranslateAnimation(0f, 0f, screenHeight.toFloat(), 0f)
        animationFromBottom.duration = 500
        animationFromBottom.fillAfter = true

        val animationToBottom = TranslateAnimation(0f, 0f, 0f, screenHeight.toFloat())
        animationToBottom.duration = 1000
        animationToBottom.fillAfter = true

        val animationFromTop = TranslateAnimation(0f, 0f, -screenHeight.toFloat(), 0f)
        animationFromTop.duration = 1000
        animationFromTop.fillAfter = true

        val animationToTop = TranslateAnimation(0f, 0f, 0f, -screenHeight.toFloat())
        animationToTop.duration = 1500
        animationToTop.fillAfter = true

        val animationBuzzLeft = TranslateAnimation(0f, -100f, 0f, 0f)
        animationBuzzLeft.duration = 200
        animationToTop.fillAfter = true

        val animationBuzzRight = TranslateAnimation(-100f, 100f, 0f, 0f)
        animationBuzzRight.startOffset = 200
        animationBuzzRight.duration = 200
        animationToTop.fillAfter = true

        val animationBack = TranslateAnimation(100f, 0f, 0f, 0f)
        animationBack.startOffset = 400
        animationBack.duration = 200
        animationToTop.fillAfter = true

        val animationBuzz = AnimationSet(true)
        animationBuzz.addAnimation(animationBuzzLeft)
        animationBuzz.addAnimation(animationBuzzRight)
        animationBuzz.addAnimation(animationBack)

        optionOne = findViewById(R.id.optionOne)
        optionTwo = findViewById(R.id.optionTwo)
        optionThree = findViewById(R.id.optionThree)
        optionFourth = findViewById(R.id.optionFour)

        flagImg = findViewById(R.id.flagImg)

        val progressBar = findViewById<ProgressBar>(R.id.progressBar)


        val btnSubmit : TextView = findViewById(R.id.btnSubmit)

        val newQuizz: ArrayList<Question> = Constants.getQuizz()

        fun runOptionsInAnimation(){
            optionOne?.startAnimation(animationFromLeftOne)
            optionTwo?.startAnimation(animationFromRightTwo)
            optionThree?.startAnimation(animationFromLeftThree)
            optionFourth?.startAnimation(animationFromRightFourth)
        }
        fun runOptionsOutAnimation(){
            optionOne?.startAnimation(animationToLeftOne)
            optionTwo?.startAnimation(animationToRightTwo)
            optionThree?.startAnimation(animationToLeftThree)
            optionFourth?.startAnimation(animationToRightFourth)
        }

        fun runQuizz() {
            val startQuestion: Question = newQuizz[0]

            optionOne?.text = startQuestion.optionsArray[0].second
            optionTwo?.text = startQuestion.optionsArray[1].second
            optionThree?.text = startQuestion.optionsArray[2].second
            optionFourth?.text = startQuestion.optionsArray[3].second

            runOptionsInAnimation()

            selectedOption = -1

            flagImg?.setBackgroundResource(startQuestion.correctAnswer.first)
            flagImg?.startAnimation(animationFromTop)

            optionOne?.setOnClickListener {
                btnSubmit.visibility = View.VISIBLE
                btnSubmit.startAnimation(animationFromBottom)

                optionOne?.setBackgroundResource(R.drawable.ic_option_button_clicked)
                selectedOption = 0

                optionTwo?.setBackgroundResource(R.drawable.ic_option_button_shape)
                optionThree?.setBackgroundResource(R.drawable.ic_option_button_shape)
                optionFourth?.setBackgroundResource(R.drawable.ic_option_button_shape)
            }
            optionTwo?.setOnClickListener {
                btnSubmit.visibility = View.VISIBLE
                btnSubmit.startAnimation(animationFromBottom)

                optionTwo?.setBackgroundResource(R.drawable.ic_option_button_clicked)
                selectedOption = 1

                optionOne?.setBackgroundResource(R.drawable.ic_option_button_shape)
                optionThree?.setBackgroundResource(R.drawable.ic_option_button_shape)
                optionFourth?.setBackgroundResource(R.drawable.ic_option_button_shape)
            }
            optionThree?.setOnClickListener {
                btnSubmit.visibility = View.VISIBLE
                btnSubmit.startAnimation(animationFromBottom)

                optionThree?.setBackgroundResource(R.drawable.ic_option_button_clicked)
                selectedOption = 2

                optionOne?.setBackgroundResource(R.drawable.ic_option_button_shape)
                optionTwo?.setBackgroundResource(R.drawable.ic_option_button_shape)
                optionFourth?.setBackgroundResource(R.drawable.ic_option_button_shape)
            }
            optionFourth?.setOnClickListener {
                btnSubmit.visibility = View.VISIBLE
                btnSubmit.startAnimation(animationFromBottom)

                optionFourth?.setBackgroundResource(R.drawable.ic_option_button_clicked)
                selectedOption = 3

                optionOne?.setBackgroundResource(R.drawable.ic_option_button_shape)
                optionTwo?.setBackgroundResource(R.drawable.ic_option_button_shape)
                optionThree?.setBackgroundResource(R.drawable.ic_option_button_shape)
            }
        }

            btnSubmit.setOnClickListener {

                btnSubmit.startAnimation(animationToBottom)

                val animator = ValueAnimator.ofInt(progressBar.progress, progressBar.progress + 10)
                animator.addUpdateListener { animation ->
                    val progress = animation.animatedValue as Int
                    progressBar.progress = progress
                }
                animator.duration = 300
                animator.start()

                btnSubmit.visibility = View.GONE
                btnSubmit.isClickable = false

                android.os.Handler().postDelayed({
                    flagImg?.startAnimation(animationToTop)
                    runOptionsOutAnimation()
                },1500)

                if (qIndex == 9) {
                    when (selectedOption) {
                        0 -> {
                            if (newQuizz[qIndex!!].id == selectedOption) {
                                optionOne?.setBackgroundResource(R.drawable.ic_correct_option_clicked)
                                optionOne?.setTextColor(Color.WHITE)
                                qScore = qScore!! + 1
                            } else {
                                optionOne?.setBackgroundResource(R.drawable.ic_incorrect_option_clicked)
                                optionOne?.setTextColor(Color.WHITE)
                                incorrectAnswer = true

                            }
                        }

                        1 -> {
                            if (newQuizz[qIndex!!].id == selectedOption) {
                                optionTwo?.setBackgroundResource(R.drawable.ic_correct_option_clicked)
                                optionTwo?.setTextColor(Color.WHITE)
                                qScore = qScore!! + 1
                            } else {
                                optionTwo?.setBackgroundResource(R.drawable.ic_incorrect_option_clicked)
                                optionTwo?.setTextColor(Color.WHITE)
                                incorrectAnswer = true
                            }
                        }

                        2 -> {
                            if (newQuizz[qIndex!!].id == selectedOption) {
                                optionThree?.setBackgroundResource(R.drawable.ic_correct_option_clicked)
                                optionThree?.setTextColor(Color.WHITE)
                                qScore = qScore!! + 1
                            } else {
                                optionThree?.setBackgroundResource(R.drawable.ic_incorrect_option_clicked)
                                optionThree?.setTextColor(Color.WHITE)
                                incorrectAnswer = true
                            }
                        }

                        3 -> {
                            if (newQuizz[qIndex!!].id == selectedOption) {
                                optionFourth?.setBackgroundResource(R.drawable.ic_correct_option_clicked)
                                optionFourth?.setTextColor(Color.WHITE)
                                qScore = qScore!! + 1
                            } else {
                                optionFourth?.setBackgroundResource(R.drawable.ic_incorrect_option_clicked)
                                optionFourth?.setTextColor(Color.WHITE)
                                incorrectAnswer = true
                            }
                        }

                    }
                    updatePoints(qScore)
                    if(incorrectAnswer == true){
                        when(newQuizz[qIndex!!].id){
                            0 -> {
                                optionOne?.setBackgroundResource(R.drawable.ic_correct_option_hint)
                                optionOne?.startAnimation(animationBuzz)
                            }
                            1 -> {
                                optionTwo?.setBackgroundResource(R.drawable.ic_correct_option_hint)
                                optionTwo?.startAnimation(animationBuzz)
                            }
                            2 -> {
                                optionThree?.setBackgroundResource(R.drawable.ic_correct_option_hint)
                                optionThree?.startAnimation(animationBuzz)
                            }
                            3 -> {
                                optionFourth?.setBackgroundResource(R.drawable.ic_correct_option_hint)
                                optionFourth?.startAnimation(animationBuzz)
                            }
                        }
                    }

                    UserData.addScore(qScore)

                    android.os.Handler().postDelayed({
                        UserData.getCustomAchievement(qScore){ result ->

                            if(!result){
                                UserData.setCustomAchievement(qScore)
                                startActivity(Intent(this, RewardsActivity::class.java))
                                overridePendingTransition(R.animator.slide_in_right, R.animator.slide_in_left)
                                finish()
                            }
                            else{
                                val resultsIntent = Intent(this, ResultActivity::class.java)
                                startActivity(resultsIntent)
                                overridePendingTransition(R.animator.slide_in_right, R.animator.slide_in_left)
                                finish()
                            }
                        }
                    }, 3100)
                } else {

                    when (selectedOption) {
                        0 -> {
                            if (newQuizz[qIndex!!].id == selectedOption) {
                                optionOne?.setBackgroundResource(R.drawable.ic_correct_option_clicked)
                                optionOne?.setTextColor(Color.WHITE)
                                qScore = qScore!! + 1
                            } else {
                                optionOne?.setBackgroundResource(R.drawable.ic_incorrect_option_clicked)
                                optionOne?.setTextColor(Color.WHITE)
                                incorrectAnswer = true
                            }
                        }

                        1 -> {
                            if (newQuizz[qIndex!!].id == selectedOption) {
                                optionTwo?.setBackgroundResource(R.drawable.ic_correct_option_clicked)
                                optionTwo?.setTextColor(Color.WHITE)
                                qScore = qScore!! + 1
                            } else {
                                optionTwo?.setBackgroundResource(R.drawable.ic_incorrect_option_clicked)
                                optionTwo?.setTextColor(Color.WHITE)
                                incorrectAnswer = true
                            }
                        }

                        2 -> {
                            if (newQuizz[qIndex!!].id == selectedOption) {
                                optionThree?.setBackgroundResource(R.drawable.ic_correct_option_clicked)
                                optionThree?.setTextColor(Color.WHITE)
                                qScore = qScore!! + 1
                            } else {
                                optionThree?.setBackgroundResource(R.drawable.ic_incorrect_option_clicked)
                                optionThree?.setTextColor(Color.WHITE)
                                incorrectAnswer = true
                            }
                        }

                        3 -> {
                            if (newQuizz[qIndex!!].id == selectedOption) {
                                optionFourth?.setBackgroundResource(R.drawable.ic_correct_option_clicked)
                                optionFourth?.setTextColor(Color.WHITE)
                                qScore = qScore!! + 1
                            } else {
                                optionFourth?.setBackgroundResource(R.drawable.ic_incorrect_option_clicked)
                                optionFourth?.setTextColor(Color.WHITE)
                                incorrectAnswer = true
                            }
                        }

                    }
                    if(incorrectAnswer == true){
                        when(newQuizz[qIndex!!].id){
                            0 -> {
                                optionOne?.setBackgroundResource(R.drawable.ic_correct_option_hint)
                                optionOne?.startAnimation(animationBuzz)
                            }
                            1 -> {
                                optionTwo?.setBackgroundResource(R.drawable.ic_correct_option_hint)
                                optionTwo?.startAnimation(animationBuzz)
                            }
                            2 -> {
                                optionThree?.setBackgroundResource(R.drawable.ic_correct_option_hint)
                                optionThree?.startAnimation(animationBuzz)
                            }
                            3 -> {
                                optionFourth?.setBackgroundResource(R.drawable.ic_correct_option_hint)
                                optionFourth?.startAnimation(animationBuzz)
                            }
                        }
                    }

                    qIndex = qIndex!! + 1
                    selectedOption = -1
                    incorrectAnswer = false

                    android.os.Handler().postDelayed({
                        optionOne?.setBackgroundResource(R.drawable.ic_option_button_shape)
                        optionTwo?.setBackgroundResource(R.drawable.ic_option_button_shape)
                        optionThree?.setBackgroundResource(R.drawable.ic_option_button_shape)
                        optionFourth?.setBackgroundResource(R.drawable.ic_option_button_shape)

                        optionOne?.setTextColor(Color.rgb(159, 149, 149))
                        optionTwo?.setTextColor(Color.rgb(159, 149, 149))
                        optionThree?.setTextColor(Color.rgb(159, 149, 149))
                        optionFourth?.setTextColor(Color.rgb(159, 149, 149))


                        optionOne?.text = newQuizz[qIndex!!].optionsArray[0].second
                        optionTwo?.text = newQuizz[qIndex!!].optionsArray[1].second
                        optionThree?.text = newQuizz[qIndex!!].optionsArray[2].second
                        optionFourth?.text = newQuizz[qIndex!!].optionsArray[3].second

                        runOptionsInAnimation()

                        selectedOption = -1

                        flagImg?.setBackgroundResource(newQuizz[qIndex!!].correctAnswer.first)
                        flagImg?.startAnimation(animationFromTop)

                        optionOne?.setOnClickListener {
                            btnSubmit.visibility = View.VISIBLE
                            btnSubmit.isClickable = true
                            btnSubmit.startAnimation(animationFromBottom)

                            optionOne?.setBackgroundResource(R.drawable.ic_option_button_clicked)
                            selectedOption = 0

                            optionTwo?.setBackgroundResource(R.drawable.ic_option_button_shape)
                            optionThree?.setBackgroundResource(R.drawable.ic_option_button_shape)
                            optionFourth?.setBackgroundResource(R.drawable.ic_option_button_shape)
                        }
                        optionTwo?.setOnClickListener {
                            btnSubmit.visibility = View.VISIBLE
                            btnSubmit.isClickable = true
                            btnSubmit.startAnimation(animationFromBottom)

                            optionTwo?.setBackgroundResource(R.drawable.ic_option_button_clicked)
                            selectedOption = 1

                            optionOne?.setBackgroundResource(R.drawable.ic_option_button_shape)
                            optionThree?.setBackgroundResource(R.drawable.ic_option_button_shape)
                            optionFourth?.setBackgroundResource(R.drawable.ic_option_button_shape)
                        }
                        optionThree?.setOnClickListener {
                            btnSubmit.visibility = View.VISIBLE
                            btnSubmit.isClickable = true
                            btnSubmit.startAnimation(animationFromBottom)

                            optionThree?.setBackgroundResource(R.drawable.ic_option_button_clicked)
                            selectedOption = 2

                            optionOne?.setBackgroundResource(R.drawable.ic_option_button_shape)
                            optionTwo?.setBackgroundResource(R.drawable.ic_option_button_shape)
                            optionFourth?.setBackgroundResource(R.drawable.ic_option_button_shape)
                        }
                        optionFourth?.setOnClickListener {
                            btnSubmit.visibility = View.VISIBLE
                            btnSubmit.isClickable = true
                            btnSubmit.startAnimation(animationFromBottom)

                            optionFourth?.setBackgroundResource(R.drawable.ic_option_button_clicked)
                            selectedOption = 3

                            optionOne?.setBackgroundResource(R.drawable.ic_option_button_shape)
                            optionTwo?.setBackgroundResource(R.drawable.ic_option_button_shape)
                            optionThree?.setBackgroundResource(R.drawable.ic_option_button_shape)
                        }
                    }, 3000)

                }
            }

        runQuizz()

    }

    private fun updatePoints(quizzScore: Int?) {

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val firestore = FirebaseFirestore.getInstance()
        val userDocRef = firestore.collection("users").document(userId!!)

        userDocRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {

                    val data = documentSnapshot.data
                    if (data != null && data.contains("points")) {

                        var points = data["points"] as Long
                        points += quizzScore!!

                        userDocRef.update("points", points)
                    }
                }
            }
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}