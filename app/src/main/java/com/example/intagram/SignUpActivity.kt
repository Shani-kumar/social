package com.example.intagram

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.intagram.Models.User
import com.example.intagram.databinding.ActivityMainBinding
import com.example.intagram.databinding.ActivitySignUpBinding
import com.example.intagram.utils.USER_NODE
import com.example.intagram.utils.USER_PROFILE_FOLDER
import com.example.intagram.utils.uploadImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var user: User
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            uploadImage(uri, USER_PROFILE_FOLDER) {
                user.image = it
                binding.profileImage.setImageURI(uri)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.textView.text = Html.fromHtml(
            "<font color=${Color.BLACK}>Already Have An Account </font>" +
                    "<font color=#2196F3> LogIn?</font>"
        )

        user = User()
        if (intent.hasExtra("Mode")) {
            if (intent.getIntExtra("Mode", -1) == 1) {
                binding.signUpBtn.text = "Update Profile"
                Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid)
                    .get()
                    .addOnSuccessListener {
                        user = it.toObject<User>()!!
                        if (!user.image.isNullOrEmpty()) {
                            Picasso.get().load(user.image).into(binding.profileImage)
                        }
                        binding.nameLayout.editText?.setText(user.name)
                        binding.emailLayout.editText?.setText(user.email)
                        binding.passwordLayout.editText?.setText(user.password)


                    }

            }
        }
        binding.signUpBtn.setOnClickListener {
            if (intent.hasExtra("Mode")) {
                if (intent.getIntExtra("Mode", -1) == 1) {
                    user.name = binding.nameLayout.editText?.text.toString()
                    user.email = binding.emailLayout.editText?.text.toString()
                    user.password = binding.passwordLayout.editText?.text.toString()
                    Firebase.firestore.collection(USER_NODE)
                        .document(Firebase.auth.currentUser!!.uid).set(user)
                        .addOnSuccessListener {
                            startActivity(
                                Intent(
                                    this@SignUpActivity,
                                    HomeActivity::class.java
                                )
                            )
                            finish()
                        }
                }
            }
            else
            {

                if (binding.nameLayout.editText?.text.toString().equals("") or
                    binding.emailLayout.editText?.text.toString().equals("") or
                    binding.passwordLayout.editText?.text.toString().equals("")
                ) {
                    Toast.makeText(this, "Empty fields are not allowed ", Toast.LENGTH_LONG).show()
                } else {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                        binding.emailLayout.editText?.text.toString(),
                        binding.passwordLayout.editText?.text.toString()
                    ).addOnCompleteListener { result ->
                        if (result.isSuccessful) {
                            user.name = binding.nameLayout.editText?.text.toString()
                            user.email = binding.emailLayout.editText?.text.toString()
                            user.password = binding.passwordLayout.editText?.text.toString()
                            Firebase.firestore.collection(USER_NODE)
                                .document(Firebase.auth.currentUser!!.uid).set(user)
                                .addOnSuccessListener {
                                    startActivity(
                                        Intent(this@SignUpActivity, HomeActivity::class.java)
                                    )
                                    finish()
                                }
                        } else {
                            Toast.makeText(
                                this,
                                result.exception?.localizedMessage ?: null,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }
        binding.add.setOnClickListener {
            launcher.launch("image/*")
        }
        binding.textView.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

    }


}