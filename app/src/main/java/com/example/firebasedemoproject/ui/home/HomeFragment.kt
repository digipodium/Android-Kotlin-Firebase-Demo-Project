package com.example.firebasedemoproject.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebasedemoproject.R
import com.example.firebasedemoproject.adapter.MessageAdapter
import com.example.firebasedemoproject.databinding.FragmentHomeBinding
import com.example.firebasedemoproject.models.Message
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.util.ArrayList

class qHomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var msglist: ArrayList<Message>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        auth = Firebase.auth
        db = Firebase.firestore
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter: MessageAdapter = MessageAdapter(R.layout.message_card, load_message())
        binding.recyclerView.adapter =  adapter

    }

    private fun load_message(): ArrayList<Message> {
        msglist = arrayListOf()
        db.collection("messages").get()
            .addOnSuccessListener {
                it.forEach { data ->
                    msglist.add(data.toObject<Message>())
                }
            }
            .addOnFailureListener {
                Snackbar.make(binding.root, it.message.toString(), Snackbar.LENGTH_INDEFINITE)
                    .show()
            }
        return msglist
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}