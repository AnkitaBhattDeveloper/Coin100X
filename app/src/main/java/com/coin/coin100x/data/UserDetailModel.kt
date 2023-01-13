package com.coin.coin100x.data

data class UserDetailModel(
    val name: String,
    val email: String,
    val contact_number:String,
    val product_name:String,
    val total_price:String,
    val product_qty: String,
    val available_bal:String,
    val remaining_bal:String,
    val isWinner:Boolean
)