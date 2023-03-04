package com.coin.coin100x.fragments

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
import com.coin.coin100x.data.AddMoneyToClientModel
import com.coin.coin100x.data.UsersPurchaseItemModel
import com.coin.coin100x.databinding.FragmentBuyBinding
import com.coin.coin100x.repositories.NoteViewModel
import com.coin.coin100x.sharedPrefrence.App
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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

        bind.itemName.text = param1.toString()
        bind.tvItemPrice.text = param2.toString()
        bind.qty.setText("1")


        var amount = App.getString(requireContext(), "USER_AMOUNT")
        val sender_id = App.getString(requireContext(), "SENDER_ID")
        user_name = App.getString(requireContext(), "USER_NAME")
        user_id = App.getString(requireContext(), "USER_ID")
        var current_amount = App.getString(requireContext(), "USER_REMAINING_AMOUNT")

        val bal = App.getInt(requireContext(), App.BALANCE)
        Log.e("TAG", "onViewCreated: balance = $bal ")

        bind.points.text = bal.toString()

        //addProductQty(param1.toString(), Integer.parseInt(param2.toString()), wallet_amount)


        Glide.with(requireContext()).load(param3.toString()).into(bind.image)

        bind.btBuy.setOnClickListener {
            Log.e("TAG", "onViewCreated: buy button click  ")
            totalSum(
                sender_id,
                user_name,
                param1.toString(),
                Integer.parseInt(param2.toString()),
                bal,
            )
            App.setString(requireContext(), "PRODUCT_NAME", (param1.toString()))

        }
    }

    private fun totalSum(
        sender_id: String,
        userName: String,
        item_name: String,
        item_price: Int,
        amount: Int
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

                if (amount >= sum) {
                    val re_amount = (amount - sum).toString()
                    bind.points.text = re_amount

                    Log.e("TAG", "totalSum: reamount = $re_amount")

                    //addDataToDatabase(sender_id,item_name,item_price.toString(),count.toString(),wallet.toString(),wallet_amount.toString())
                    addProductsToDatabase(
                        FirebaseAuth.getInstance().uid.toString(),
                        userName,
                        item_name,
                        item_price.toString(),
                        count.toString(),
                        amount.toString(),
                        re_amount

                    )

                    userTotalBalance(
                        amount.toString(),
                        param1.toString(),
                        user_name,
                        re_amount,
                        "",
                        sender_id,
                        amount.toString()
                    )


                    /*  updateDataToDatabase(
                          sender_id,
                          user_id,
                          user_name,
                          amount.toString(),
                          re_amount
                      )*/

                } else {
                    Log.e("TAG", "totalSum: not enough balance ")
                    bind.points.text = amount.toString()
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

    /*private fun updateDataToDatabase(
        senderid: String,
        c_id: String,
        c_name: String,
        bal: String,
        current_bal: String
    ) {
        val updateMap = mapOf(
            *//*"sender_id" to senderid,
            "client_id" to c_id,
            "client_name" to c_name,
            "client_amount" to bal,*//*
            "client_remaining_amount" to current_bal,
            //"current_time" to "date"
        )
        // val updatedata = AddMoneyToClientModel(senderid, c_id, c_name, bal, current_bal, "")
        val ref =
            db.collection("MoneyAdded").document("ReceiverAmount")
                .collection(FirebaseAuth.getInstance().uid.toString())
                .document("Total_Balance").update(updateMap).addOnSuccessListener {

                }.addOnFailureListener{

                }


        Log.e("TAG", "updateDataToDatabase: $ref ")

        Log.e("TAG", "updateDataToDatabase: current balance = $current_bal and total bal = $bal ")


    }*/

    private fun userTotalBalance(
        amount: String,
        c_id: String,
        c_name: String,
        current_bal: String,
        date: String,
        senderid: String,
        balance: String
    ) {

        App.setInt(requireContext(),App.REMANING_BALANCE,Integer.parseInt(current_bal))

        val totalBalance = mapOf(
            "sender_id" to senderid,
            "client_id" to c_id,
            "client_name" to c_name,
            "client_amount" to balance,
            "client_remaining_amount" to current_bal,
            "current_time" to date,
            "type_of_transaction" to "Debit",
            "sum" to current_bal
        )

        // App.setInt(requireContext(), App.BALANCE, sum)
        db.collection("MoneyAdded").document("TotalBalance").update(totalBalance)
            .addOnCompleteListener {
                Log.e("TAG", "userTotalBalance: updation success ")
            }.addOnFailureListener {
                Log.e("TAG", "userTotalBalance: failed ")
            }
    }

    private fun getSenderTransaction( amount: String,
                                      c_id: String,
                                      c_name: String,
                                      current_bal: String,
                                      date: String,
                                      senderid: String,
                                      balance: String) {

        val totalBalance = mapOf(
            "sender_id" to senderid,
            "client_id" to c_id,
            "client_name" to c_name,
            "client_amount" to balance,
            "client_remaining_amount" to current_bal,
            "current_time" to date,
            "type_of_transaction" to "Debit",
            "sum" to current_bal
        )


        db.collection("SenderMoney").document("CurrentTransaction")
            .collection(FirebaseAuth.getInstance().uid.toString())
            .get().addOnCompleteListener {
                for (snap in it.result) {
                    Log.e(
                        "TAG",
                        "getSenderTransaction: ${snap.data["client_remaining_amount"].toString()}",
                    )

                    Log.e("TAG", "getSenderTransaction: sum remain ******** remain")
                    Log.e(

                        "TAG",
                        "getSenderTransaction:  s *******${snap.data["client_remaining_amount"].toString()}",
                    )

                    //App.setInt(context,App.AMOUNT,0)

                }

            }.addOnFailureListener {
                Log.e("TAG", "getSenderTransaction: failed ")
            }


    }


}