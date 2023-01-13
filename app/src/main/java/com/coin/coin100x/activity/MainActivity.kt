package com.coin.coin100x.activity

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.coin.coin100x.R
import com.coin.coin100x.databinding.ActivityMainBinding
import com.coin.coin100x.fragments.HomeFragment
import com.coin.coin100x.sharedPrefrence.App
import com.coin.coin100x.viewModel.SharedViewModel

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var context: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        context = this
        replaceFragment(HomeFragment())

        /*  binding.bottomNavBar.setOnItemSelectedListener {
              when (it.itemId) {
                  R.id.home -> replaceFragment(HomeFragment())
                  R.id.wallet -> replaceFragment(AddMoneyFragment())
  
                  else -> replaceFragment(HomeFragment())
  
              }
              true
          }*/

        getViewModel()

    }

    private fun replaceFragment(fragment: Fragment) {
        val fm = supportFragmentManager
        val ft = fm.beginTransaction().replace(R.id.frameLayout, fragment)
        ft.commit()
        //   getViewModel()

    }


    fun getViewModel() {
        val model = ViewModelProvider(this).get(SharedViewModel::class.java)
        model.item_name.observe(this, Observer {
            Log.e("TAG", " main activity getViewModel: $it")
        })
    }


}