package com.lhs94.pictonote.ui.note

import com.lhs94.pictonote.data.Note
import com.lhs94.pictonote.ui.BaseViewModel
import com.lhs94.pictonote.ui.SharedViewModel
import com.lhs94.pictonote.ui.widget.toast.Toast

class NoteViewModel: BaseViewModel() {
    var sharedViewModel: SharedViewModel? = null
    var currentNote: Note? = null

    var title: String = ""
    var imageListAdapter: Int = 0
    var text: String = ""

    fun onClickEditOptionMenu() {
        Toast.showDebugToast("onClickEditOptionMenu")
    }

    fun onClickDeleteOptionMenu() {
        Toast.showDebugToast("onClickDeleteOptionMenu")
    }
}