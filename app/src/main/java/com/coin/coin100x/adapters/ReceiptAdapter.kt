package com.coin.coin100x.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.coin.coin100x.data.ReceiptDataModel
import com.coin.coin100x.databinding.ReceiptLayoutBinding

class ReceiptAdapter(val context: Context, val a_list: ArrayList<ReceiptDataModel>) :
    RecyclerView.Adapter<ReceiptAdapter.ViewHolder>() {

    lateinit var bind: ReceiptLayoutBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        bind = ReceiptLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(bind)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            customerName.text = a_list[position].customer_name
            customerEmail.text = a_list[position].customer_email
            contactNumber.text =a_list[position].contact_number.toString()
            productName.text = a_list[position].product_name
            totalAmount.text = a_list[position].total_amount.toString()

        }
    }



override fun getItemCount(): Int {
    return a_list.size
}

inner class ViewHolder(var binding: ReceiptLayoutBinding) :
    RecyclerView.ViewHolder(binding.root)

}