package com.example.intagram.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.intagram.Models.User
import com.example.intagram.R
import com.example.intagram.adapters.SearchAdapter
import com.example.intagram.databinding.FragmentSearchBinding
import com.example.intagram.databinding.SearchRvBinding
import com.example.intagram.utils.USER_NODE
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class SearchFragment : Fragment() {
lateinit var adapter: SearchAdapter
lateinit var binding: FragmentSearchBinding
var userList = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        adapter = SearchAdapter(requireContext(),userList)
        binding.rv.adapter = adapter

        Firebase.firestore.collection(USER_NODE).get().addOnSuccessListener {
            var tempList = ArrayList<User>()
            userList.clear()
            for (i in it.documents) {
                if (i.id.toString().equals(Firebase.auth.currentUser!!.uid.toString())) {
                } else {
                    var user: User = i.toObject<User>()!!
                    tempList.add(user)
                }
                userList.removeAll(tempList.toSet())
                userList.addAll(tempList)
                adapter.notifyDataSetChanged()
            }
        }

        binding.searchbtn.setOnClickListener{
           var text =  binding.searchView.editableText.toString()
            Log.d("text",text)
            Firebase.firestore.collection(USER_NODE).whereEqualTo("name",text).get().addOnSuccessListener {
                var tempList = ArrayList<User>()
                userList.clear()
                if (it.isEmpty) { }
                else {
                    for (i in it.documents) {
                        var user: User = i.toObject<User>()!!
                        tempList.add(user)
                    }
                    userList.addAll(tempList)
                    adapter.notifyDataSetChanged()

                }
            }
        }

        return binding.root
    }

    companion object {

    }
}