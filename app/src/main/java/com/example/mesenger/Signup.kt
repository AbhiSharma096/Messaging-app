package com.example.mesenger

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
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
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.selects.select
import java.util.UUID



class Signup : AppCompatActivity() {

    private val cameraandmediaResultLanture : ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){ permission->
            permission.entries.forEach{
                val permissionname = it.key
                val isGranted = it.value
                if (isGranted){

                    val pickIntent = Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        openGalleryAction.launch(pickIntent)

                    }


                }
            }




    private lateinit var auth: FirebaseAuth
    private lateinit var email : EditText
    private lateinit var Password: EditText
    private lateinit var cPassword: EditText
    private lateinit var imagebtn: ImageButton
    val selectedphotouri: Uri? = null

    @SuppressLint("MissingInflatedId")
    val openGalleryAction: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
            if(result.resultCode == RESULT_OK && result.data!=null){
//                imagebtn.setImageBitmap()
                val uri = result.data?.data
                val bitmap  = MediaStore.Images.Media.getBitmap(contentResolver,uri)
                val bitmapDrawable= BitmapDrawable(bitmap)
                imagebtn.setImageBitmap(bitmap)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)



        val backbtn: Button = findViewById(R.id.back)
        val signupbtn: Button = findViewById(R.id.signup)

        auth = Firebase.auth
        email = findViewById(R.id.Email)
        Password = findViewById(R.id.etPassword2)
        cPassword = findViewById(R.id.etPassword3)
        imagebtn = findViewById(R.id.imagebutton)


        imagebtn.setOnClickListener {
            if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M && shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                showRationalDialog("Messenger Requires Camera permision",
                    "Camera cannot be used because Camera access is denied")

            }
            else {
                cameraandmediaResultLanture.launch(arrayOf(Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE))
            }

//            val intent = Intent(Intent.ACTION_PICK)
//            intent.type= "image/*"
//            startActivityForResult(intent,0)
        }



        signupbtn.setOnClickListener {
            val Username: EditText = findViewById(R.id.etUsername)
            var  edtemail = email.text.toString()
            var edtPassword = Password.text.toString()
            var cpassword = cPassword.text.toString()


            if (edtemail.isEmpty() || edtPassword.isEmpty()) {
                Toast.makeText(this, "Please enter email/password !", Toast.LENGTH_SHORT).show()
            } else if ( cpassword!= edtPassword){
                Toast.makeText(this, "Confermation Password must be same!", Toast.LENGTH_LONG).show()
            }
            else {
                    auth.createUserWithEmailAndPassword(edtemail, edtPassword).addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val i = Intent(this@Signup,ContactActivity::class.java)
                            startActivity(i)
                            finish()
                            upload_img_firebase()
                        }
                        else {
                            Toast.makeText(this,"Enter Proper Email/Password.",Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }



        backbtn.setOnClickListener{
                val i = Intent(this@Signup, MainActivity::class.java)
                startActivity(i)
                finish()
        }

        }


//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (resultCode==0 && resultCode== Activity.RESULT_OK && data!=null ){
//            val uri = data.data
//            val bitmap  = MediaStore.Images.Media.getBitmap(contentResolver,uri)
//            val bitmapDrawable= BitmapDrawable(bitmap)
//            imagebtn.setBackgroundDrawable(bitmapDrawable)
//        }
//    }




    private fun showRationalDialog(
        tittle: String,
        message: String,) {
        val builder : AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(tittle)
            .setMessage(message)
            .setPositiveButton("Cancel"){
                dialog,_->dialog.dismiss()
            }
        builder.create().show()

    }



    fun upload_img_firebase(){
        if (selectedphotouri==null) return
        val file = UUID.randomUUID().toString()

        val ref = FirebaseStorage.getInstance().getReference("/image/$file")
        ref.putFile(selectedphotouri!!)
    }
    }


