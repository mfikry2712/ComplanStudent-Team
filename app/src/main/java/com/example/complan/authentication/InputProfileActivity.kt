package com.example.complan.authentication

import com.example.complan.dataclass.UserProfile
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import com.example.complan.R
import com.example.complan.databinding.ActivityInputProfileBinding
import com.example.complan.menuandpager.Menu
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class InputProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInputProfileBinding
    private lateinit var userName : EditText
    private lateinit var userMajor : EditText
    private lateinit var schoolCode : EditText

    private lateinit var auth: FirebaseAuth
    private lateinit var dbi: FirebaseDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userName = findViewById(R.id.edtName)
        userMajor = findViewById(R.id.edtJurusan)
        schoolCode = findViewById(R.id.edtCode)

        auth = Firebase.auth
        val firebaseUser = auth.currentUser
        if (firebaseUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        dbi = Firebase.database

        var testName = false
        var testMajor = false
        var testCode = false

        userName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                testName = s.isNotEmpty()
                binding.btnSend.isEnabled = testName && testMajor && testCode
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        userMajor.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                testMajor = s.isNotEmpty()
                binding.btnSend.isEnabled = testName && testMajor && testCode
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        schoolCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                testCode = s.isNotEmpty()
                binding.btnSend.isEnabled = testName && testMajor && testCode
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })


        binding.btnSend.setOnClickListener{

            val dataStudent = UserProfile(
                userName.text.toString(),
                userMajor.text.toString(),
                schoolCode.text.toString()
            )

            dbi.reference.child(CHILD_SCHOOL).child(schoolCode.text.toString()).get().addOnSuccessListener {
                if (it.exists()){
                    dbi.reference.child(CHILD_USER).child(firebaseUser.uid).setValue(dataStudent){ error, _ ->
                        if (error != null) {
                            Toast.makeText(this, "Error" + error.message, Toast.LENGTH_SHORT).show()
                        } else {
                            dbi.reference.child(CHILD_SCHOOL).child(schoolCode.text.toString()).child(
                                CHILD_USER
                            )
                                .child(firebaseUser.uid).setValue(dataStudent) { errorStatus, _ ->
                                    if (errorStatus != null) {
                                        Toast.makeText(this, "Error" + errorStatus.message, Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                                        startActivity(Intent(this@InputProfileActivity, Menu::class.java))
                                        finish()
                                    }
                                }
                        }
                    }
                }else{
                    Toast.makeText(this, "Kode sekolah tidak ditemukan", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object{
        const val CHILD_USER = "user"
        const val CHILD_SCHOOL = "kode_sekolah"
    }
}