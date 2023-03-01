package com.example.Model

class PushNotification {
      var notification : NotificationData? = null
       var to : String? = null

       constructor() {}
       constructor(notification: NotificationData?, to: String?) {
             this.notification = notification
             this.to = to
       }
}