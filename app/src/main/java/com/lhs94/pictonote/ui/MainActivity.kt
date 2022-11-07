package com.lhs94.pictonote.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.lhs94.pictonote.R
import com.lhs94.pictonote.databinding.ActivityMainBinding
import com.lhs94.pictonote.sqlite.SQLiteControler
import com.lhs94.pictonote.ui.widget.toast.Toast

class MainActivity : AppCompatActivity() {
    private val sharedViewModel: SharedViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        Toast.init(this)
        setSupportActionBar(binding.toolbar)

        SQLiteControler.initControler(this)
        //SQLiteControler.getInstance().clearAllDatabase();
    }

    override fun onDestroy() {
        SQLiteControler.instance?.closeControler()
        super.onDestroy()
    }
}