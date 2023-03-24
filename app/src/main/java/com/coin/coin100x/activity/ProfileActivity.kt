package com.coin.coin100x.activity


import android.content.Context
import android.content.Intent
import android.content.res.Resources.Theme
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.coin.coin100x.data.AddMoneyToClientModel
import com.coin.coin100x.data.UsersTotalBalance
import com.coin.coin100x.databinding.ActivityProfileBinding
import com.coin.coin100x.sharedPrefrence.App
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import org.checkerframework.checker.units.qual.Current
import kotlin.concurrent.thread

class ProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivityProfileBinding
    lateinit var context: Context
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var dbRef: DatabaseReference
    val db = Firebase.firestore
    val currentUser = FirebaseAuth.getInstance().currentUser?.uid
    lateinit var data: Intent
    private var imageUri: Uri? = null
    private var sum = 0
    private var bal = 0
    private var remaining_bal = 0
    private var total = 0
    var clientAmount = 0
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
        binding.name.text = currentUser

        onClick()
        binding.tvShowBalance.setOnClickListener {
            showBalance()
        }
        CoroutineScope(Dispatchers.IO).launch {
            Log.e("TAG", "onCreate: ${Thread.currentThread().name} ")
            getMoneyFromDatabase()
            getUserBalance()
            // getSenderTransaction()
            // getDebitData()

        }

        MainScope().launch(Dispatchers.Default) {
            Log.e("TAG", "onCreate: main ${Thread.currentThread().name}")
        }
        val clientAmount = intent.getStringExtra(App.CLIENT_AMOUNT)
        Log.e("TAG", "onCreate: client amount $clientAmount")

        if (!clientAmount.isNullOrEmpty())
            binding.Balance.text = clientAmount


    }


    override fun onStart() {
        super.onStart()
        Log.e("TAG", "onStart: executed ")
        //getDataFromDatabase()
        //getData()
        //getMoneyFromDatabase()
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
                    .putExtra("TRANSACTION", "transactionHistory")
            )
        }
        binding.tvPortfolio.setOnClickListener {
            startActivity(
                Intent(context, ReceiptActivity::class.java)
                    .putExtra("PORTFOLIO", "portfolio")
            )
        }
        binding.tvReceipt.setOnClickListener {
            startActivity(
                Intent(context, ReceiptActivity::class.java)
                    .putExtra("RECEIPT", "receipt")
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

    private fun getMoneyFromDatabase() {
        db.collection("MoneyAdded").document("ReceiverAmount")
            .collection(FirebaseAuth.getInstance().uid.toString())
            .get().addOnCompleteListener {
                for (db in it.result) {
                    val senderid = db.data["sender_id"].toString()
                    val c_id = db.data["client_id"].toString()
                    val c_name = db.data["client_name"].toString()
                    val c_amount = db.data["client_amount"].toString()
                    val c_remain_amount = db.data["client_remaining_amount"].toString()
                    val time = db.data["current_time"].toString()
                    val TOT = db.data["type_of_transaction"].toString()
                    //   totalUserBalance(senderid, c_id, c_name, c_amount, c_remain_amount, time, TOT)
                    Log.e("TAG", "getMoneyFromDatabase: ${db.data["client_amount"].toString()}")
                    Log.e(
                        "TAG",
                        "getMoneyFromDatabase: ${db.data["type_of_transaction"].toString()}",
                    )
                    total += Integer.parseInt(c_amount)
                    Log.e("TAG", "getMoneyFromDatabase: total balance = $total")
                    // setCurrentBalance(c_name, total.toString(),"0", c_id, senderid)
                    //getDebitData(total)
                }
            }
    }

    /* private fun totalUserBalance(
         senderid: String,
         c_id: String,
         c_name: String,
         c_amount: String,
         c_remain_amount: String,
         time: String,
         typeOfTransaction: String
     ) {
         remain = App.getInt(context, App.REMANING_BALANCE)
         Log.e("TAG", "totalUserBalance: remaining $remain ")
         sum += remain
         // App.setInt(context, App.BALANCE, sum)

         val totalBalance = mapOf(
             "sender_id" to senderid,
             "client_id" to c_id,
             "client_name" to c_name,
             "client_amount" to c_amount,
             "client_remaining_amount" to c_remain_amount,
             "current_time" to time,
             "type_of_transaction" to typeOfTransaction,
             "sum" to sum
         )

         db.collection("MoneyAdded").document("TotalBalance").set(totalBalance)
             .addOnCompleteListener {
                 Log.e("TAG", "totalUserBalance: success $totalBalance ")
             }.addOnFailureListener { Log.e("TAG", "totalUserBalance: failed ") }
     }*/

    /*private fun getSenderTransaction() {
        db.collection("SenderMoney").document("CurrentTransaction")
            .collection(FirebaseAuth.getInstance().uid.toString()).document("CurrentBalance")
            .get().addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.e(
                        "TAG",
                        "getSenderTransaction: ${
                            it.result.data?.get("client_amount")?.toString()
                        }",
                    )
                    Log.e(
                        "TAG",
                        "getSenderTransaction: ${
                            it.result.data?.get("current_balance")?.toString()
                        }",
                    )
                    sum =Integer.parseInt(it.result.data?.get("client_amount")?.toString()!!)
                    Log.e("TAG", "getSenderTransaction: total balance su  ====== $sum ")


                }
            }
    }*/


    /* private fun getDebitData() {
         db.collection("MoneyAdded").document("Debited_Amount")
             .collection(FirebaseAuth.getInstance().uid.toString())
             .document("CurrentBalance").get().addOnCompleteListener {
                 if (it.result.exists()) {
                     Log.e(
                         "TAG",
                         "getDebitData: remaining balnace${
                             it.result.data?.get("remaining_balance").toString()
                         } ",
                     )
                     binding.Balance.text = it.result.data?.get("remaining_balance").toString()


                     Log.e(
                         "TAG",
                         "getDebitData current balance :${
                             it.result.data?.get("total_balance").toString()
                         } ",
                     )
                     bal = Integer.parseInt(it.result.data?.get("total_balance").toString())
                     sum = total - bal
                     Log.e("TAG", "getDebitData: sum is = $sum ")

                     remaining_bal = sum + Integer.parseInt(
                         it.result.data?.get("remaining_balance").toString()
                     )
                     Log.e(
                         "TAG",
                         "getDebitData: adding sum and remaining balance is = $remaining_bal",
                     )
                     App.setInt(context, App.WALLET_AMOUNT, remaining_bal)

                 }
             }.addOnFailureListener {
                 Log.e("TAG", "setDebitData: failed")
             }
     }*/

    private fun showBalance() {
        getUserBalance()
    }

    private fun getUserBalance() {
        db.collection("UsersBalance").document(FirebaseAuth.getInstance().uid.toString()).get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val c_balance = it.result.data?.get("user_balance").toString()
                    Log.e("TAG", "getUserBalance: $c_balance")
                    binding.Balance.text = c_balance

                }

            }.addOnFailureListener {
                Log.e("TAG", "getUserBalance: ${it.message}")
            }
    }


}