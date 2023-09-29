package com.example.intagram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.example.intagram.Models.Post
import com.example.intagram.Models.User
import com.example.intagram.databinding.ActivityPostBinding
import com.example.intagram.databinding.FragmentAddBinding
import com.example.intagram.utils.POST
import com.example.intagram.utils.POST_FOLDER
import com.example.intagram.utils.USER_NODE
import com.example.intagram.utils.USER_PROFILE_FOLDER
import com.example.intagram.utils.uploadImage
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class PostActivity : AppCompatActivity() {
    private lateinit var binding:ActivityPostBinding

    var imageUrl:String ?= null
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            uploadImage(uri, POST_FOLDER) {
                url->
                if (url != null) {
                    binding.selectimage.setImageURI(uri)
                    imageUrl=url
                }
            }
        }

        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.materialToolbar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)
        binding.selectimage.setOnClickListener {
            launcher.launch("image/*")
        }
        binding.postbtn.setOnClickListener {
            Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get()
                .addOnSuccessListener {
                    var user = it.toObject<User>()!!


                    val post: Post =
                        Post(imageUrl!!, binding.captionlayout.editText?.text.toString(),Firebase.auth.currentUser!!.uid,System.currentTimeMillis().toString())

                    Firebase.firestore.collection(POST).document().set(post).addOnSuccessListener {
                        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid).document()
                            .set(post)
                        startActivity(Intent(this, HomeActivity::class.java))
                        finish()
                    }
                }
        }
    }

}
