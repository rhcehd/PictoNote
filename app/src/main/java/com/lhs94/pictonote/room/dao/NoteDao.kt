package com.lhs94.pictonote.room.dao

import androidx.room.*
import com.lhs94.pictonote.room.entity.Note
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertNote(note: Note): Completable

    @Update
    fun updateNote(note: Note): Completable

    @Query("SELECT * FROM note")
    fun getAllNotes(): Flowable<List<Note>>
}