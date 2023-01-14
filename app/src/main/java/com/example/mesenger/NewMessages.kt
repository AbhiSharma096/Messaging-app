package com.example.mesenger


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle



class NewMessages : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_messages)
        //actionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title="Select User"
       // val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        //RecyclerView.Adapter




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