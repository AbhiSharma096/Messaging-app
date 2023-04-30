package com.example.mesenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth

class EditProfile : AppCompatActivity() {
      private lateinit var auth: FirebaseAuth


      override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            auth = FirebaseAuth.getInstance()
            setContentView(R.layout.activity_edit_profile)

            var current = auth.currentUser
            var uid = current!!.uid





      }
}