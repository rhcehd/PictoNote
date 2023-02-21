package com.lhs94.pictonote.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.lhs94.pictonote.R
import com.lhs94.pictonote.databinding.ActivityMainBinding
import com.lhs94.pictonote.ui.widget.toast.Toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        Toast.init(this)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setCustomView(R.layout.toolbar_title)
    }
}