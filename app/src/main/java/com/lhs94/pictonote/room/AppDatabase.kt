package com.lhs94.pictonote.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lhs94.pictonote.room.dao.NoteDao
import com.lhs94.pictonote.room.entity.Note
import io.reactivex.rxjava3.core.Completable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Note::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun noteDao(): NoteDao
}