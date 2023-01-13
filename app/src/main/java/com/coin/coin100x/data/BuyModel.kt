package com.coin.coin100x.data

data class BuyModel(val item_name: String, val total_price: String)
{
    var item_count: Int = 0

    @JvmName("getItem_count1")
    fun getItem_count(): Int {
        if (item_count <= 0)
            return 0
        else
            return item_count
    }

    @JvmName("setItem_count1")
    fun setItem_count(item_count: Int) {
        this.item_count = item_count

    }
}
