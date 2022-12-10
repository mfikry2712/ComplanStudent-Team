package com.example.complan.inputform

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.complan.menuandpager.Menu
import com.example.complan.authentication.LoginActivity
import com.example.complan.databinding.ActivityLaporanFasilitasBinding
import com.example.complan.dataclass.DataLaporanFasilitas
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class LaporanFasilitasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLaporanFasilitasBinding
    private lateinit var selectedImg: Uri
    private lateinit var db: DatabaseReference
    private lateinit var dbi: DatabaseReference
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLaporanFasilitasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseDatabase.getInstance().getReference("kode_sekolah")

        auth = Firebase.auth

        var testName = false
        var testLocation = false
        var testDesc = false


        binding.edtFasilitasName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                testName = s.isNotEmpty()
                binding.btnSendFasilitas.isEnabled = testName && testLocation && testDesc
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        binding.edtFasilitasLokasi.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                testLocation = s.isNotEmpty()
                binding.btnSendFasilitas.isEnabled = testName && testLocation && testDesc
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        binding.edtDescription.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                testDesc = s.isNotEmpty()
                binding.btnSendFasilitas.isEnabled = testName && testLocation && testDesc
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        binding.btnSelectImage.setOnClickListener { startGallery() }
        binding.btnSendFasilitas.setOnClickListener{ uploadImage() }
        binding.btnBack.setOnClickListener{
            startActivity(Intent(this@LaporanFasilitasActivity, Menu::class.java))
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun uploadImage(){
        val firebaseUser = auth.currentUser
        if (firebaseUser == null) {
            // Not signed in, launch the Login activity
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }else {
            val namaFoto = firebaseUser.uid + Date().time
            val storageReference = FirebaseStorage.getInstance().getReference("images/$namaFoto")

            storageReference.putFile(selectedImg).addOnSuccessListener {


                val friendlyMessage = DataLaporanFasilitas(
                    firebaseUser.uid,
                    binding.edtFasilitasName.text.toString(),
                    binding.edtFasilitasLokasi.text.toString(),
                    binding.edtDescription.text.toString(),
                    namaFoto,
                    Date().time,
                    false
                )
                dbi = FirebaseDatabase.getInstance().getReference("user").child(firebaseUser.uid)
                dbi.get().addOnSuccessListener {
                    val kdSekolah = it.child("schoolCode").value
                    db.child(kdSekolah.toString()).child("Laporan").child("Laporan Fasilitas").push()
                        .setValue(friendlyMessage) { error, _ ->
                            if (error != null) {
                                Toast.makeText(this, "gagal" + error.message, Toast.LENGTH_SHORT)
                                    .show()
                            } else {
                                dbi.child("Laporan").child("Laporan Fasilitas").push()
                                    .setValue(friendlyMessage) { error, _ ->
                                        if (error != null) {
                                            Toast.makeText(this, "gagal" + error.message, Toast.LENGTH_SHORT)
                                                .show()
                                        } else {
                                            Toast.makeText(this, "berhasil", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                            }
                        }
                }
                Toast.makeText(this@LaporanFasilitasActivity, "berhasil upload", Toast.LENGTH_SHORT)
                    .show()

            }.addOnFailureListener {
                Toast.makeText(this@LaporanFasilitasActivity, "failed upload", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            selectedImg = result.data?.data as Uri
            binding.btnSelectImage.setImageURI(selectedImg)
        }
    }
}