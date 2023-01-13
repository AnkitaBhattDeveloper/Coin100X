package com.coin.coin100x.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.coin.coin100x.data.AddMoneyToClientModel
import com.coin.coin100x.sharedPrefrence.App
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class GetMoneyRepository {
    val firebaseDatabase = FirebaseDatabase.getInstance()
    val dbRef = firebaseDatabase.getReference("Users")

    private val _searchResponse = MutableLiveData<AddMoneyToClientModel>()
    val searchResponse: LiveData<AddMoneyToClientModel>
        get() = _searchResponse

    init {
        dbRef.child("MoneyAdded").child("ReceiverAmount")
            .child(FirebaseAuth.getInstance().uid.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.e("TAG", "getDataFromDatabase: datacjsnge))))) ")

                    //_searchResponse.postValue(null)


                    for (data in snapshot.children) {

                        val model = data.getValue<AddMoneyToClientModel>()
                        Log.e(
                            "TAG",
                            "getDataFromDatabase: datacjsnge************  ${model?.amount} name = ${model?.clientName.toString()} "
                        )
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("TAG", "onDataChange: ${error.message}")
                }

            })
    }
}