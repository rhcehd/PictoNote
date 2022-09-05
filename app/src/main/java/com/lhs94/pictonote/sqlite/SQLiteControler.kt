package com.lhs94.pictonote.sqlite

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.lhs94.pictonote.note.Note
import java.util.ArrayList

class SQLiteControler private constructor(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    private val database: SQLiteDatabase
    private var cursor: Cursor? = null
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS  notes (idx INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, text TEXT, image TEXT)")
        Log.d("dbcontroler", "on create")
    }

    override fun onOpen(db: SQLiteDatabase) {
        super.onOpen(db)
        db.execSQL("CREATE TABLE IF NOT EXISTS  notes (idx INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, text TEXT, image TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    //=====================================for debug
    fun clearAllDatabase() {
        database.execSQL("DROP TABLE notes")
    }

    val allDatas: ArrayList<Note>
        get() {
            cursor = database.rawQuery("SELECT * FROM notes", null)
            val notes = ArrayList<Note>()
            while (cursor?.moveToNext() == true) {
                val idx = cursor?.getInt(0)
                val title = cursor?.getString(1)
                val text = cursor?.getString(2)
                val image = cursor?.getString(3)
                val note = Note(idx!!, title, text, image)
                notes.add(note)
            }
            return notes
        }

    fun insertData(title: String, text: String, image: String?) {
        database.execSQL("INSERT INTO notes VALUES (null, '$title', '$text', '$image')")
    }

    fun updateData(idx: Int, title: String, text: String, image: String?) {
        database.execSQL("UPDATE notes SET title = '$title', text = '$text', image = '$image' WHERE idx = $idx")
    }

    fun deleteData(idx: Int) {
        database.execSQL("DELETE FROM notes WHERE idx = $idx")
    }

    fun closeControler() {
        cursor!!.close()
        database.close()
        close()
    }

    companion object {
        var instance: SQLiteControler? = null
            private set
        private const val DB_NAME = "pictonote"
        private const val DB_VERSION = 1
        private const val TAG = "SQLiteControler"
        fun initControler(context: Context) {
            if (instance == null) {
                instance = SQLiteControler(context)
                Log.i(TAG, "Initialization Successful")
            } else {
                Log.i(TAG, "already been initialized")
            }
        }
    }

    init {
        database = writableDatabase
    }
}