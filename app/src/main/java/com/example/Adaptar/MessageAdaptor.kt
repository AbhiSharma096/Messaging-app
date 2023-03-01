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
import java.text.SimpleDateFormat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


private const val sent = 1
private const val received = 2



class MessageAdaptor(val context:Context,val messages: List<com.example.mesenger.Message>,senderRoom : String,receiverRoom: String):RecyclerView.Adapter <RecyclerView.ViewHolder?>(){



      var message : ArrayList<Message> = messages as ArrayList<Message>
      private var senderRoom = senderRoom
      private var receiverRoom = receiverRoom

      init {
            this.senderRoom = senderRoom
                this.receiverRoom = receiverRoom
      }



      override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            if (viewType == sent){
                  //inflate sent layout

                        val view = LayoutInflater.from(context).inflate(R.layout.send,parent,false)
                        return sentViewHolder(view)


            }
            else
            {

                        val view = LayoutInflater.from(context).inflate(R.layout.recieved,parent,false)
                        return receivedViewHolder(view)


            }
      }

      override fun getItemCount(): Int = messages.size


      @SuppressLint("SimpleDateFormat")
      override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

            val currentMessage = messages[position]
            if (holder.javaClass == sentViewHolder::class.java){

                  val view = holder as sentViewHolder
                  if (currentMessage.message == "This message was deleted"){
                        view.sentMessage.text = currentMessage.message
                        view.sentMessage.setTextColor(context.resources.getColor(R.color.red))
                        view.sentTime.text = ""
                  }
                  else{
                        view.sentMessage.text = currentMessage.message
                        val time = currentMessage.timestamp.toString()
                        var format = SimpleDateFormat("MMM-dd hh:mm a")
                        view.sentTime.text = format.format(time.toLong())
                       }

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
                                          .child("Messages")
                                          .child(it1)
                                          .setValue(currentMessage)
                                          .addOnSuccessListener {
                                                dialog?.dismiss()
                                          }

                              }
                                   currentMessage.messageid?.let { it1 ->
                                          FirebaseDatabase.getInstance().reference.child("Chats")
                                                 .child(receiverRoom)
                                                 .child("Messages")
                                                 .child(it1!!)
                                                 .setValue(currentMessage)
                                                 .addOnSuccessListener {
                                                 dialog?.dismiss()
                                                 }

                                   }
                        }
                        val delete = view.findViewById<TextView>(R.id.delete)
                            delete.setOnClickListener {
                                  currentMessage.message = "This message was deleted"
                                   currentMessage.messageid?.let { it1 ->
                                          FirebaseDatabase.getInstance().reference.child("Chats")
                                                 .child(senderRoom)
                                                 .child("Messages")
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

            else

            {
                  val view = holder as receivedViewHolder
                  if (currentMessage.message == "This message was deleted"){
                        view.reciveMessage.text = currentMessage.message
                        view.reciveMessage.setTextColor(context.resources.getColor(R.color.red))
                        view.sentTime.text = ""
                  }
                  else{
                        view.reciveMessage.text = currentMessage.message
                        val time = currentMessage.timestamp.toString()
                        val format = SimpleDateFormat("MMM-dd hh:mm a")
                        view.sentTime.text = format.format(time.toLong())
                  }
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
            val sentTime: TextView = itemView.findViewById<TextView>(R.id.time)
      }
      class againreceivedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            val reciveMessage: TextView = itemView.findViewById<TextView>(R.id.txtrecieved_message)
            val sentTime: TextView = itemView.findViewById<TextView>(R.id.time)
      }
      class receivedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            val reciveMessage: TextView = itemView.findViewById<TextView>(R.id.txtrecieved_message)
            val sentTime: TextView = itemView.findViewById<TextView>(R.id.time)
      }
}