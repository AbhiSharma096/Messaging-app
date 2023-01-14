package com.example.mesenger

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import com.example.mesenger.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class ContactActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var search : ImageView




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)

        checkifuserisLogedin()
        supportActionBar!!.hide()
        search = findViewById(R.id.Search)


        search.setOnClickListener {
            val intent = Intent(this, NewMessages::class.java)
            startActivity(intent)
        }
    }

    private fun checkifuserisLogedin() {
        val uri = FirebaseAuth.getInstance().uid
        if (uri == null ){
            val i = Intent(this, Signin::class.java)
            i.flags= Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(i)
        }
    }
}