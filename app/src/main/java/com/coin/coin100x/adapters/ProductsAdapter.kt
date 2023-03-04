package com.coin.coin100x.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.coin.coin100x.activity.AddActivity
import com.coin.coin100x.data.ProductsModel
import com.coin.coin100x.databinding.ProductsLayoutBinding
import com.coin.coin100x.databinding.TotalListItemLayoutBinding

class ProductsAdapter(
    val context: Context,
    val productList: ArrayList<ProductsModel>,
) :
    RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {
    lateinit var binding: ProductsLayoutBinding
    var isSelected: Boolean = false
    var item_price: Int = 0
    var sum: Int = 0


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            ProductsLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind.tvItemName.text = productList[position].item_name
        holder.bind.tvitemPrice.text = productList[position].item_price

        Glide.with(context).load(productList[position].img_url).into(binding.productImage)
        Log.e("TAG", "onBindViewHolder: products adapter called ", )

        binding.root.setOnClickListener {
            context.startActivity(
                Intent(context, AddActivity::class.java)
                    .putExtra("ITEM_NAME", productList[position].item_name)
                    .putExtra("ITEM_AMOUNT", productList[position].item_price)
                    .putExtra("ITEM_IMAGE",productList[position].img_url)
            )

        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    inner class ViewHolder(var bind: ProductsLayoutBinding) : RecyclerView.ViewHolder(bind.root)

}
