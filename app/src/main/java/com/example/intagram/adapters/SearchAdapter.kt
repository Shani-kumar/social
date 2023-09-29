package com.example.intagram.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.intagram.R
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.intagram.Models.User
import com.example.intagram.databinding.PostRvBinding
import com.example.intagram.databinding.SearchRvBinding

class SearchAdapter(var context: Context, var userList : ArrayList<User>):RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: SearchRvBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       var binding = SearchRvBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return userList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(userList.get(position).image).placeholder(R.drawable.user).into(holder.binding.profileImage)
        holder.binding.name.text = userList.get(position).name
    }
}