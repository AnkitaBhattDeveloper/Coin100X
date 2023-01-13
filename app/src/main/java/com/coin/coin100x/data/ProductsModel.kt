package com.coin.coin100x.data

data class ProductsModel(
    val item_name: String,
    val isChecked: Boolean,
    val item_price: String,
    val img_url:String
) {
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




