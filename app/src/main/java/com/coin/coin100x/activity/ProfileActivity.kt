package com.coin.coin100x.activity


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.coin.coin100x.databinding.ActivityProfileBinding
import com.coin.coin100x.sharedPrefrence.App
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivityProfileBinding
    lateinit var context: Context
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var dbRef: DatabaseReference
    val db = Firebase.firestore
    val currentUser = FirebaseAuth.getInstance().currentUser?.uid
    lateinit var data: Intent
    private var imageUri: Uri? = null
    var sum = 0
    //var model = AddMoneyToClientModel()
    // val sharedPref: com.coin.coin100x.sharedPrefrence.App? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this
        supportActionBar?.hide()
        firebaseDatabase = FirebaseDatabase.getInstance()
        dbRef = firebaseDatabase.getReference("Users")




        onClick()
        getDataFromDatabase()


    }


    override fun onStart() {
        super.onStart()
        Log.e("TAG", "onStart: executed ")
        // getDataFromDatabase()
        //getData()
    }

    private fun onClick() {
        binding.tvPurchase.setOnClickListener {
            startActivity(Intent(context, MainActivity::class.java))
        }
        binding.btShowWinner.setOnClickListener {
            startActivity(Intent(context, LuckyWinnerActivity::class.java))
        }
        binding.tvTransactionHistory.setOnClickListener {
            startActivity(
                Intent(context, ReceiptActivity::class.java)
                    .putExtra("RECEIPT", "receipt")
            )
        }
        binding.tvPortfolio.setOnClickListener {
            startActivity(
                Intent(context, ReceiptActivity::class.java)
                    .putExtra("PORTFOLIO", "portfolio")
            )
        }


        binding.profileImage.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 100)

        }

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 100) {
            imageUri = data?.data
            binding.profileImage.setImageURI(imageUri)
        }
    }


    /*private fun getDataFromDatabase() {
        Log.e("TAG", "getDataFromDatabase: ")
        dbRef.child("MoneyAdded").child("ReceiverAmount")
            .child(FirebaseAuth.getInstance().uid.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.e("TAG", "getDataFromDatabase: datacjsnge))))) ")
                    for (data in snapshot.children) {

                        val model = data.getValue(AddMoneyToClientModel::class.java)
                        Log.e(
                            "TAG",
                            "getDataFromDatabase: datacjsnge************  ${model?.amount} name = ${model?.clientName.toString()} "
                        )
                        binding.name.text = model?.clientName.toString()
                        App.setString(context, "SENDER_ID", model?.senderId.toString())
                        App.setString(context, "CLIENT_NAME", model?.clientName.toString())

                        Log.e(
                            "TAG",
                            "onDataChange: ${model?.clientId.toString()} == ${FirebaseAuth.getInstance().uid.toString()}"
                        )

                        if (model?.clientId.toString() == FirebaseAuth.getInstance().uid.toString()
                        ) {

                            if (!model?.remainingAmount.isNullOrEmpty()) {
                                sum += Integer.parseInt(model?.remainingAmount.toString())
                                binding.tvBalance.text = sum.toString()

                                App.setInt(context, "BAL", sum)

                                Log.e(
                                    "TAG",
                                    "onDataChange: ${model?.clientId.toString()} == ${FirebaseAuth.getInstance().uid.toString()} and amount = $sum"
                                )
                            } else
                                binding.tvBalance.text = "0"
                        } else
                            binding.tvBalance.text = "0"

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("TAG", "onDataChange: ${error.message}")
                }

            })

    }*/


    /* private fun getData() {
         dbRef.child("RegisteredUser")
             .child(FirebaseAuth.getInstance().uid.toString())
             .addListenerForSingleValueEvent(object : ValueEventListener {
                 override fun onDataChange(snapshot: DataSnapshot) {
                     if (snapshot.exists()) {
                         val data = snapshot.getValue(Register_Info::class.java)
                         Log.e("TAG", "onDataChange: ${data?.userName.toString()} ")
                         binding.name.text = data?.userName.toString()
                         App.setString(context, "USER_NAME", data?.userName.toString())
                     }
                 }

                 override fun onCancelled(error: DatabaseError) {
                     TODO("Not yet implemented")
                 }

             })
     }*/


    private fun getDataFromDatabase() {
        val ref =
            db.collection("MoneyAdded").document(currentUser.toString())
        ref.get().addOnSuccessListener {
            binding.name.text = it.data?.get("client_name").toString()
            if (currentUser.toString() == it.data?.get("client_id")) {
                binding.tvBalance.text = it.data?.get("client_amount").toString()
                App.setString(context, "USER_NAME", it.data?.get("client_name").toString())
                App.setString(context, "USER_AMOUNT", it.data?.get("client_amount").toString())


            } else {
                Log.e(
                    "TAG",
                    "getDataFromDatabase: ${it.data?.get("client_id")} and ${it.data?.get("client_name")} and ${
                        it.data?.get("client_amount")
                    }"
                )
            }

        }.addOnFailureListener {
            Log.e("TAG", "getDataFromDatabase: ${it.message} ")
        }

    }


}