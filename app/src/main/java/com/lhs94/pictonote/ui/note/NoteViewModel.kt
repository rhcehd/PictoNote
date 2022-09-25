package com.lhs94.pictonote.ui.note

import com.lhs94.pictonote.data.Note
import com.lhs94.pictonote.ui.BaseViewModel
import com.lhs94.pictonote.ui.SharedViewModel

class NoteViewModel: BaseViewModel() {
    var sharedViewModel: SharedViewModel? = null
    var currentNote: Note? = null
}