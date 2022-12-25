package com.example.mesenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import java.net.PasswordAuthentication
import java.util.SplittableRandom
import kotlin.math.sign
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


//val email : String? = null
//val userid : String? = null
//val password : String? = null

class MainActivity : AppCompatActivity() , View.OnClickListener{
    private lateinit var auth: FirebaseAuth
    private lateinit var email : EditText
    private lateinit var Password: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val btn: Button = findViewById(R.id.signin)
        val btn2: Button = findViewById(R.id.signup)
        auth = Firebase.auth
        email = findViewById(R.id.etUsername)
        Password = findViewById(R.id.etPassword2)
        var etemail = email.text.toString()
        var etpassword = Password.text.toString()
        btn.setOnClickListener {
        if (etemail.isEmpty() || etpassword.isEmpty()) {
            Toast.makeText(this, "Please enter email/password !", Toast.LENGTH_LONG).show()
        } else {
                    auth.signInWithEmailAndPassword(etemail, etpassword)
                    .addOnCompleteListener(this) { task ->
                         if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information

                            val i = Intent(
                            this@MainActivity,
                            ContactActivity::class.java
                            )
                             startActivity(i)
                             finish()
                         }    else {
                               // If sign in fails, display a message to the user.
                               Toast.makeText(this, "No user found", Toast.LENGTH_LONG).show()
                         }
                    }
        }
    }

        btn2.setOnClickListener {
            val i = Intent(
                this@MainActivity,
                Signup::class.java
            )
            startActivity(i)


        }

    }

    override fun onClick(p0: View?) {

        TODO("Not yet implemented")
    }
}