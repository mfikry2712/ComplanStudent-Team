package com.example.complan.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.complan.authentication.LoginActivity
import com.example.complan.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var db: DatabaseReference

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        val firebaseUser = auth.currentUser
        if (firebaseUser == null) {
            // Not signed in, launch the Login activity
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
            return
        }

        db = FirebaseDatabase.getInstance().getReference("user").child(firebaseUser.uid)

        db.get().addOnSuccessListener{
            val kdSekolah =  it.child("schoolCode").value.toString()
            val nmUser =  it.child("userName").value.toString()
            val jrUser =  it.child("userMajor").value.toString()

            binding.textView13.text = nmUser
            binding.textView14.text = kdSekolah
            binding.textView11.text = jrUser
        }

        binding.btnSignOut.setOnClickListener{
                auth.signOut()
                startActivity(Intent(requireActivity(), LoginActivity::class.java))
                requireActivity().finish()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}