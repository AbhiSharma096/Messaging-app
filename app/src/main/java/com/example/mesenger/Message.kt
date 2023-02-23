package com.example.mesenger

class Message {
      var messageid : String? = null
      var message : String? = null
       var fromId : String? = null
      var imageurl : String? = null
       var timestamp : Long? = null
       var type : String? = null



      constructor() {}
       constructor(message: String?, fromId: String?, timestamp: Long?) {

              this.message = message
              this.fromId = fromId
              this.timestamp = timestamp



       }
}