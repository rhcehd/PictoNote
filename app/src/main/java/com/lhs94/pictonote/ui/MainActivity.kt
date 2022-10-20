package com.lhs94.pictonote.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.lhs94.pictonote.R
import com.lhs94.pictonote.sqlite.SQLiteControler
import com.lhs94.pictonote.ui.widget.toast.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Toast.init(this)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        SQLiteControler.initControler(this)
        //SQLiteControler.getInstance().clearAllDatabase();
    }

    override fun onDestroy() {
        SQLiteControler.instance?.closeControler()
        super.onDestroy()
    }
}