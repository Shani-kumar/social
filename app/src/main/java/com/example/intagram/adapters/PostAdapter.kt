package com.example.intagram.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideExtension
import com.example.intagram.Models.Post
import com.example.intagram.Models.User
import com.example.intagram.R
import com.example.intagram.databinding.PostRvBinding
import com.example.intagram.utils.USER_NODE
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.lang.Exception


class PostAdapter (
    var context: Context,
   var postList: ArrayList<Post>
): RecyclerView.Adapter<PostAdapter.MyHolder>(){

    inner class MyHolder(var binding: PostRvBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        var binding= PostRvBinding.inflate(LayoutInflater.from(context),parent,false)
        return MyHolder(binding)
    }

    override fun getItemCount(): Int {
        return  postList.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        try{
            Firebase.firestore.collection(USER_NODE).document(postList.get(position).Uid).get().addOnSuccessListener {
                var user=it.toObject<User>()
                Glide.with(context).load(user!!.image).placeholder(R.drawable.user).into(holder.binding.profileImage)
                holder.binding.name.text = user.name
            }
        }
        catch (e:Exception){

        }

        Glide.with(context).load(postList.get(position).postUrl).placeholder(R.drawable.user).into(holder.binding.postimage)
        holder.binding.time.text = postList.get(position).time
        holder.binding.cscaption.text =postList.get(position).caption
        holder.binding.like.setOnClickListener {
            holder.binding.like.setImageResource(R.drawable.heart)
        }
    }
}