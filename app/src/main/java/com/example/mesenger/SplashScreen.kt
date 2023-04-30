package com.example.mesenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth

class SplashScreen : AppCompatActivity() {
      override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_splash_screen)
            val currentUser = FirebaseAuth.getInstance().currentUser
            val handler = android.os.Handler()
                handler.postDelayed({

                      if (currentUser != null ){
                            val i = Intent(this@SplashScreen, ContactActivity::class.java)
                            startActivity(i)
                            finish()
                      }
                      else{
                             val i = Intent(this@SplashScreen, Signin::class.java)
                             startActivity(i)
                             finish()
                      }

                }, 1000)
      }
}