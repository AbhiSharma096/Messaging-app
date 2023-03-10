package com.example.mesenger


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class NewMessages : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_messages)
        //actionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title="Select User"
       // val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        //RecyclerView.Adapter
//          var recyclerview_newmessage :RecyclerView
//          recyclerview_newmessage = findViewById(R.id.RecycleView_SelectUser)
//
//          recyclerview_newmessage.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL,false)




    }

    override fun onSupportNavigateUp(): Boolean {
        OnBackPressed()
        return super.onSupportNavigateUp()
    }

   fun OnBackPressed() {
        val intent = Intent(this, ContactActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}