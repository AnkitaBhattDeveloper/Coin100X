package com.coin.coin100x.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.coin.coin100x.R
import com.coin.coin100x.adapters.TransactionAdapter
import com.coin.coin100x.data.AddMoneyToClientModel
import com.coin.coin100x.data.ProductsModel
import com.coin.coin100x.databinding.FragmentTransactionHistoryBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TransactionHistory.newInstance] factory method to
 * create an instance of this fragment.
 */
class TransactionHistory : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var database: FirebaseDatabase
    lateinit var dbref: DatabaseReference
    val db = Firebase.firestore
    private lateinit var binding: FragmentTransactionHistoryBinding
    val transactionList: ArrayList<AddMoneyToClientModel> = arrayListOf()

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
        // Inflate the layout for this fragment
        binding = FragmentTransactionHistoryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TransactionHistory.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TransactionHistory().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("TAG", "onViewCreated: transaction history called  ")
        addAdapter()

    }

    private fun addAdapter() {
        db.collection("MoneyAdded").document("ReceiverAmount")
            .collection(FirebaseAuth.getInstance().uid.toString()).get().addOnCompleteListener {
                for (snap in it.result) {
                    val transactionAdapter = TransactionAdapter(requireContext(), transactionList)
                    binding.rvTransactionHistory.apply {
                        layoutManager = GridLayoutManager(requireContext(), 1)
                        transactionList.addAll(
                            listOf(
                                AddMoneyToClientModel(
                                    snap.data["sender_id"].toString(),
                                    snap.data["client_id"].toString(),
                                    snap.data["client_name"].toString(),
                                    snap.data["client_amount"].toString(),
                                    snap.data["client_remaining_amount"].toString(),
                                    snap.data["current_time"].toString(),
                                )
                            )
                        )

                        adapter = transactionAdapter
                    }


                    Log.e("TAG", "addAdapter: ${snap.data["client_amount"].toString()} ")
                }
                Log.e("TAG", "addAdapter: success ")
            }.addOnFailureListener {
                Log.e("TAG", "addAdapter: success ")
            }
    }
}