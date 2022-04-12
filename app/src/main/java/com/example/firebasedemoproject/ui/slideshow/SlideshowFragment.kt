package com.example.firebasedemoproject.ui.slideshow

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.firebasedemoproject.AuthActivity
import com.example.firebasedemoproject.databinding.FragmentSlideshowBinding
import com.example.firebasedemoproject.models.Message
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SlideshowFragment : Fragment() {
    private var _binding: FragmentSlideshowBinding? = null
    private val binding get() = _binding!!

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root
        auth = Firebase.auth
        db = Firebase.firestore
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnShare.setOnClickListener {
            val msg = binding.editMsg.text.toString()
            val data = auth.currentUser?.displayName?.let { it1 -> Message(msg, it1) }
            data?.let { it1 ->
                db.collection("messages").add(it1).addOnSuccessListener {
                    Snackbar.make(binding.root,"message sent",Snackbar.LENGTH_LONG).show()
                    binding.editMsg.text.clear()
                }.addOnFailureListener {
                    Snackbar.make(binding.root, it.message.toString(),Snackbar.LENGTH_LONG).show()
                }
            }
        }

    }

    override fun onStart() {
        super.onStart()
        updateUI(auth.currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser == null) {
            val i = Intent(requireContext(), AuthActivity::class.java)
            startActivity(i)
            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}