package com.lhs94.pictonote.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.lhs94.pictonote.R
import com.lhs94.pictonote.note.NoteActivity
import com.lhs94.pictonote.sqlite.SQLiteControler

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        SQLiteControler.initControler(this)
        //SQLiteControler.getInstance().clearAllDatabase();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permissionList = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET)
            var check: Int
            for (permission in permissionList) {
                check = checkCallingOrSelfPermission(permission)
                if (check == PackageManager.PERMISSION_DENIED) {
                    requestPermissions(permissionList, 0)
                }
            }
        }
    }

    override fun onDestroy() {
        SQLiteControler.instance?.closeControler()
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.actionbar_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add -> {
                val intent = Intent(this, NoteActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_info ->                 //show info
                return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0) {
            for (grantResult in grantResults) {
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    // do nothing
                } else if (grantResult == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(applicationContext, "권한 설정이 필요합니다", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }
    }
}