package com.example.complan.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.example.complan.databinding.ActivityRegisterBinding
import com.google.firebase.auth.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        var testMail = false
        var testPassword = false
        var testConfirmPassword = false

        binding.edtMail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                testMail = s.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(s).matches()
                binding.btnSendSignUp.isEnabled = testMail && testPassword && testConfirmPassword
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        binding.edtPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                testPassword = s.length >= 6
                binding.btnSendSignUp.isEnabled = testMail && testPassword && testConfirmPassword
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        binding.edtConfirmPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                testConfirmPassword = s.length >= 6
                binding.btnSendSignUp.isEnabled = testMail && testPassword && testConfirmPassword
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        binding.btnSendSignUp.setOnClickListener{
            val textMail = binding.edtMail.text.toString()
            val textPassword = binding.edtPassword.text.toString()
            val textConfirmPassword = binding.edtConfirmPassword.text.toString()

            if (textMail.isNotEmpty() && textPassword.isNotEmpty() && textConfirmPassword.isNotEmpty()){
                if(textConfirmPassword == textPassword){
                    signUp(textMail,textPassword)
                }else{
                    Toast.makeText(this, "Confirm Password ber beda", Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(this, "please fill in all fields!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signUp(email : String, password : String){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
            if (it.isSuccessful){
                Toast.makeText(this, "Successful registration!", Toast.LENGTH_LONG).show()
                startActivity(Intent(this@RegisterActivity, InputProfileActivity::class.java))
            }else{
                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }
}