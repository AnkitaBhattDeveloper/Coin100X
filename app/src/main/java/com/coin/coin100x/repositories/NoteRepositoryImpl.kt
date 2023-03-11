package com.coin.coin100x.repositories

import com.coin.coin100x.data.NoteDataModel
import com.google.firebase.firestore.FirebaseFirestore

class NoteRepositoryImpl(val database: FirebaseFirestore) : NoteRepository {
    override fun getNotes(): List<NoteDataModel> {
        return arrayListOf()
    }

    override fun addNotes(note: NoteDataModel, result: List<String>) {
        //  database.collection()
    }
}