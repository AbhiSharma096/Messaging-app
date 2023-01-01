package com.example.mesenger

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.provider.ContactsContract
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.selects.select
import java.net.URI
import java.text.SimpleDateFormat
import java.util.*


class Signup : AppCompatActivity() {


    private lateinit var auth: FirebaseAuth
    private lateinit var etname: EditText
    private lateinit var etemail: EditText
    private lateinit var Password: EditText
    private lateinit var cPassword: EditText
    private lateinit var imagebtn: ImageView
    private lateinit var backbtn: Button
    private lateinit var uri: Uri
    var customprogress: Dialog? =null
    private lateinit var signupbtn: Button
    private var storage = Firebase.storage
    //
    private val cameraandmediaResultLanture: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permission ->
            permission.entries.forEach {
                val permissionname = it.key
                val isGranted = it.value
                if (isGranted) {
                }
            }
        }



    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)



        backbtn = findViewById(R.id.back)
        signupbtn = findViewById(R.id.signup)
        storage = FirebaseStorage.getInstance()
        auth = Firebase.auth

        etname = findViewById(R.id.etUsername)
        etemail = findViewById(R.id.Email)
        Password = findViewById(R.id.etPassword2)
        cPassword = findViewById(R.id.etPassword3)
        imagebtn = findViewById(R.id.imagebutton)
        val name = etname.text.toString()



        val pickphoto = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                imagebtn.setImageURI(it)
                if (it != null) {
                    uri = it
                }

            }
        )

        // To get the permission to access storage
        imagebtn.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && shouldShowRequestPermissionRationale(
                    Manifest.permission.CAMERA
                )
            ) {
                // If permission were denied then show rational dialog
                showRationalDialog(
                    "Messenger Requires Storage Access Permission",
                    "Camera cannot be used because Storage access is denied"
                )

            } else {

                cameraandmediaResultLanture.launch(
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                )
            }
            pickphoto.launch("image/*")

        }


        // Create a user and go inside the app
        signupbtn.setOnClickListener {
            val Username: EditText = findViewById(R.id.etUsername)
            val edtemail = etemail.text.toString()
            val edtPassword = Password.text.toString()
            val cpassword = cPassword.text.toString()


            if (edtemail.isEmpty() || edtPassword.isEmpty()) {
                Toast.makeText(this, "Please enter email/password !", Toast.LENGTH_SHORT).show()
            } else if (cpassword != edtPassword) {
                // Check Weather both of the entered password are same or not
                Toast.makeText(this, "Confermation Password must be same!", Toast.LENGTH_LONG)
                    .show()
            } else {

                uploadimage()

                auth.createUserWithEmailAndPassword(edtemail, edtPassword)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            uploadimage()
                            val i = Intent(this@Signup, ContactActivity::class.java)
                            startActivity(i)
                            finish()

                        } else {
                            Toast.makeText(this, "Enter Proper Email/Password.", Toast.LENGTH_LONG)
                                .show()
                        }
                    }
            }
        }



        backbtn.setOnClickListener {
            val i = Intent(this@Signup, MainActivity::class.java)
            startActivity(i)
            finish()
        }

    }

    private fun uploadimage() {

          Showprogressdialog()
//        val formatter = SimpleDateFormat("DD/MM/yyyy", Locale.getDefault())
//        val now = Date()
//        val filename = formatter.format(now)
//        val storagRefrence = FirebaseStorage.getInstance().getReference("images/$filename")
//
//
//        storagRefrence.putFile(uri).addOnSuccessListener {
//            Toast.makeText(this, "Enter Proper Email/Password.", Toast.LENGTH_LONG)
//                .show()
//
//        }.addOnFailureListener {
//
//        }
        storage.getReference("images").child(System.currentTimeMillis().toString())
            .putFile(uri)
            .addOnSuccessListener { task->
                task.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener {
                        val userId = FirebaseAuth.getInstance().currentUser!!.uid

                        val mapImage = mapOf(
                            "url" to it.toString()
                        )
                        val databaseReference =
                            FirebaseDatabase.getInstance().getReference(("userImages"))
                        databaseReference.child(userId).setValue(mapImage)
                            .addOnSuccessListener {
                                Toast.makeText(this,"Successful",Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { error ->

                                Toast.makeText(this,it.toString(),Toast.LENGTH_SHORT).show()
                            }
                    }

            }


    }



//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode==100 && resultCode== RESULT_OK){
//            uri = data?.data!!
//            imagebtn.setImageURI(uri)
//        }
//    }


    private fun Showprogressdialog (){
        customprogress= Dialog(this@Signup)
        customprogress?.setContentView(R.layout.custom_progress)
        customprogress?.show()

    }
    private fun cancelprogressdialog(){
        if(customprogress!=null){
            customprogress?.dismiss()
            customprogress=null
        }
    }


    private fun showRationalDialog(
        tittle: String,
        message: String, ) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(tittle)
            .setMessage(message)
            .setPositiveButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
        builder.create().show()

    }


}


