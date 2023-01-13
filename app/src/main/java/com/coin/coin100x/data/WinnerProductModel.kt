package com.coin.coin100x.data

data class WinnerProductModel(
    val item_name: String,
    val item_price: String,
    val item_image: String
) {
    constructor() : this("", "", "")
}