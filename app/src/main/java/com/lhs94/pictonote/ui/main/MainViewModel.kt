package com.lhs94.pictonote.ui.main

import com.lhs94.pictonote.sqlite.SQLiteControler
import com.lhs94.pictonote.ui.BaseViewModel

class MainViewModel: BaseViewModel() {

    val noteListAdapter: NoteListAdapter = NoteListAdapter()

    init {
        val sqliteController = SQLiteControler.instance
        val notes = sqliteController!!.allDatas
        noteListAdapter.updateData(notes)
    }
}