package com.coin.coin100x.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.coin.coin100x.activity.ProfileActivity
import com.coin.coin100x.data.AddMoneyToClientModel
import com.coin.coin100x.data.UsersPurchaseItemModel
import com.coin.coin100x.databinding.FragmentBuyBinding
import com.coin.coin100x.repositories.NoteViewModel
import com.coin.coin100x.sharedPrefrence.App
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.log

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"

/**
 * A simple [Fragment] subclass.
 * Use the [BuyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BuyFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var param3: String? = null

    lateinit var bind: FragmentBuyBinding

    var user_id = ""
    var user_name = ""
    var isChecked = false
    var sum = 0
    var item_count = 1
    var remaining_amount = 0


    val firebaseDatabase = FirebaseDatabase.getInstance()
    lateinit var dbRef: DatabaseReference
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            param3 = it.getString(ARG_PARAM3)

            Log.e("TAG", "onCreate: param1 =$param1 and param 2 = $param2 img url = $param3")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        bind = FragmentBuyBinding.inflate(layoutInflater, container, false)
        return bind.root
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String, param3: String) = BuyFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, param1)
                putString(ARG_PARAM2, param2)
                putString(ARG_PARAM3, param3)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbRef = firebaseDatabase.getReference("Users")

        CoroutineScope(Dispatchers.IO).launch {
            getUserDetail()
        }

        bind.itemName.text = param1.toString()
        bind.tvItemPrice.text = param2.toString()
        bind.qty.setText("1")


        var amount = App.getString(requireContext(), "USER_AMOUNT")
        val sender_id = App.getString(requireContext(), "SENDER_ID")
        user_name = App.getString(requireContext(), "USER_NAME")
        user_id = App.getString(requireContext(), "USER_ID")
        var current_amount = App.getString(requireContext(), "USER_REMAINING_AMOUNT")

        val wallet = App.getInt(requireContext(), App.WALLET_AMOUNT)
        Log.e("TAG", "onViewCreated: balance = $wallet ")

        //val wallet = App.getInt(requireContext(), App.WALLET_AMOUNT)

        bind.points.text = wallet.toString()
        Log.e("TAG", "onViewCreated: wallet in buy fragment $wallet  ")

        //addProductQty(param1.toString(), Integer.parseInt(param2.toString()), wallet_amount)


        Glide.with(requireContext()).load(param3.toString()).into(bind.image)
        /* totalSum(
             sender_id,
             user_name,
             param1.toString(),
             Integer.parseInt(param2.toString()),
             wallet,
         )*/

        App.setString(requireContext(), "PRODUCT_NAME", (param1.toString()))

    }

    private fun totalSum(
        userId: String,
        userName: String,
        userEmail: String,
        userNumber: String,
        item_name: String,
        item_price: Int,
        wallet_amount: Int
    ) {
        val qty = bind.qty.text.toString()
        Log.e("TAG", "totalSum: quantity = $qty")

        if (qty.isNotEmpty()) {
            val count = Integer.parseInt(qty)
            if (count < 1) {
                bind.qty.setText("1")
                bind.tvItemPrice.text = sum.toString()
                bind.totalPrice.text = sum.toString()
                Log.e("TAG", "totalSum:   enter a valid quantity $count")

            } else {
                sum = item_price * count
                bind.tvItemPrice.text = sum.toString()
                bind.totalPrice.text = sum.toString()

                if (wallet_amount >= sum) {
                    remaining_amount = wallet_amount - sum
                    bind.points.text = remaining_amount.toString()
                    Log.e("TAG", "totalSum: reamount = $remaining_amount")

                    //addDataToDatabase(sender_id,item_name,item_price.toString(),count.toString(),wallet.toString(),wallet_amount.toString())
                    addProductsToDatabase(
                        FirebaseAuth.getInstance().uid.toString(),
                        userName,
                        item_name,
                        item_price.toString(),
                        count.toString(),
                        wallet_amount.toString(),
                        remaining_amount.toString()

                    )
                    setDebitData(
                        userName,
                        FirebaseAuth.getInstance().uid.toString(),
                        wallet_amount.toString(),
                        remaining_amount.toString(),
                        "sender_id"
                    )
                    bind.btBuy.setOnClickListener {
                        updateUserBalance(
                            userId,
                            userName,
                            userEmail,
                            userNumber,
                            remaining_amount.toString()
                        )
                    }


                    /* userTotalBalance(
                         wallet_amount.toString(),
                         param1.toString(),
                         user_name,
                         remaining_amount,
                         "",
                         sender_id,
                         wallet_amount.toString()
                     )*/


                    /*  updateDataToDatabase(
                          sender_id,
                          user_id,
                          user_name,
                          amount.toString(),
                          re_amount
                      )*/

                } else {
                    Log.e("TAG", "totalSum: not enough balance ")
                    bind.points.text = wallet_amount.toString()
                }
            }
        }
    }


    fun addProductsToDatabase(
        user_id: String,
        user_name: String,
        product_name: String,
        product_price: String,
        product_qty: String,
        available_bal: String,
        remaining_bal: String
    ) {

        val upim = UsersPurchaseItemModel(
            user_id,
            user_name,
            product_name,
            product_price,
            product_qty,
            available_bal,
            remaining_bal,
            true
        )
        dbRef.child("User_Purchase_Item").child(FirebaseAuth.getInstance().uid.toString())
            .push()
            .setValue(upim).addOnCompleteListener {
                if (it.isSuccessful)
                    Toast.makeText(
                        requireContext(),
                        "product puchased Successfully",
                        Toast.LENGTH_SHORT
                    ).show()

            }.addOnFailureListener {
                Log.e("TAG", "addProductsToDatabase: ${it.message} ")

            }
    }


    private fun setDebitData(
        clientName: String,
        clientId: String,
        totalBalance: String,
        remaningBalance: String,
        senderId: String
    ) {
        val debitData = mapOf(
            "client_name" to clientName,
            "client_id" to clientId,
            "total_balance" to totalBalance,
            "remaining_balance" to remaningBalance,
            "sender_id" to senderId,
            "type_of_transaction" to "Debit"
        )

        db.collection("MoneyAdded").document("Debited_Amount")
            .collection(FirebaseAuth.getInstance().uid.toString())
            .document("CurrentBalance").set(debitData).addOnCompleteListener {
                setCurrentBalance(clientName, totalBalance, remaningBalance, clientId, senderId)

            }.addOnFailureListener {
                Log.e("TAG", "setDebitData: failed")
            }
    }

    private fun setCurrentBalance(
        u_name: String,
        current_bal: String,
        remaining_balance: String,
        u_id: String,
        sender: String
    ) {
        val updatedUserTotalBalance = mapOf(
            "user_name" to u_name,
            "current_balance" to current_bal,
            "remaining_balance" to remaining_balance,
            "user_id" to u_id,
            "sender_id" to sender
        )

        db.collection("User_Current_Balance").document(FirebaseAuth.getInstance().uid.toString())
            .set(updatedUserTotalBalance).addOnCompleteListener {
                Log.e("TAG", "updateCurrentBalance: update successfully ")

            }.addOnFailureListener {
                Log.e("TAG", "updateCurrentBalance: update failed ")
            }


    }

    /*  private fun getSenderTransaction() {
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
                      totalSum(
                          "sender_id",
                          user_name,
                          param1.toString(),
                          Integer.parseInt(param2.toString()),
                          Integer.parseInt(it.result.data?.get("current_balance")?.toString()!!),
                      )
                      bind.points.text = it.result.data?.get("current_balance")?.toString()

                  }
              }
      }
  */

/*    private fun updateSenderTransaction(
        senderid: String,
        c_id: String,
        c_name: String,
        c_amount: String,
        // c_remain_amount: String,
        balance: String,
        time: String
    ) {
        val totalBalance = mapOf(
            "sender_id" to senderid,
            "client_id" to c_id,
            "client_name" to c_name,
            "client_amount" to 0,
            //"client_remaining_amount" to c_remain_amount,
            "current_balance" to balance,
            "current_time" to time,
            "type_of_transaction" to "credit"
        )

        db.collection("SenderMoney").document("CurrentTransaction").collection(c_id)
            .document("CurrentBalance")
            .update(totalBalance).addOnSuccessListener {
                Log.e("TAG", "setSenderTransaction: success $totalBalance")
            }.addOnFailureListener {
                Log.e("TAG", "setSenderTransaction: failed ")
            }


    }*/

    private fun getUserDetail() {
        db.collection("UsersBalance").document(FirebaseAuth.getInstance().uid.toString()).get()
            .addOnCompleteListener {
                Log.e("TAG", "getUserDetail: ")
                if (it.isSuccessful) {
                    val bal = it.result.data?.get("user_balance").toString()
                    val userId = it.result.data?.get("user_id").toString()
                    val userName = it.result.data?.get("user_name").toString()
                    val userEmail = it.result.data?.get("user_email").toString()
                    val userNumber = it.result.data?.get("user_phoneNumber").toString()
                    bind.points.text = bal
                    totalSum(
                        userId,
                        user_name,
                        userEmail,
                        userNumber,
                        param1.toString(),
                        Integer.parseInt(param2.toString()),
                        Integer.parseInt(bal)
                    )

                }
            }.addOnFailureListener {

        }
    }

    private fun updateUserBalance(
        userId: String,
        userName: String,
        userEmail: String,
        userNumber: String,
        currentBalance: String
    ) {
        val userBalance = mapOf(
            "user_id" to userId,
            "user_name" to userName,
            "user_email" to userEmail,
            "user_phoneNumber" to userNumber,
            "user_balance" to currentBalance
        )
        db.collection("UsersBalance").document(userId)
            .update(userBalance).addOnCompleteListener {
                Log.e("TAG", "addDataToDatabase: $it")
            }.addOnFailureListener {
                Log.e("TAG", "addDataToDatabase: ${it.message}")
            }
    }


}