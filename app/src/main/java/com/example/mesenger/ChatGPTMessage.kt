package com.example.mesenger

data class ChatGPTMessage (
      val message : String,
      val sentBy : String,
      val timestamp : String
){
      companion object{
            const val SENT_BY_ME = "sent_me"
            const val SENT_BY_BOT = "sent_bot"
      }
}