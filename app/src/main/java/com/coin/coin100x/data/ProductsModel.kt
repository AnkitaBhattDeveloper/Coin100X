package com.coin.coin100x.data

data class ProductsModel(
    val item_name: String,
    val item_price: String,
    val item_qty: String,
    val img_url: String
) {
   /* val map = mapOf(
        "product_name" to item_name,
        "product_price" to item_price,
        "product_qty" to item_qty,
        "product_image" to img_url
    )*/

    constructor() : this("", "", "", "")
}


