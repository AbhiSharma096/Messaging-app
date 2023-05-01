package com.example.Adaptar

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.squareup.picasso.Picasso
import androidx.recyclerview.widget.RecyclerView
import com.example.mesenger.ChatActivity
import com.example.mesenger.R
import com.example.mesenger.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserAdapter(private val context: Context, private val userlist: ArrayList<User>) :
      RecyclerView.Adapter<UserAdapter.ViewHolder>() {

      override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.user_layout, parent, false)
            return ViewHolder(view)
      }

      override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val user = userlist[position]
            holder.username.text = user.username
            Picasso.get().load(user.url)
                  .fit()
                  .centerInside()
                  .into(holder.imageUser)

            // Get the UIDs of the current user and the current user's chat partner.
            val currentUserUid = FirebaseAuth.getInstance().currentUser!!.uid
            val chatPartnerUid = user.uid

            // Construct the ID of the chat room between the current user and the chat partner.
            val chatRoomId = "$currentUserUid$chatPartnerUid"

            // Get the reference to the last message in the chat room.

            GlobalScope.launch {
                  val chatRoomRef = FirebaseDatabase.getInstance().getReference("Chats").child(chatRoomId)
                  val lastMessageRef = chatRoomRef.child("lastMsg")

                  // Read the value of the last message from the database and display it in the UI.
                  lastMessageRef.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {

                              holder.latestMessage.text  =  dataSnapshot.getValue(String::class.java) ?: "No message"
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                              holder.latestMessage.text = "No message"
                        }
                  })
            }


            // Open the chat activity when the user clicks on the user item.
            holder.itemView.setOnClickListener {
                  val intent = Intent(context, ChatActivity::class.java)
                  intent.putExtra("name", user.username)
                  intent.putExtra("uid", user.uid)
                  context.startActivity(intent)
            }
      }

      override fun getItemCount(): Int {
            return userlist.size
      }

      inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val username: TextView = view.findViewById(R.id.name)
            val latestMessage: TextView = view.findViewById(R.id.LatestMessage)
            val imageUser: CircleImageView = view.findViewById(R.id.userimage)
      }
}
