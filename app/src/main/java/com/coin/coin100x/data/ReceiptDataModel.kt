package com.coin.coin100x.data

data class ReceiptDataModel(
    val customer_name: String?,
    val customer_email: String?,
    val contact_number: String,
    val product_name: String?,
    val total_amount: Int
)