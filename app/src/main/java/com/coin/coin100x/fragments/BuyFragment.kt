package com.coin.coin100x.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.coin.coin100x.data.UsersPurchaseItemModel
import com.coin.coin100x.databinding.FragmentBuyBinding
import com.coin.coin100x.sharedPrefrence.App
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

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


    var wallet_amount = ""

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

        wallet_amount = App.getString(requireContext(), "USER_AMOUNT")
        val sender_id = App.getString(requireContext(), "SENDER_ID")
        val user_name = App.getString(requireContext(), "USER_NAME")

        bind.points.text = wallet_amount.toString()

        //addProductQty(param1.toString(), Integer.parseInt(param2.toString()), wallet_amount)


        Glide.with(requireContext()).load(param3.toString()).into(bind.image)

        bind.btBuy.setOnClickListener {
            Log.e("TAG", "onViewCreated: buy button click  ")
            totalSum(
                sender_id,
                user_name,
                param1.toString(),
                Integer.parseInt(param2.toString()),
                Integer.parseInt(wallet_amount),
            )
            App.setString(requireContext(), "PRODUCT_NAME", (param1.toString()))

        }


    }

    private fun totalSum(
        sender_id: String,
        userName: String,
        item_name: String,
        item_price: Int,
        wallet: Int
    ) {
        val qty = bind.qty.text.toString()
        Log.e("TAG", "totalSum: quantity = $qty")

        if (qty.isNotEmpty()) {
            val count = Integer.parseInt(qty)

            if (count < 1) {
                bind.qty.setText("1")
                Log.e("TAG", "totalSum:   enter a valid quantity $count")

            } else {
                sum = item_price * count
                bind.tvItemPrice.text = sum.toString()
                bind.totalPrice.text = sum.toString()

                if (wallet >= sum) {
                    wallet_amount = (wallet - sum).toString()
                    bind.points.text = wallet_amount.toString()
                    //addDataToDatabase(sender_id,item_name,item_price.toString(),count.toString(),wallet.toString(),wallet_amount.toString())
                    addProductsToDatabase(
                        FirebaseAuth.getInstance().uid.toString(),
                        userName,
                        item_name,
                        item_price.toString(),
                        count.toString(),
                        wallet.toString(),
                        wallet_amount.toString()
                    )

                    //updateDataToDatabase(sender_id, wallet.toString(), wallet_amount.toString())


                } else {
                    Log.e("TAG", "totalSum: not enough balance ")
                    bind.points.text = wallet.toString()
                }
            }
        }
    }

    /*  private fun updateDataToDatabase(
          senderid: String,
          amount: String,
          remain_amount: String
      ) {

          val key = dbRef.child("updatedAmount").push().key
          if (key == null) {
              Log.w("TAG", "Couldn't get push key for posts")
              return
          }

          val post = AddMoneyToClientModel(
              senderid,
              FirebaseAuth.getInstance().uid,
              App.getString(requireContext(), "CLIENT_NAME"),
              amount,
              remain_amount
          )
          val postValues = post.toMap()

          val childUpdates = hashMapOf<String, Any>(
              "/MoneyAdded/ReceiverAmount/${FirebaseAuth.getInstance().uid}/$key" to postValues,
              // "/user-amount/$remain_amount/$key" to postValues
          )

          dbRef.updateChildren(childUpdates)
      }
  */

    /*  dbRef.child("MoneyAdded").child("SenderAmount").child(senderid).push().setValue(addData)
          .addOnSuccessListener {
              dbRef.child("MoneyAdded").child("ReceiverAmount")
                  .child(FirebaseAuth.getInstance().uid.toString()).push()
                  .setValue(addData).addOnSuccessListener {

                  }
          }*/


    /*  private fun getDataFromDatabase(item_name: String, item_price: Int) {
          Log.e("TAG", "getDataFromDatabase: ")
          dbRef.child("MoneyAdded").child("ReceiverAmount")
              .child(FirebaseAuth.getInstance().uid.toString())
              .addValueEventListener(object : ValueEventListener {
                  override fun onDataChange(snapshot: DataSnapshot) {
                      Log.e("TAG", "getDataFromDatabase: datacjsnge))))) ")
                      for (data in snapshot.children) {
                          Log.e("TAG", "getDataFromDatabase: datacjsnge************ ")
                          val model = data.getValue(AddMoneyToClientModel::class.java)
                          wallet_amount = model?.remaining_amount.toString()
                          addProductQty(item_name, item_price, wallet_amount)


                      }
                  }

                  override fun onCancelled(error: DatabaseError) {
                      Log.e("TAG", "onDataChange: ${error.message}")
                  }

              })

      }*/


    /*fun addProductQty(item_name: String, item_price: Int, wallet: Int) {
        val qty = bind.qty.text.toString()


        if (!qty.isEmpty()){
            val count = Integer.parseInt(qty)
        if (count >= 1) {
            Log.e("TAG", "onBindViewHolder add button quantity: ${count}")

            sum = item_price * count
            App.setInt(requireContext(), "TOTAL_PRICE", sum)

            bind.itemPrice.text = sum.toString()
            Log.e("TAG", "onBindViewHolder add button sum: ${sum}")
            bind.totalPrice.text = sum.toString()

            val rm_amt = wallet - sum

            Log.e("TAG", "addProductQty: amount = $wallet and rema =$rm_amt")

            addProductsToDatabase(
                FirebaseAuth.getInstance().uid.toString(),
                item_name,
                item_price.toString(),
                qty,
                rm_amt.toString(),
                rm_amt.toString()
            )
            bind.points.text = rm_amt.toString()


        } }
        else {

            Toast.makeText(requireContext(), "Add atleast one quantity", Toast.LENGTH_SHORT).show()
            Log.e("TAG", "addProductQty: select valid quantity ")
        }


    }*/


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
             remaining_bal
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


/*
    private fun addProductsToDatabase(
        user_id: String,
        user_name: String,
        product_name: String,
        product_price: String,
        product_qty: String,
        available_bal: String,
        remaining_bal: String
    ) {
        val purchasedItems = hashMapOf(
            "userId" to user_id,
            "userName" to user_name,
            "productName" to product_name,
            "productPrice" to product_price,
            "productQuantity" to product_qty,
            "availableBalance" to available_bal,
            "remainingBalance" to remaining_bal
        )
        db.collection("Purchased_Items").document(user_id).set(purchasedItems)
            .addOnSuccessListener {
                Log.e("TAG", "addProductsToDatabase: successful $it ")
            }.addOnFailureListener {
            Log.e("TAG", "addProductsToDatabase:failure ${it.message}")

        }


    }
*/


}