package com.lhs94.pictonote.ui.main

import com.lhs94.pictonote.room.AppDatabase
import com.lhs94.pictonote.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val database: AppDatabase): BaseViewModel() {

    val noteListAdapter: NoteListAdapter = NoteListAdapter()

    init {
        database.noteDao().getAllNotes().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe ({ result ->
            noteListAdapter.updateData(result)
        }, {
            noteListAdapter.updateData(emptyList())
        })
    }
}