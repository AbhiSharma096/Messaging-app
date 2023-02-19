package com.example.mesenger

import android.provider.ContactsContract.CommonDataKinds.Email

data class User (
    var email: String? = null,
    var uid: String? = null,
    var url: String? = null,
    var username: String? = null

) {
    constructor() : this("","","","")



}
