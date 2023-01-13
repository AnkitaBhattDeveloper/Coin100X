package com.coin.coin100x.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.coin.coin100x.adapters.PortfolioAdapter
import com.coin.coin100x.data.UsersPurchaseItemModel
import com.coin.coin100x.databinding.FragmentPortfolioBinding
import com.coin.coin100x.sharedPrefrence.App
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PortfolioFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PortfolioFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var bind: FragmentPortfolioBinding
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var dbRef: DatabaseReference
    var purchase_list: ArrayList<UsersPurchaseItemModel> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = FragmentPortfolioBinding.inflate(layoutInflater, container, false)
        return bind.root
    }

    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PortfolioFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseDatabase = FirebaseDatabase.getInstance()
        dbRef = firebaseDatabase.getReference("Users")
        bind.name.text = App.getString(requireContext(), "USER_NAME")
        //addAdapter()
        getDataFromDatabase()


    }

    /*  fun addAdapter() {
          val portfolio_adapter = PortfolioAdapter(requireContext(), purchase_list)
          bind.rvPurchased.apply {
              layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
              getDataFromDatabase()
              adapter = portfolio_adapter
          }
      }*/

    private fun getDataFromDatabase() {
        dbRef.child("User_Purchase_Item")
            .child(FirebaseAuth.getInstance().uid.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (data in snapshot.children) {
                        val value = data.getValue(UsersPurchaseItemModel::class.java)

                        val portfolio_adapter = PortfolioAdapter(requireContext(), purchase_list)
                        bind.rvPurchased.apply {
                            layoutManager =
                                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                            purchase_list.addAll(
                                listOf(
                                    UsersPurchaseItemModel(
                                        value?.user_id,
                                        value?.user_name,
                                        value?.product_name,
                                        value?.product_price,
                                        value?.product_qty,
                                        value?.available_bal,
                                        value?.remaining_bal
                                    )
                                )
                            )
                            adapter = portfolio_adapter
                        }

                        Log.e("TAG", "onDataChange: ${value?.product_name} ")

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("TAG", "onDataChange: ${error?.message} ")
                }

            })
    }


}