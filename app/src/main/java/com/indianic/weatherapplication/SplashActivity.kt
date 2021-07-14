package com.indianic.weatherapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        firebaseAuth= FirebaseAuth.getInstance()

        // This is used to hide the status bar and make
        // the splash screen as a full screen activity.
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // we used the postDelayed(Runnable, time) method
        // to send a message with a delayed time.
        Handler().postDelayed({
            checkUser()
        }, 2000) // 2000 is the delayed time in milliseconds.
    }
    private fun checkUser() {
        val firebaseUser=firebaseAuth.currentUser
        if (firebaseUser!=null){
            startActivity(Intent(this, VerifyOtpActivity::class.java))
            finish()
        }else{
            startActivity(Intent(this, VerifyOtpActivity::class.java).putExtra("PHONE",firebaseUser?.phoneNumber))
            finish()
        }
    }
}