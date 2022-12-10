package com.example.complan.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.complan.inputform.LaporanPersonActivity
import com.example.complan.inputform.LaporanFasilitasActivity
import com.example.complan.authentication.LoginActivity
import com.example.complan.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var db: DatabaseReference
    private lateinit var dbi: DatabaseReference

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        auth = Firebase.auth
        val firebaseUser = auth.currentUser
        if (firebaseUser == null) {
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
        }else{
            db = FirebaseDatabase.getInstance().getReference("user").child(firebaseUser.uid)
            db.get().addOnSuccessListener{ snapshot ->
                val kdSekolah = snapshot.child("schoolCode").value
                dbi = FirebaseDatabase.getInstance().getReference("kode_sekolah").child(kdSekolah.toString())
                dbi.get().addOnSuccessListener {
                   val schoolName = it.child("schoolName").value.toString()
                   val address = it.child("domicile").value.toString()

                    binding.tvNamaSekolah.text = schoolName
                    binding.tvAlamat.text = address
                }
            }
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnToLaporanPerson.setOnClickListener{
            startActivity(Intent(requireActivity(), LaporanPersonActivity::class.java))
        }

        binding.btnToLaporanFacility.setOnClickListener{
            startActivity(Intent(requireActivity(), LaporanFasilitasActivity::class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}