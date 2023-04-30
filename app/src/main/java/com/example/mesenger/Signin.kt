package com.example.mesenger

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.mesenger.databinding.ActivityMainBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging


//val email : String? = null
//val userid : String? = null
//val password : String? = null

class Signin : AppCompatActivity() , View.OnClickListener{
    private lateinit var auth: FirebaseAuth
    private lateinit var email : EditText
    private lateinit var Password: EditText
    private lateinit var signin: Button
    private lateinit var signup: Button
    private lateinit var binding: ActivityMainBinding
    private var customprogress: Dialog? =null
    private var token : String? = null


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

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            token = task.result

            // Log and toast
            val msg = token
            if (msg != null) {
                Log.d("TAG", msg)
            } else {
                Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
            }
        })



        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null ){
            val i = Intent(this@Signin, ContactActivity::class.java)
            startActivity(i)
            finish()
        }


        signin.setOnClickListener {
            var etemail = email.text.toString()
            var etpassword = Password.text.toString()
        if ( etemail.isEmpty() || etpassword.isEmpty() ) {
            Toast.makeText(this, "Please enter email/password !", Toast.LENGTH_SHORT).show()
        } else {    Showprogressdialog()
                    auth.signInWithEmailAndPassword(etemail, etpassword)
                    .addOnCompleteListener(this) { task ->
                         if (task.isSuccessful) {

                            cancelprogressdialog()
                                    val i = Intent(this@Signin, ContactActivity::class.java)
                                    i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    startActivity(i)
                                    finish()

                         }  else {
                               // If sign in fails, display a message to the user.
                               Toast.makeText(this, "No user found", Toast.LENGTH_LONG).show()
                             cancelprogressdialog()
                         }
                    }
        }
    }

        signup.setOnClickListener {
            val i = Intent(
                this@Signin,
                Signup::class.java
            )
            startActivity(i)
            finish()
        }


    }

    private fun Showprogressdialog (){
        customprogress= Dialog(this@Signin)
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