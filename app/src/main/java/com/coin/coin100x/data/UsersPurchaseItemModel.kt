package com.coin.coin100x.data

data class UsersPurchaseItemModel(
    val user_id: String?,
    val user_name: String?,
    val product_name: String?,
    val product_price: String?,
    val product_qty: String?,
    val available_bal: String?,
    val remaining_bal: String?,
    var resale:Boolean
) {
    constructor() : this("", "", "", "", "", "", "", false)
}
