package com.example.mesenger

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.example.mesenger.databinding.ActivityMainBinding
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
    private lateinit var signin: Button
    private lateinit var signup: Button
    private lateinit var binding: ActivityMainBinding
    var customprogress: Dialog? =null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        signin = binding.signin
        signup = binding.signup
        auth = Firebase.auth
        email = binding.etEmail
        Password = binding.etPassword2



        signin.setOnClickListener {
            var etemail = email.text.toString()
            var etpassword = Password.text.toString()
        if ( etemail.isEmpty() || etpassword.isEmpty() ) {
            Toast.makeText(this, "Please enter email/password !", Toast.LENGTH_SHORT).show()
        } else {    Showprogressdialog()
                    auth.signInWithEmailAndPassword(etemail, etpassword)
                    .addOnCompleteListener(this) { task ->
                         if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                            cancelprogressdialog()
                            val i = Intent(this@MainActivity, ContactActivity::class.java)
                             startActivity(i)
                             finish()
                         }    else {
                               // If sign in fails, display a message to the user.
                               Toast.makeText(this, "No user found", Toast.LENGTH_LONG).show()
                             cancelprogressdialog()
                         }
                    }
        }
    }

        signup.setOnClickListener {
            val i = Intent(
                this@MainActivity,
                Signup::class.java
            )
            startActivity(i)
            finish()
        }


    }

    private fun Showprogressdialog (){
        customprogress= Dialog(this@MainActivity)
        customprogress?.setContentView(R.layout.custom_progress)
        customprogress?.show()

    }
    private fun cancelprogressdialog(){
        if(customprogress!=null){
           customprogress?.dismiss()
            customprogress=null
        }
    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")

    }


}