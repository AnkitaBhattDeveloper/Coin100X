package com.coin.coin100x.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.coin.coin100x.activity.AddActivity
import com.coin.coin100x.data.UsersPurchaseItemModel
import com.coin.coin100x.databinding.PurchaseLayoutItemsBinding

class PortfolioAdapter(
    val context: Context,
    val purchaseList: ArrayList<UsersPurchaseItemModel>,
) :
    RecyclerView.Adapter<PortfolioAdapter.ViewHolder>() {
    lateinit var binding: PurchaseLayoutItemsBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = PurchaseLayoutItemsBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val price = Integer.parseInt(purchaseList[position].product_price!!)
        val qty = Integer.parseInt(purchaseList[position].product_qty!!)

        val sum = price * qty

        holder.bind.purchaseItemName.text = purchaseList[position].product_name
        holder.bind.purchaseItemPrice.text = sum.toString()
        holder.bind.purchaseItemQty.text = purchaseList[position].product_qty

        holder.bind.btResale.setOnClickListener {
            context.startActivity(
                Intent(context, AddActivity::class.java)
                    .putExtra("ITEM_NAME", purchaseList[position].product_name)
                    .putExtra("ITEM_AMOUNT", purchaseList[position].product_price)
            )

        }


    }

    override fun getItemCount(): Int {
        return purchaseList.size
    }


    inner class ViewHolder(var bind: PurchaseLayoutItemsBinding) :
        RecyclerView.ViewHolder(bind.root)


}
