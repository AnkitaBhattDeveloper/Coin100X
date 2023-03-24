package com.coin.coin100x.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.coin.coin100x.data.AddMoneyToClientModel
import com.coin.coin100x.data.UsersPurchaseItemModel
import com.coin.coin100x.databinding.PurchaseLayoutItemsBinding
import com.coin.coin100x.databinding.TransactionLayoutBinding

class TransactionAdapter(
    val context: Context,
    val purchaseList: ArrayList<AddMoneyToClientModel>,
) :
    RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {
    lateinit var binding: TransactionLayoutBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = TransactionLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val amt = Integer.parseInt(purchaseList[position].client_amount!!)
//        val sender_id = Integer.parseInt(purchaseList[position].sender_id!!)


       // holder.bind.tvSenderId.text = sender_id.toString()
        holder.bind.tvSenderMoney.text = amt.toString()


    }

    override fun getItemCount(): Int {
        return purchaseList.size
    }

    inner class ViewHolder(var bind: TransactionLayoutBinding) :
        RecyclerView.ViewHolder(bind.root)


}