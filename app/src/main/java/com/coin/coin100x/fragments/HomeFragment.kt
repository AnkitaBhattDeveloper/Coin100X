package com.coin.coin100x.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.coin.coin100x.adapters.ProductsAdapter
import com.coin.coin100x.data.ProductsModel
import com.coin.coin100x.databinding.FragmentHomeBinding

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
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val products_adapter = ProductsAdapter(requireContext(), item_list)
        bind.rvProducts.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            item_list.add(
                ProductsModel(
                    "ADIDAS",
                    true,
                    "1000",
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSD7iNo6LT1uVtSRKZFJcka1NhPVq71UA6zWQ&usqp=CAU"
                )
            )
            item_list.add(
                ProductsModel(
                    "ZARA",
                    true,
                    "1000",
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQt2LKpTw7tO7FSqjPqP21lSNyg6TOIVUqIOg&usqp=CAU"
                )
            )
            item_list.add(
                ProductsModel(
                    "GUCCI",
                    true,
                    "1000",
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRwH4fYHzzAVkQ-TdsOhqtnhDpxssy5KJCxaw&usqp=CAU"
                )
            )
            adapter = products_adapter

        }


    }


}