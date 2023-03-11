package com.coin.coin100x.repositories

import com.coin.coin100x.data.NoteDataModel

interface NoteRepository {
    fun getNotes():List<NoteDataModel>
    fun addNotes(note:NoteDataModel,result:List<String>)

}