package com.example.mesenger

import android.provider.ContactsContract.CommonDataKinds.Email

class User {
    var name : String? = null
    var email : String? = null
    var uid : String? = null
    var profileimage: String? = null

    constructor(){}

    constructor(name: String?, email: String?, uid: String?){
        this.name = name
        this.profileimage = profileimage
        this.email = email
        this.uid = uid

    }
}