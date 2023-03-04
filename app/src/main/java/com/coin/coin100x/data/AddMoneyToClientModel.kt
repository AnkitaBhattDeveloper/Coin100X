package com.coin.coin100x.data

data class AddMoneyToClientModel(
    val sender_id: String,
    val client_id: String,
    val client_name: String,
    var client_amount: String,
    var client_remaining_amount: String,
    var current_time: String
){
    val map = mapOf(
        "sender_id" to sender_id,
        "client_id" to client_id,
        "client_name" to client_name,
        "client_amount" to client_amount,
        "client_remaining_amount" to client_remaining_amount,
        "current_time" to current_time
    )
    constructor() : this("", "", "", "", "","")



}