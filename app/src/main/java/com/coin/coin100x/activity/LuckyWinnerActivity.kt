package com.coin.coin100x.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.coin.coin100x.data.WinnerProductModel
import com.coin.coin100x.databinding.ActivityLuckyWinnerBinding
import com.google.firebase.database.*
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit

class LuckyWinnerActivity : AppCompatActivity() {
    lateinit var binding: ActivityLuckyWinnerBinding
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var dbRef: DatabaseReference
    lateinit var context: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLuckyWinnerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        context = this
        firebaseDatabase = FirebaseDatabase.getInstance()
        dbRef = firebaseDatabase.getReference("Admin")


/*
        val animationFadeIn = AnimationUtils.loadAnimation(this, com.coin.coin100x.R.anim.bounce)
        binding.winner.startAnimation(animationFadeIn)
*/


        val party = Party(
            speed = 0f,
            maxSpeed = 30f,
            damping = 0.9f,
            spread = 360,
            colors = listOf(0xecd2ac, 0xe5d2ac, 0xe9dbda),
            emitter = Emitter(duration = 90, TimeUnit.SECONDS).max(10000),
            position = Position.Relative(0.5, 0.1)
        )
        binding.konfettiView.start(party)
        getDataFromDatabase()
        onBtnClick()


    }

    fun getDataFromDatabase() {
        dbRef.child("AddProduct").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val value = snapshot.getValue(WinnerProductModel::class.java)
                    binding.tvLuckyProduct.setText(value?.item_name)
                    Log.e("TAG", "onDataChange: winner activirty ${value?.item_name} ")
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun onBtnClick() {
        binding.btnSkip.setOnClickListener {
            startActivity(Intent(context, ProfileActivity::class.java))
            finish()
        }
        binding.btnBuyProducts.setOnClickListener {
            startActivity(Intent(context, MainActivity::class.java))
            finish()
        }

    }


}