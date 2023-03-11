package com.coin.coin100x.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.coin.coin100x.data.Register_Info
import com.coin.coin100x.data.UsersTotalBalance
import com.coin.coin100x.databinding.ActivityUserRegisterBinding
import com.coin.coin100x.sharedPrefrence.App
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserRegisterActivity : AppCompatActivity() {

    lateinit var binding: ActivityUserRegisterBinding
    lateinit var context: Context
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var dbRef: DatabaseReference
    val db= Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        firebaseAuth = FirebaseAuth.getInstance()
        binding = ActivityUserRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        onButtonClick()

        firebaseDatabase = FirebaseDatabase.getInstance()
        dbRef = firebaseDatabase.getReference("Users")

    }

    fun onButtonClick() {
        binding.submitButton.setOnClickListener {
            val name = binding.userName.text.toString()
            val email = binding.userEmail.text.toString()
            val mobile_number = binding.userMobileNumber.text.toString()
            val password = binding.userPassword.text.toString()
            val confirm_password = binding.confirmPassword.text.toString()

            if (name.isEmpty()) {
                binding.userName.error = "enter name"
                return@setOnClickListener
            }
            if (email.isEmpty()) {
                binding.userEmail.error = "enter email"
                return@setOnClickListener

            }
            if (mobile_number.isEmpty()) {
                binding.userMobileNumber.error = "enter mobile number"
                return@setOnClickListener

            }
            if (password.isEmpty()) {
                binding.userPassword.error = "enter password"
                return@setOnClickListener
            }
            if (!password.equals(confirm_password)) {
                binding.confirmPassword.error = "Re-type your password"
                return@setOnClickListener
            }
            userSignup(
                email = email,
                password = password,
                name = name,
                mobile_number = mobile_number,
                "0"
            )

        }
    }

    fun userSignup(
        email: String,
        password: String,
        name: String,
        mobile_number: String,
        balance: String
    ) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(
                this@UserRegisterActivity
            ) {
                if (it.isSuccessful) {
                    addDataToDatabase(
                        firebaseAuth.uid.toString(),
                        name = name,
                        email = email,
                        mobile_number,
                        "0"
                    )

                    App.setString(context, "USER_NAME", name)
                    App.setString(context, "USER_EMAIL", email)
                    App.setString(context, "USER_NUMBER", mobile_number)

                    startActivity(Intent(context, LoginActivity::class.java))
                    finish()
                    Log.e("TAG", "userSignup: SuccessFul ${it.isSuccessful} ")
                } else
                    Log.e("TAG", "userSignup: task is not successfull $it")
            }
    }

    private fun addDataToDatabase(
        uid: String,
        name: String,
        email: String,
        phone_number: String,
        balance: String

    ) {

        // com.coin.coin100x.sharedPrefrence.App().setString(context, App.UUID, uId.toString())
        val userInfo = Register_Info(uid, name, email, phone_number, balance)
        dbRef.child("RegisteredUser").child(uid).setValue(userInfo)

        val userBalance = mapOf("user_id" to uid,
        "user_name" to name,
        "user_email" to email,
        "user_phoneNumber" to phone_number,
        "user_balance" to balance)
       db.collection("UsersBalance").document(FirebaseAuth.getInstance().uid.toString()).set(userBalance).addOnCompleteListener {
           Log.e("TAG", "addDataToDatabase: success", )
       }.addOnFailureListener {
           Log.e("TAG", "addDataToDatabase: failed", )
       }
    }

}