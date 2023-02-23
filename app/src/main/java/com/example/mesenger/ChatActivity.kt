package com.example.mesenger

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.ContactsContract.Profile
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.Adaptar.MessageAdaptor
import com.example.mesenger.databinding.ActivityChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.util.Date

class ChatActivity : AppCompatActivity() {
      private lateinit var messageRecyclerView : RecyclerView
      private lateinit var messageEditText : EditText
      private lateinit var sendButton : ImageView
      private lateinit var messageAdaptor: MessageAdaptor
      private lateinit var messagelist: ArrayList<Message>
      var receiverRoom : String ? = null
      var senderRoom : String ? = null
      var binding : ActivityChatBinding? = null
      private lateinit var mDataBase: DatabaseReference
      var database: FirebaseDatabase? = null
      var storage : FirebaseStorage? = null
      var dialog : ProgressDialog? = null



      override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_chat)
            setTheme(R.style.MYTheme_Mesenger)
//            supportActionBar!!.hide()

            val name = intent.getStringExtra("name")
            val Recieveruid = intent.getStringExtra("uid")
            val senderuid = FirebaseAuth.getInstance().currentUser?.uid
            val image: ImageView = findViewById(R.id.RecieverProfilepic)
            val Reciever : TextView = findViewById(R.id.RecieverName)
            mDataBase = FirebaseDatabase.getInstance().getReference()
            database = FirebaseDatabase.getInstance()
            storage = FirebaseStorage.getInstance()
            senderRoom = Recieveruid + senderuid
            receiverRoom = senderuid + Recieveruid
            messageRecyclerView = findViewById(R.id.ChatRecycleView)
            messageEditText = findViewById(R.id.MessageEditText)
            sendButton = findViewById(R.id.SendButton)
            messagelist = ArrayList()

            messageAdaptor = MessageAdaptor(this, messagelist,senderRoom!!,receiverRoom!!)
            messageRecyclerView.layoutManager = LinearLayoutManager(this)
            messageRecyclerView.adapter = messageAdaptor
            val status = findViewById<TextView>(R.id.status)

            database!!.reference.child("Presense").child(Recieveruid!!).addValueEventListener(object : ValueEventListener{
                  override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()){
                              val Status = snapshot.getValue(String::class.java)
                              if (Status == "Offline"){
                                    status.visibility = View.GONE
                              }else{
                                    status.setText(Status)
                                    status.visibility = View.VISIBLE
                              }
                        }
                  }

                  override fun onCancelled(error: DatabaseError) {

                  }
            })

            mDataBase.child("Chats")
                  .child(senderRoom!!)
                  .child("Messages")
                  .addValueEventListener(object : ValueEventListener {


                  override fun onDataChange(snapshot: DataSnapshot) {
                        messagelist.clear()
                        for (postsnapshot in snapshot.children){
                              val message = postsnapshot.getValue(Message::class.java)
                              message!!.messageid = postsnapshot.key!!
                              messagelist.add(message)
                        }
                        messageAdaptor.notifyDataSetChanged()
                        messageRecyclerView.scrollToPosition(messagelist.size - 1)
                  }

                  override fun onCancelled(error: DatabaseError) {

                  }

            })





            Reciever.text = name
            val URL = FirebaseDatabase.getInstance().getReference("User").child(Recieveruid!!).child("url")

            URL.addValueEventListener(object : ValueEventListener{

                  override fun onDataChange(snapshot: DataSnapshot) {
                        var profilepic = snapshot.value.toString()
                        Picasso.get().load(profilepic).into(image)
                  }

                  override fun onCancelled(error: DatabaseError) {

                  }

            })







            val back : ImageView = findViewById(R.id.backButton)



            back.setOnClickListener {
                  OnBackPressed()
            }

            sendButton.setOnClickListener {
                  val message = messageEditText.text.toString()
                  val date = Date()
                  val messageobject = Message(message, senderuid!!, date.time)
                  val randomekey = database!!.reference.push().key
                  val lastMsgObj = HashMap<String,Any>()
                  lastMsgObj["lastMsg"] = messageobject.message!!
                  lastMsgObj["lastMsgTime"] = date.time
                  database!!.reference.child("Chats").child(senderRoom!!).updateChildren(lastMsgObj)
                  database!!.reference.child("Chats").child(receiverRoom!!).updateChildren(lastMsgObj)
                  database!!.reference.child("Chats").child(senderRoom!!).child("Messages").child(randomekey!!).setValue(messageobject)
                            .addOnSuccessListener {
                                   database!!.reference.child("Chats").child(receiverRoom!!).child("Messages").child(randomekey!!).setValue(messageobject)
                                          .addOnSuccessListener {

                                          }
                            }
                  messageEditText.text.clear()
            }

            val handler = Handler()
            messageEditText.addTextChangedListener(object : TextWatcher {
                  override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                  }

                  override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        database!!.reference.child("Presense").child(senderuid!!).setValue("typing...")
                        handler.removeCallbacksAndMessages(null)
                            handler.postDelayed(userStopedTyping, 1000)
                  }
                  var userStopedTyping = Runnable {
                        database!!.reference.child("Presense").child(senderuid!!).setValue("Online")
                  }

                  override fun afterTextChanged(s: Editable?) {

                  }

            })
//            supportActionBar!!.setDisplayShowTitleEnabled(false)




      }



      override fun onResume() {
            super.onResume()
                val uid = FirebaseAuth.getInstance().currentUser?.uid
                     database!!.reference.child("Presense").child(uid!!).setValue("Online")
      }

      override fun onPause() {
            super.onPause()
            val uid = FirebaseAuth.getInstance().currentUser?.uid
                database!!.reference.child("Presense").child(uid!!).setValue("Offline")
      }



      fun OnBackPressed() {
            finish()
      }




}

