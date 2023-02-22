package com.example.mesenger

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.Adaptar.UserAdaptar
//import com.example.mesenger.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class ContactActivity : AppCompatActivity() {

    private lateinit var search : ImageView
    private lateinit var newRecyclerview : RecyclerView

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference
    private lateinit var userlist: ArrayList<User>
    private lateinit var adaptor: UserAdaptar
    private lateinit var userProfilePic : CircleImageView




    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)
        checkifuserisLogedin()


        mAuth = FirebaseAuth.getInstance()
        mDbRef= FirebaseDatabase.getInstance().getReference("User")
        userlist = ArrayList()
        adaptor = UserAdaptar(this, userlist)
        newRecyclerview= findViewById(R.id.UserRecycleView)
        newRecyclerview.setHasFixedSize(true)
        search = findViewById(R.id.Search)
        newRecyclerview.layoutManager = LinearLayoutManager(this)
        newRecyclerview.adapter = adaptor
        userProfilePic = findViewById(R.id.UserProfilePic)
        supportActionBar!!.hide()


        search.setOnClickListener {
            val intent = Intent(this, NewMessages::class.java)
            startActivity(intent)
        }
        val current = mAuth.currentUser
              val URL = FirebaseDatabase.getInstance().getReference("User").child(current!!.uid).child("url")
              URL.addValueEventListener(object : ValueEventListener{
                  override fun onDataChange(snapshot: DataSnapshot) {
                      var profilepic = snapshot.value.toString()
                      Picasso.get().load(profilepic).into(userProfilePic)
                  }

                  override fun onCancelled(error: DatabaseError) {

                  }

              })




        mDbRef.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                userlist.clear()
                for (postSnapshot in snapshot.children){
                    val currentuser = postSnapshot.getValue(User::class.java)
                    if (mAuth.currentUser!!.uid == currentuser!!.uid)
                        continue
                    userlist.add(currentuser!!)
                }
                adaptor.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })





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