package com.lhs94.pictonote

import com.lhs94.pictonote.note.NoteActivity

class AppController private constructor() {
    private var currentNoteActivity: NoteActivity? = null
    fun setCurrentNoteActivity(noteActivity: NoteActivity?) {
        currentNoteActivity = noteActivity
    }

    fun setSelectingImage(b: Boolean) {
        currentNoteActivity!!.setSelectingImage(b)
    }

    companion object {
        var instance: AppController? = null
            get() {
                if (field == null) {
                    field = AppController()
                }
                return field
            }
            private set
    }
}