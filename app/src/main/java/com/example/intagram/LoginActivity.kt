package com.example.intagram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.intagram.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.gobackbtn.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
            finish()
        }
        binding.signInBtn.setOnClickListener {
            if(binding.emailLayout.editText?.text.toString().equals("")  or
                binding.passwordLayout.editText?.text.toString().equals(""))
            {
                Toast.makeText(this,"Empty fields are not allowed ", Toast.LENGTH_LONG).show()
            }
            else {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(
                    binding.emailLayout.editText?.text.toString(),
                    binding.passwordLayout.editText?.text.toString()
                ).addOnSuccessListener {
                    startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                    finish()
                }
                    .addOnFailureListener {
                        Toast.makeText(this, "error",Toast.LENGTH_LONG).show()
                    }

            }
        }
    }
}