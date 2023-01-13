package com.coin.coin100x.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.coin.coin100x.data.ReceiptDataModel
import com.coin.coin100x.data.Register_Info
import com.coin.coin100x.databinding.FragmentReceiptBinding
import com.coin.coin100x.sharedPrefrence.App
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ReceiptFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReceiptFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var bind: FragmentReceiptBinding
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var dbRef: DatabaseReference
    var receipt_list: ArrayList<ReceiptDataModel> = arrayListOf()


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
        bind = FragmentReceiptBinding.inflate(layoutInflater, container, false)
        return bind.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ReceiptFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ReceiptFragment().apply {
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



        getDataFromDatabase()


    }


    private fun getDataFromDatabase() {
        dbRef.child("RegisteredUser")
            .child(FirebaseAuth.getInstance().uid.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val value = snapshot.getValue(Register_Info::class.java)

                        /* val portfolio_adapter = ReceiptAdapter(requireContext(), receipt_list)
                         bind.rvReceipt.apply {
                             layoutManager =
                                 LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                           */

                        receipt_list.addAll(
                            listOf(
                                ReceiptDataModel(
                                    value?.userName,
                                    value?.email,
                                    value?.phone_Number.toString(),
                                    App.getString(requireContext(), "PRODUCT_NAME"),
                                    App.getInt(requireContext(), "TOTAL_PRICE"),

                                    )
                            )
                        )
                        //adapter = portfolio_adapter
                        //}
                        Log.e("TAG", "onDataChange: ${value?.userName} ")

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("TAG", "onDataChange: ${error?.message} ")
                }

            })
    }


    private fun setData() {

    }

}