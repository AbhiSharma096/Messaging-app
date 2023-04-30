package com.example.mesenger

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.mesenger.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class ProfileActivity : AppCompatActivity() {

      private lateinit var auth: FirebaseAuth
      private lateinit var email : TextView
       private lateinit var username : TextView
       private lateinit var name : TextView
       private lateinit var phone : TextView
       private lateinit var database : FirebaseDatabase
       private lateinit var userProfilePic : CircleImageView
       private lateinit var backbtn : ImageView
       private lateinit var edit : LinearLayout

      @SuppressLint("MissingInflatedId")
      override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_profile1)

            auth = FirebaseAuth.getInstance()
            email = findViewById(R.id.Profile_Email)
            name = findViewById(R.id.Profile_Name)
            username = findViewById(R.id.Profile_Username)
            phone = findViewById(R.id.PhNo)
            userProfilePic = findViewById(R.id.Profilepic)
            backbtn = findViewById(R.id.backButton)
            edit = findViewById(R.id.edit_Profile)

            val current = auth.currentUser


            val Name = FirebaseDatabase.getInstance().getReference("User").child(current!!.uid).child("username")
            Name.addValueEventListener(object : ValueEventListener {
                  override fun onDataChange(snapshot: DataSnapshot) {
                        var Name1 = snapshot.value.toString()
                        name.text = Name1
                  }

                  override fun onCancelled(error: DatabaseError) {

                  }

            })
            val Email = FirebaseDatabase.getInstance().getReference("User").child(current!!.uid).child("Email")
            Email.addValueEventListener(object : ValueEventListener{
                  override fun onDataChange(snapshot: DataSnapshot) {
                        var Email = snapshot.value.toString()
                        email.text = Email
                  }

                  override fun onCancelled(error: DatabaseError) {

                  }

            })

            backbtn.setOnClickListener {
                  finish()
            }
            edit.setOnClickListener {
                  val intent = Intent(this,EditProfile::class.java)
                       startActivity(intent)
            }

            val URL = FirebaseDatabase.getInstance().getReference("User").child(current!!.uid).child("url")
            URL.addValueEventListener(object : ValueEventListener{
                  override fun onDataChange(snapshot: DataSnapshot) {
                        var profilepic = snapshot.value.toString()
                        Picasso.get().load(profilepic)
                              .fit()
                              .centerInside()
                              .into(userProfilePic)


                  }

                  override fun onCancelled(error: DatabaseError) {

                  }

            })


      }
}