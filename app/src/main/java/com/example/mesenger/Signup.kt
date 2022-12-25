package com.example.mesenger

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class Signup : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var email : EditText
    private lateinit var Password: EditText

    @SuppressLint("MissingInflatedId")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)



        val backbtn: Button = findViewById(R.id.back)
        val signupbtn: Button = findViewById(R.id.signup)
        auth = Firebase.auth
        email = findViewById(R.id.Email)
        Password = findViewById(R.id.etPassword2)
        val imagebtn : Button = findViewById(R.id.imagebutton)


        imagebtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type= "image/*"
            startActivityForResult(intent,0)
        }


        signupbtn.setOnClickListener {
            val Username: EditText = findViewById(R.id.etUsername)
            var  edtemail = email.text.toString()
            var edtPassword = Password.text.toString()


            if (edtemail.isEmpty() || edtPassword.isEmpty()) {
                Toast.makeText(this, "Please enter email/password !", Toast.LENGTH_LONG).show()
            } else {
                auth.createUserWithEmailAndPassword(edtemail, edtPassword).addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val i = Intent(this@Signup,ContactActivity::class.java)
                            startActivity(i)
                            finish()
                        }
                        else {
                            Toast.makeText(this,"not done",Toast.LENGTH_LONG).show()
                        }
                }
            }
        }


        backbtn.setOnClickListener{
                val i = Intent(this@Signup, MainActivity::class.java)
                startActivity(i)
                finish()
        }

        }
    }


