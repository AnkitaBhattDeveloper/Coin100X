package com.coin.coin100x.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.coin.coin100x.activity.ReceiptActivity
import com.coin.coin100x.data.BuyModel
import com.coin.coin100x.databinding.TotalListItemLayoutBinding

class BuyAdapter(val context: Context, val itemList: ArrayList<BuyModel>) :
    RecyclerView.Adapter<BuyAdapter.ViewHolder>() {

    lateinit var binding: TotalListItemLayoutBinding
    var isSelected: Boolean = false
    var item_price: Int = 0
    var sum: Int = 0



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            TotalListItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind.itemName.text = itemList[position].item_name
        holder.bind.itemPrice.text = itemList[position].total_price


        holder.bind.imgAdd.setOnClickListener{
            itemList[position].setItem_count(itemList.get(position).getItem_count() + 1)
            holder.bind.tvQty.text = itemList[position].getItem_count().toString()
            item_price = Integer.parseInt(itemList[position].total_price)
            sum += item_price
            holder.bind.itemPrice.text = sum.toString()
            Log.e("TAG", "onBindViewHolder add button sum: ${sum}")
        }
        holder.bind.imgRemove.setOnClickListener {
            itemList.get(position).setItem_count((itemList.get(position).getItem_count()) - 1)
            holder.bind.tvQty.text = (itemList[position].item_count).toString()
            item_price = Integer.parseInt(itemList[position].total_price)
            if (sum != 0)
                sum -= item_price
            holder.bind.itemPrice.text = sum.toString()
            Log.e("TAG", "onBindViewHolder remove button sum: ${sum}")
        }
        holder.bind.root.setOnClickListener{
            context.startActivity(Intent(context,ReceiptActivity::class.java))
        }

    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ViewHolder(var bind: TotalListItemLayoutBinding) :
        RecyclerView.ViewHolder(bind.root)

}