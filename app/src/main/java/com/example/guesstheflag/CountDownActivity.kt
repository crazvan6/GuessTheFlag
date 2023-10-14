package com.example.guesstheflag

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation
import android.widget.ProgressBar
import android.widget.TextView
import com.google.android.material.card.MaterialCardView
import java.util.logging.Handler

class CountDownActivity : AppCompatActivity() {

    private var progressText : TextView? = null
    private var num : Int? = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_count_down)

        val screenWidth = resources.displayMetrics.widthPixels
        val screenHeight = resources.displayMetrics.heightPixels

        val animationIn = TranslateAnimation(-screenWidth.toFloat(), 0f, 0f, 0f)
        animationIn.duration = 500

        val animationBack = TranslateAnimation(0f, -100f, 0f, 0f)
        animationBack.startOffset = 500
        animationBack.duration = 200

        val animationOut = TranslateAnimation(0f, screenWidth.toFloat(), 0f, 0f)
        animationOut.startOffset = 700
        animationOut.duration = 500

        val animationSet = AnimationSet(true)
        animationSet.addAnimation(animationIn)
        animationSet.addAnimation(animationBack)
        animationSet.addAnimation(animationOut)

        progressText = findViewById(R.id.progressText)

        progressText?.startAnimation(animationSet)

        android.os.Handler().postDelayed({
            progressText?.text = "2"
            progressText?.startAnimation(animationSet)
        }, 1200)
        android.os.Handler().postDelayed({
            progressText?.text = "1"
            progressText?.startAnimation(animationSet)
            progressText?.visibility = View.GONE
        }, 2400)

        android.os.Handler().postDelayed({
            val quizzIntent = Intent(this, QuestionsActivity::class.java)
            startActivity(quizzIntent)
            overridePendingTransition(R.animator.slide_in_right, R.animator.slide_in_left)
            finish()
        }, 3450)
    }
}