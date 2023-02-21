package com.example.Adaptar


import android.annotation.SuppressLint
import android.view.View
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.squareup.picasso.Picasso
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mesenger.ChatActivity
import com.example.mesenger.R
import com.example.mesenger.User
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView


 class UserAdaptar(private val context: Context, private val userlist: ArrayList<User>) :
    RecyclerView.Adapter<UserAdaptar.ViewHolder>() {





    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

          val view = LayoutInflater.from(parent.context).inflate(R.layout.user_layout,parent,false)
            return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = userlist[position]
          holder.username.text=user.username
          holder.itemView.setOnClickListener {
                val intent = Intent(context, ChatActivity::class.java)
                intent.putExtra("name",user.username)
                intent.putExtra("uid",user.uid)
                context.startActivity(intent)
          }
          Picasso.get().load(user.url).into(holder.imageUser)
    }


    override fun getItemCount(): Int {
       return userlist.size
    }

      class ViewHolder(view: View):RecyclerView.ViewHolder(view){
            val username: TextView = view.findViewById(R.id.name)
            val textDummy: TextView = view.findViewById(R.id.LatestMessage)
            val imageUser:  CircleImageView = view.findViewById(R.id.userimage)
      }


}