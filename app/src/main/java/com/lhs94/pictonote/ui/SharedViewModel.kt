package com.lhs94.pictonote.ui

import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR

class SharedViewModel: BaseViewModel() {
    var isNoteTitleVisible = false
        @Bindable get
        set(value) {
            if(field != value) {
                field = value
                notifyChange(BR.noteTitleVisible)
            }
        }
    var noteTitle: String = ""
        @Bindable get
        set(value) {
            if(field != value) {
                field = value
                notifyChange(BR.noteTitle)
            }
        }
}