package com.lhs94.pictonote.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.lhs94.pictonote.note.Note;

import java.util.ArrayList;

public class SQLiteControler extends SQLiteOpenHelper {

    private static SQLiteControler instance = null;
    private SQLiteDatabase database;

    private static final String DB_NAME = "pictonote";
    private static final int DB_VERSION = 1;

    private static final String TAG = "SQLiteControler";

    private Cursor cursor;

    private SQLiteControler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS  notes (idx INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, text TEXT, image TEXT)");
        Log.d("dbcontroler", "on create");
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("CREATE TABLE IF NOT EXISTS  notes (idx INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, text TEXT, image TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //=====================================for debug
    public void clearAllDatabase() {
        database.execSQL("DROP TABLE notes");
    }

    public static void initControler(Context context) {
        if(instance == null) {
            instance = new SQLiteControler(context);
            Log.i(TAG, "Initialization Successful");
        }
        else {
            Log.i(TAG, "already been initialized");
        }
    }

    public ArrayList<Note> getAllDatas() {
        cursor = database.rawQuery("SELECT * FROM notes", null);
        ArrayList<Note> notes = new ArrayList<>();
        while(cursor.moveToNext()) {
            int idx = cursor.getInt(0);
            String title = cursor.getString(1);
            String text = cursor.getString(2);
            String image = cursor.getString(3);
            Note note = new Note(idx, title, text, image);
            notes.add(note);
        }

        return notes;
    }

    public void insertData(String title, String text, String image) {
        database.execSQL("INSERT INTO notes VALUES (null, '" + title + "', '" + text +"', '" + image + "')");
    }

    public void updateData(int idx, String title, String text, String image) {
        database.execSQL("UPDATE notes SET title = '" + title + "', text = '" + text + "', image = '" + image + "' WHERE idx = " + idx);
    }

    public void deleteData(int idx) {
        database.execSQL("DELETE FROM notes WHERE idx = " + idx);

    }

    public static SQLiteControler getInstance() {
        return instance;
    }

    public void closeControler() {
        cursor.close();
        database.close();
        close();
    }
}
