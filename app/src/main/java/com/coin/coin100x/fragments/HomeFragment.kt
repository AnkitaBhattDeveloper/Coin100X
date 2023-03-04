package com.coin.coin100x.fragments

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.coin.coin100x.adapters.ProductsAdapter
import com.coin.coin100x.data.ProductsModel
import com.coin.coin100x.databinding.FragmentHomeBinding
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    lateinit var bind: FragmentHomeBinding
    var item_list: ArrayList<ProductsModel> = arrayListOf()
    var db = Firebase.firestore
    var storageRef = Firebase.storage.reference
    lateinit var uploadTask: UploadTask
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var dbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        bind = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return bind.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) = HomeFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, param1)
                putString(ARG_PARAM2, param2)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseDatabase = FirebaseDatabase.getInstance()
        dbRef = firebaseDatabase.getReference("Admin")

        getProductFromData()


        /*  val products_adapter = ProductsAdapter(requireContext(), item_list)
          bind.rvAllProducts.apply {
              layoutManager = GridLayoutManager(requireContext(), 1)
              item_list.add(
                  ProductsModel(
                      "ADIDAS",
                      "1000",
                      "2",
                      "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSD7iNo6LT1uVtSRKZFJcka1NhPVq71UA6zWQ&usqp=CAU"
                  )
              )
              item_list.add(
                  ProductsModel(
                      "ZARA",
                      "1000",
                      "2",
                      "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQt2LKpTw7tO7FSqjPqP21lSNyg6TOIVUqIOg&usqp=CAU"
                  )
              )
              adapter = products_adapter

          }*/


    }

    /*  private fun getProducts() {
          db.collection("Products").get().addOnSuccessListener { result ->
              for (document in result) {
                  Log.e("TAG", "getProducts:  ${document.data?.get("product_name")}")
                  val products_adapter = ProductsAdapter(requireContext(), item_list)
                  val product = result.toObjects(ProductsModel::class.java)
                  bind.rvProducts.apply {
                      layoutManager = GridLayoutManager(requireContext(), 1)
                      item_list.addAll(product)
                      Log.e("TAG", "getProducts: ${product[0].map}")
                      adapter = products_adapter


                  }

              }
          }.addOnFailureListener {

          }

      }*/


    private fun getProductFromData() {
        dbRef.child("Available_Products")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (db in snapshot.children) {
                        //item_list.clear()

                        val data = db.getValue(ProductsModel::class.java)

                        Log.e("TAG", "onDataChange: ${data?.img_url.toString()} ")
                        Log.e("TAG", "onDataChange: ${data?.item_name.toString()} ")
                        Log.e("TAG", "onDataChange: ${data?.item_price.toString()} ")

                        val products_adapter = ProductsAdapter(requireContext(), item_list)
                        bind.rvAllProducts.apply {
                            layoutManager = GridLayoutManager(requireContext(), 1)
                            item_list.addAll(
                                listOf(
                                    ProductsModel(
                                        data?.item_name.toString(),
                                        data?.item_price.toString(),
                                        data?.item_qty.toString(),
                                        data?.img_url.toString()
                                    )
                                )
                            )

                            adapter = products_adapter
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("TAG", "onCancelled: ${error.message}")
                }
            })
    }
}


