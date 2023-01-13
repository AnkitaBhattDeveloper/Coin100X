package com.coin.coin100x.data

data class AddMoneyToClientModel(
    val senderId: String?,
    val clientId: String?,
    val clientName: String?,
    /* val product_name: String?,
     val product_price: String?,
     val product_qty: String?,*/
    var amount: String?,
    var remainingAmount: String?
) {
    constructor() : this("", "", "", "", "")

    /*fun toMap(): Map<String, Any?> {
        return mapOf(
            "sender_id" to senderId,
            "client_id" to clientId,
            "client_name" to clientName,
            "wallet_amount" to amount,
            "remaining_amount" to remainingAmount
        )

    }*/


}