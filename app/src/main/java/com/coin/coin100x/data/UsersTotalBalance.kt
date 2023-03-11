package com.coin.coin100x.data

data class UsersTotalBalance(
    val uuid: String?,
    var userName: String?,
    var email: String?,
    var phone_Number: String?,
    var balance:String?


) {
    constructor() : this("",
        "", "",
        "",""
    )
}