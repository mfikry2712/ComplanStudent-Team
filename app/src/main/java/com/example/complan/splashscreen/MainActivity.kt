package com.example.complan.splashscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.complan.R
import com.example.complan.authentication.InputProfileActivity
import com.example.complan.authentication.LoginActivity
import com.example.complan.menuandpager.Menu
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var authReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen_layout)

        auth = Firebase.auth

        val firebaseUser = auth.currentUser
        if (firebaseUser == null) {
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        }else{

        authReference = FirebaseDatabase.getInstance().getReference("user")

        Handler(Looper.getMainLooper()).postDelayed({
            authReference.child(firebaseUser.uid).get().addOnSuccessListener{
                if (it.exists()){
                    val o = Intent(this@MainActivity,  Menu::class.java)
                    startActivity(o)
                }else{
                    val o = Intent(this@MainActivity,  InputProfileActivity::class.java)
                    startActivity(o)
                }
            }.addOnFailureListener{
                Log.d("Error Failure",it.message.toString())
            }
            finish()
        }, delayMillis)

    }}

    companion object{
        const val delayMillis : Long = 2000
    }
}