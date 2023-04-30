package com.example.mesenger

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.Adaptar.UserAdapter

import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView
//import com.example.mesenger.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import io.supercharge.shimmerlayout.ShimmerLayout

class ContactActivity : AppCompatActivity() {

    private lateinit var newRecyclerview : RecyclerView
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference
    private lateinit var userlist: ArrayList<User>
    private lateinit var adaptor: UserAdapter
    private lateinit var userProfilePic : CircleImageView
    private lateinit var shimmerlayout : ShimmerLayout
    private lateinit var textView: TextView
    private lateinit var ChatGpt: LinearLayout




    @SuppressLint("MissingInflatedId", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)
        `checklistsLoge-din`()


        mAuth = FirebaseAuth.getInstance()
        mDbRef= FirebaseDatabase.getInstance().getReference("User")
        userlist = ArrayList()
        adaptor = UserAdapter(this, userlist)
        newRecyclerview= findViewById(R.id.UserRecycleView)
        newRecyclerview.setHasFixedSize(true)
        shimmerlayout = findViewById(R.id.Shimmer)
        shimmerlayout.startShimmerAnimation()
          ChatGpt = findViewById(R.id.floatingButton)
        val materialtoolbar :MaterialToolbar = findViewById(R.id.toolbar)
        val drawerlayout : DrawerLayout = findViewById(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
          var animator : ValueAnimator? = null
        newRecyclerview.layoutManager = LinearLayoutManager(this)
        newRecyclerview.adapter = adaptor
        userProfilePic = findViewById(R.id.UserProfilePic)
          textView = findViewById(R.id.NewChat)
        val imageview = navigationView.getHeaderView(0).findViewById<ImageView>(R.id.userimage)
        val name = navigationView.getHeaderView(0).findViewById<TextView>(R.id.name)
        val email = navigationView.getHeaderView(0).findViewById<TextView>(R.id.email)



        materialtoolbar.setNavigationOnClickListener {
            drawerlayout.openDrawer(GravityCompat.START)
        }
        navigationView.setNavigationItemSelectedListener {
                when(it.itemId){
                      R.id.home -> {
                          Toast.makeText(this, "nothing", Toast.LENGTH_SHORT).show()
                      }
                      R.id.logout -> {
                     FirebaseAuth.getInstance().signOut()
                     val intent = Intent(this, Signin::class.java)
                     intent.flags= Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                     startActivity(intent)
                      }
                }
                true
        }
          Firebase.messaging.subscribeToTopic("${mAuth.currentUser!!.uid}")
                .addOnCompleteListener { task ->
                      var msg = "Subscribed"
                      if (!task.isSuccessful) {
                            msg = "Subscribe failed"
                      }
                      Log.d(TAG, msg)
                     // Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                }

//          var token :String? = null
//          val getToken = FirebaseDatabase.getInstance().getReference("User").child(mAuth.currentUser!!.uid).child("token")
//               getToken.addValueEventListener(object : ValueEventListener{
//                override fun onDataChange(snapshot: DataSnapshot) {
//                      if (snapshot.exists()) {
//                            token = snapshot.getValue(String::class.java)
//                            var lol = token
//                            Firebase.messaging.subscribeToTopic(mAuth.currentUser!!.uid)
//                                  .addOnCompleteListener { task ->
//                                        var msg = "Subscribed"
//                                        if (!task.isSuccessful) {
//                                              msg = "Subscribe failed"
//                                        }
//                                        Log.d(TAG, msg)
//                                        Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
//                                  }
//                            Toast.makeText(this@ContactActivity, token, Toast.LENGTH_SHORT).show()
//                      }
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//
//                }
//
//               })







            val current = mAuth.currentUser
        val Name = FirebaseDatabase.getInstance().getReference("User").child(current!!.uid).child("username")
              Name.addValueEventListener(object : ValueEventListener{
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
        val URL = FirebaseDatabase.getInstance().getReference("User").child(current!!.uid).child("url")
        URL.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var profilepic = snapshot.value.toString()
                Picasso.get().load(profilepic)
                    .fit()
                    .centerInside()
                    .into(userProfilePic)

                     Picasso.get().load(profilepic).fit().centerInside().into(imageview)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })


        // Open profile activity
              userProfilePic.setOnClickListener {
              val intent = Intent(this, ProfileActivity::class.java)
              startActivity(intent)
              }
              imageview.setOnClickListener {
              val intent = Intent(this, ProfileActivity::class.java)
              startActivity(intent)
              }
              name.setOnClickListener {
              val intent = Intent(this, ProfileActivity::class.java)
              startActivity(intent)
              }

               // Open chat activity
              ChatGpt.setOnClickListener {
              val intent = Intent(this, ChatGPT::class.java)
                   // intent.putExtra("room", current.uid)
              startActivity(intent)
              }



          newRecyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener(){
              override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                  super.onScrolled(recyclerView, dx, dy)
                  if (dy > 0){
                      if (animator == null){
                          animator = createAnimator()
                          animator!!.start()
                      }
                  }else{
                      if (animator != null){
                          animator!!.reverse()
                          animator = null
                      }
                  }
              }
          })


          mDbRef.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                userlist.clear()
                for (postSnapshot in snapshot.children){
                    val currentuser = postSnapshot.getValue(User::class.java)
                    if (mAuth.currentUser!!.uid.toString() == currentuser!!.uid.toString())
                        continue
                    userlist.add(currentuser!!)
                }
                  // Shimmer layout stop
                shimmerlayout.stopShimmerAnimation()
                shimmerlayout.isVisible = false
                newRecyclerview.isVisible = true
                adaptor.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })





    }
      private fun createAnimator(): ValueAnimator{
            val initsize = textView.measuredWidth
            val animator = ValueAnimator.ofInt(initsize, 0)
            animator.duration = 600
                animator.addUpdateListener {
                      val value = it.animatedValue as Int
                      val params = textView.layoutParams
                      params.width = value
                      textView.requestLayout()
                }
            return animator
      }
      private fun `checklistsLoge-din`() {
        val uri = FirebaseAuth.getInstance().uid
        if (uri == null ){
            val i = Intent(this, Signin::class.java)
            i.flags= Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(i)
        }
    }
}