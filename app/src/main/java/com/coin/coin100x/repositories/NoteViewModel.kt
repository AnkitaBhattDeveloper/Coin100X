package com.coin.coin100x.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.coin.coin100x.data.NoteDataModel

class NoteViewModel(val repository: NoteRepository) : ViewModel() {

    private val _notes = MutableLiveData<List<NoteDataModel>>()
    val notes: LiveData<List<NoteDataModel>>
        get() = _notes

    fun getNotes() {
        _notes.value = repository.getNotes()
    }

}