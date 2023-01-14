package com.example.Profile

data class UserProfile(val name: String,
                        val bio: String,
                        val ProfilePicturePath: String?) {

    constructor(): this("","",null){

    }
}