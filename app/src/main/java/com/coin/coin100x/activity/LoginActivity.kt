package com.coin.coin100x.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.coin.coin100x.data.Login_Info
import com.coin.coin100x.databinding.ActivityLoginBinding
import com.coin.coin100x.sharedPrefrence.App
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var context: Context

    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var dbref: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        firebaseAuth = FirebaseAuth.getInstance()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        onButtonClick()

        if (firebaseAuth.currentUser != null) {
            startActivity(Intent(context, ProfileActivity::class.java))
            finish()
        }
        firebaseDatabase = FirebaseDatabase.getInstance()
        dbref = firebaseDatabase.getReference("Users")


    }

    fun onButtonClick() {
        binding.loginButton.setOnClickListener {
            val email = binding.userLoginEmail.text.toString()
            val password = binding.userLoginPassword.text.toString()

            if (email.isEmpty()) {
                binding.userLoginEmail.error = "enter valid email"
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                binding.userLoginPassword.error = "enter password"
                return@setOnClickListener
            }
            userSignIn(email = email, password = password)

        }
        binding.registerButton.setOnClickListener {
            startActivity(Intent(context, UserRegisterActivity::class.java))
        }

    }

    fun userSignIn(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this@LoginActivity) {
                if (it.isSuccessful) {
                    saveLoginDataToDatabse(email, password, App.AMOUNT)
                    startActivity(
                        Intent(context, ProfileActivity::class.java)
                    )
                    finish()
                    Log.e("TAG", "userSignIn: user login successfully ${it}")
                }
            }

    }

    fun saveLoginDataToDatabse(email: String, password: String, amount: String) {

        val uid = firebaseAuth.uid
        if (uid != null) {
            App.UUID = uid
            Log.e("TAG", "saveLoginDataToDatabse: UUID ${App.UUID}")

        }

        val loginInfo = Login_Info(email = email, password = password, amount = amount)
        dbref.child("Login Info").child(uid.toString()).setValue(loginInfo).addOnCompleteListener {
            Log.e("TAG", "saveLoginDataToDatabse: $it ")
        }.addOnFailureListener {
            Log.e("TAG", "saveLoginDataToDatabse: ${it.message}")
        }

    }


}