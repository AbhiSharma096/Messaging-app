package com.example.Adaptar

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mesenger.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class MessageAdaptor(val context:Context,val messages: List<com.example.mesenger.Message>,senderRoom : String,receiverRoom: String):RecyclerView.Adapter <RecyclerView.ViewHolder?>(){

      val sent = 1
       val received = 2
      lateinit var message : ArrayList<Message>
     var senderRoom = senderRoom
       var receiverRoom = receiverRoom

      init {
            if (messages != null) {
                  this.message = messages as ArrayList<Message>
            }
            this.senderRoom = senderRoom
                this.receiverRoom = receiverRoom
      }



      override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            if (viewType == sent){
                  //inflate sent layout
                  val view = LayoutInflater.from(context).inflate(R.layout.send,parent,false)
                  return sentViewHolder(view)
            }
            else{
                  val view = LayoutInflater.from(context).inflate(R.layout.recieved,parent,false)
                  return receivedViewHolder(view)
            }
      }

      override fun getItemCount(): Int = messages.size

      @SuppressLint("MissingInflatedId", "SuspiciousIndentation")
      override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

            val currentMessage = messages[position]
            if (holder.javaClass == sentViewHolder::class.java){

                  val view = holder as sentViewHolder
                  view.sentMessage.text = currentMessage.message
                  view.itemView.setOnLongClickListener{

                        val view = LayoutInflater.from(context).inflate(R.layout.delete_layout,null)
                        val dialog : AlertDialog? = AlertDialog.Builder(context)
                                   .setTitle("Delete Message")
                                   .setView(view)
                                   .create()
                        val everyone = view.findViewById<TextView>(R.id.everyone)
                        everyone.setOnClickListener {
                              currentMessage.message = "This message was deleted"
                              currentMessage.messageid?.let { it1 ->
                                    FirebaseDatabase.getInstance().reference.child("Chats")
                                          .child(senderRoom)
                                          .child("messages")
                                          .child(it1)
                                          .setValue(currentMessage)
                                          .addOnSuccessListener {
                                                dialog?.dismiss()
                                          }

                              }
                                   currentMessage.messageid?.let { it1 ->
                                          FirebaseDatabase.getInstance().reference.child("Chats")
                                                 .child(receiverRoom)
                                                 .child("messages")
                                                 .child(it1!!)
                                                 .setValue(currentMessage)
                                                 .addOnSuccessListener {
                                                 dialog?.dismiss()
                                                 }

                                   }
                        }
                        val delete = view.findViewById<TextView>(R.id.delete)
                            delete.setOnClickListener {
                                   currentMessage.messageid?.let { it1 ->
                                          FirebaseDatabase.getInstance().reference.child("Chats")
                                                 .child(senderRoom)
                                                 .child("messages")
                                                 .child(it1)
                                                 .setValue(currentMessage)
                                                 .addOnSuccessListener {
                                                 dialog?.dismiss()
                                                 }

                                   }
                            }
                            val cancel = view.findViewById<TextView>(R.id.cancel)
                            cancel.setOnClickListener {
                                   dialog?.dismiss()
                            }
                            dialog?.show()

                        false
                  }
            }
            else{
                  val view = holder as receivedViewHolder
                  view.reciveMessage.text = currentMessage.message
            }
      }
      override fun getItemViewType(position: Int): Int {
            val currentMessage = messages[position]
            if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.fromId)){
                  return sent
            }
            else{
                  return received
            }
      }
      class sentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            val sentMessage: TextView = itemView.findViewById<TextView>(R.id.txtsend_message)
      }
      class receivedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            val reciveMessage: TextView = itemView.findViewById<TextView>(R.id.txtrecieved_message)
      }
}