package com.example.Adaptar


import android.annotation.SuppressLint
import android.view.View
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mesenger.R
import com.example.mesenger.User






abstract class UserAdaptar(private val context: Context, private val userlist: ArrayList<User>) :
    RecyclerView.Adapter<UserAdaptar.ViewHolder>() {

    class ViewHolder(view: View):RecyclerView.ViewHolder(view){

    }


    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        //val view = LayoutInflater.from(parent.context).inflate(R.layout.user_layout, parent, false)

          val view = LayoutInflater.from(parent.context).inflate(R.layout.user_layout,parent,false)
            return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }


    override fun getItemCount(): Int {
       return userlist.size
    }


}