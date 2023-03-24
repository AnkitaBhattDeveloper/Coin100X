package com.coin.coin100x.activity

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.coin.coin100x.R
import com.coin.coin100x.data.ReceiptDataModel
import com.coin.coin100x.databinding.ActivityReceiptBinding
import com.coin.coin100x.fragments.PortfolioFragment
import com.coin.coin100x.fragments.ReceiptFragment
import com.coin.coin100x.fragments.TransactionHistory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ReceiptActivity : AppCompatActivity() {
    lateinit var bind: ActivityReceiptBinding
    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityReceiptBinding.inflate(layoutInflater)
        setContentView(bind.root)
        supportActionBar?.hide()
        context = this


        val port = intent.getStringExtra("PORTFOLIO")
        if (port?.equals("portfolio") == true)
            replaceFragment(PortfolioFragment())

        val recp = intent.getStringExtra("RECEIPT")
        if (recp?.equals("receipt") == true)
            replaceFragment(ReceiptFragment())

        val tHistory = intent.getStringExtra("TRANSACTION")
        if (tHistory?.equals("transactionHistory") == true)
            replaceFragment(TransactionHistory())


        Log.e("TAG", "onCreate: recepit activity key =  ")
        //getData()

    }

    private fun replaceFragment(fragment: Fragment) {
        val fm = supportFragmentManager
        val ft = fm.beginTransaction().replace(R.id.frameLayout, fragment)
        ft.commit()


    }

}