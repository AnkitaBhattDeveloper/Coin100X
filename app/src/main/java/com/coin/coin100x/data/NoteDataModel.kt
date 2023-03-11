package com.coin.coin100x.data

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class NoteDataModel(
    val id: String,
    val text: String,
    @ServerTimestamp
    var date: Date
)
