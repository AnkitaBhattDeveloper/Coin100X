package com.coin.coin100x.data


data class Register_Info(
    val uuid: String?,
    var userName: String?,
    var email: String?,
    var phone_Number: String?,


) {
    constructor() : this("",
        "", "",
        ""
    )
}