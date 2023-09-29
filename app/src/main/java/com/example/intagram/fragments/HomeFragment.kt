package com.example.intagram.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.intagram.Models.Post
import com.example.intagram.R
import com.example.intagram.adapters.PostAdapter
import com.example.intagram.databinding.FragmentHomeBinding
import com.example.intagram.utils.POST
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment() {
  private lateinit var binding: FragmentHomeBinding
  private var postList = ArrayList<Post>()
    private lateinit var adapter: PostAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        adapter = PostAdapter(requireContext(),postList)
        binding.rvpost.layoutManager =LinearLayoutManager(requireContext())
        binding.rvpost.adapter =adapter
        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.materialToolbar2)


        Firebase.firestore.collection(POST).get().addOnSuccessListener {
            var tempList =ArrayList<Post>()
            postList.clear()
            for (i in it.documents){
                var post: Post? = i.toObject<Post>()
                if (post != null) {
                    tempList.add(post)
                }

            }
            postList.addAll(tempList)
            adapter.notifyDataSetChanged()

        }
        return  binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.option_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    companion object {}
}