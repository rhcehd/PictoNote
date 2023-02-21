package com.lhs94.pictonote.ui.note

import androidx.databinding.Bindable
import com.lhs94.pictonote.BR
import com.lhs94.pictonote.room.AppDatabase
import com.lhs94.pictonote.room.entity.Note
import com.lhs94.pictonote.ui.BaseViewModel
import com.lhs94.pictonote.ui.widget.toast.Toast
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val database: AppDatabase): BaseViewModel() {
    var currentNote: Note? = null
        get() {
            val currentNote = field
            return if(currentNote != null) {
                field = Note(currentNote.uid, title, text, "")
                field
            } else {
                field
            }
        }
        set(value) {
            field = value
            if(value != null) {
                title = value.title
                text = value.text
                notifyChange(BR._all)
            }
        }

    var title: String = ""
        @Bindable get
    var imageListAdapter: Int = 0
    var text: String = ""
        @Bindable get

    fun onClickEditOptionMenu() {
        Toast.showDebugToast("onClickEditOptionMenu")
    }

    fun onClickDeleteOptionMenu() {
        Toast.showDebugToast("onClickDeleteOptionMenu")
    }

    fun saveNote() {
        val note = currentNote ?: Note(title, text)
        val completable = if (note.uid == null) {
            database.noteDao().insertNote(note)
        } else {
            database.noteDao().updateNote(note)
        }
        completable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe()
            /*.subscribe({
                Toast.showDebugToast("저장됨")
            }, {
                Toast.showDebugToast("$it")
            })*/
    }
}