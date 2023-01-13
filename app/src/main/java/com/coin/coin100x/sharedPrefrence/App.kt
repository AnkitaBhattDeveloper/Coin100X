package com.coin.coin100x.sharedPrefrence

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

class App : Application() {
    companion object {

        const val FRAGMENT_CODE = 100
        const val AMOUNT = "AMOUNT"
        const val CURRENT_USER = "user"
        const val PREF_USER = "PREF_USER"
        const val WALLET_AMOUNT = "WALLET_AMOUNT"
        var UUID = "userId"
        const val BALANCE = "nill"


        fun setString(context: Context, key: String, value: String) {
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences(PREF_USER, MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString(key, value)
            editor.apply()
            //editor.commit()
        }

        fun getString(context: Context, key: String):String {
            val sharedPreferences: SharedPreferences = context.getSharedPreferences(
                PREF_USER, MODE_PRIVATE
            )
           return sharedPreferences.getString(key, "")!!
        }

        fun setInt(context: Context, key: String, value: Int) {
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences(PREF_USER, Application.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putInt(key, value)
            editor.apply()
        }

        fun getInt(context: Context, key: String):Int {
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences(PREF_USER, Application.MODE_PRIVATE)
            return sharedPreferences.getInt(key, 0)
        }
    }
}