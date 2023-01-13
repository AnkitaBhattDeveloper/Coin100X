package com.coin.coin100x.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    val t_amount = MutableLiveData<String>()
    val points = MutableLiveData<String>()
    val item_name = MutableLiveData<String>()
    val item_amount = MutableLiveData<String>()


    fun setMoney(amount: String) {
        t_amount.value = amount
    }

    fun getMoney(): MutableLiveData<String> {
        return t_amount
    }

    fun setItem_Amount(item_price: String) {
        item_amount.value = item_price
    }

    fun getItem_Amount(): MutableLiveData<String> {
        return item_amount
    }


    /*fun sendMoney(amount: String) {
        money.value = amount
    }*/

    fun addPoints(coins: String) {
        points.value = coins
    }

    fun sendItemInfo(item: String, price: String) {
        item_name.value = item
        item_amount.value = price
    }

}