package com.example.mesenger

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.Adaptar.ChatAdaptor
import com.example.Adaptar.MessageAdaptor
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.util.*
import java.util.ArrayList;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody.Companion.toRequestBody
import okio.IOException
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ChatGPT : AppCompatActivity() {
      var JSON = "application/json; charset=utf-8".toMediaType()
      var client = OkHttpClient()
      private lateinit var messageEditText : EditText
      private lateinit var mDataBase: DatabaseReference
      private lateinit var messagelist: ArrayList<Message>
      private lateinit var ChatAdaptor : ChatAdaptor
      private lateinit var messageRecyclerView : RecyclerView
      private lateinit var sendButton : ImageView
      private lateinit var database : FirebaseDatabase
      private lateinit var back : ImageView
      private lateinit var welcometext : ImageView
      private lateinit var url : String
      private lateinit var progressbar : LinearProgressIndicator


      override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_chat_gpt)
            sendButton = findViewById(R.id.SendButton)
            messageEditText  = findViewById(R.id.MessageEditText)
            mDataBase = FirebaseDatabase.getInstance().getReference()
            messagelist = ArrayList()
            messageRecyclerView = findViewById(R.id.ChatRecycleView1)
            messageRecyclerView.layoutManager = LinearLayoutManager(this)
            database = FirebaseDatabase.getInstance()
            back = findViewById(R.id.backButton)
            welcometext =findViewById(R.id.newmessage)
            progressbar = findViewById(R.id.progressBar)
            val senderuid = FirebaseAuth.getInstance().currentUser?.uid
            url = "https://api.openai.com/v1/completions"
            ChatAdaptor = ChatAdaptor(this,messagelist,senderuid!!,"ChatGPT")
            messageRecyclerView.adapter = ChatAdaptor



            back.setOnClickListener {
                  OnBackPressed()
            }

            mDataBase.child("Chats")
                  .child(senderuid!!)
                  .child("Messages")
                  .addValueEventListener(object : ValueEventListener {


                        override fun onDataChange(snapshot: DataSnapshot) {
                              messagelist.clear()
                              for (postsnapshot in snapshot.children){
                                    val message = postsnapshot.getValue(Message::class.java)
                                    message!!.messageid = postsnapshot.key!!
                                    if (message != null) {
                                          messagelist.add(message)
                                    }
                              }
                              if (messagelist.size == 0){
                                    welcometext.isVisible = true
                                    messageRecyclerView.isVisible = false
                              }
                              else{
                                    welcometext.isVisible = false
                                    messageRecyclerView.isVisible = true
                              }
                                   ChatAdaptor.notifyDataSetChanged()

                              messageRecyclerView.smoothScrollToPosition(messagelist.size - 1)
                        }

                        override fun onCancelled(error: DatabaseError) {

                        }

                  })




            sendButton.setOnClickListener {

                  if(!messageEditText.text.isEmpty()) {
                        val message = messageEditText.text.toString()
                        val date = Date()
                        val messageobject = Message(message,senderuid!!,date.time)
                        val randomkey = database!!.reference.push().key

                        database!!.reference.child("Chats").child(senderuid!!).child("Messages")
                              .child(randomkey!!).setValue(messageobject)
                              .addOnSuccessListener {
                                    progressbar.visibility = ProgressBar.VISIBLE
                                    callAPI(message){response ->

                                                 addResponse(response)
                                    }

                              }
                        messageEditText.text.clear()

                  }
            }


      }
      fun addResponse(response : String){

            val date = Date()
            val messageobject = Message(response,"ChatGpt",date.time)
            val randomkey = database!!.reference.push().key
            val senderuid = FirebaseAuth.getInstance().currentUser?.uid

            database!!.reference.child("Chats").child(senderuid!!).child("Messages")
                  .child(randomkey!!).setValue(messageobject)
                  .addOnSuccessListener {
                            progressbar.visibility = ProgressBar.INVISIBLE

                  }
            messageEditText.text.clear()
      }




      fun callAPI( message : String, callback: (String) -> Unit){
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            val apiKey = "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"
            val url = "https://api.openai.com/v1/completions"

            val requestBody = """
                  {
                    "model": "text-davinci-003",
                    "prompt": "$message",
                    "max_tokens": 100,
                    "temperature": 0
                  }
                  """.trimIndent()

            val request = Request.Builder()
                  .url(url)
                  .addHeader("Content-Type", "application/json")
                  .addHeader("Authorization", "Bearer $apiKey")
                  .post(requestBody.toRequestBody())
                  .build()


            client.newCall(request).enqueue(object : Callback {
                  override fun onFailure(call: Call, e: IOException) {
                        // Handle the failure
                        Log.v("data","failed")
                  }

                  override fun onResponse(call: Call, response: Response) {
                        val Body = response.body?.string()

                        if (Body!=null){
                              Log.v("data",Body)
                        }
                        else (
                                   Log.v("data","null")
                            )
                        val jsonObject = JSONObject(Body)
                        val choices = jsonObject.getJSONArray("choices")
                        val TextResult = choices.getJSONObject(0).getString("text")
                        Log.v("data",TextResult)
                        callback(TextResult)
                  }
            })
      }

      fun OnBackPressed() {
            finish()
      }
}



// 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
//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