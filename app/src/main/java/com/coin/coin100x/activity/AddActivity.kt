package com.coin.coin100x.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.coin.coin100x.R
import com.coin.coin100x.databinding.ActivityAddBinding
import com.coin.coin100x.fragments.BuyFragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class AddActivity : AppCompatActivity() {

    lateinit var bind: ActivityAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityAddBinding.inflate(layoutInflater)
        setContentView(bind.root)
        supportActionBar?.hide()

        val productName = intent.getStringExtra("ITEM_NAME")
        val productPrice = intent.getStringExtra("ITEM_AMOUNT")
        val productImage = intent.getStringExtra("ITEM_IMAGE")

        val sale = intent.getStringExtra("RESALE")

        replaceFragment(
            BuyFragment.newInstance(
                productName.toString(),
                productPrice.toString(),
                productImage.toString()
            )
        )


    }


    private fun replaceFragment(fragment: Fragment) {
        val fm = supportFragmentManager
        val ft = fm.beginTransaction().replace(R.id.frameLayout, fragment)
        ft.commit()

    }

}