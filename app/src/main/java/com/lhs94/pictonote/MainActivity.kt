package com.lhs94.pictonote

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import com.lhs94.pictonote.sqlite.SQLiteControler
import com.lhs94.pictonote.NoteListAdapter
import android.os.Bundle
import android.os.Build
import android.content.pm.PackageManager
import com.lhs94.pictonote.R
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import com.lhs94.pictonote.note.NoteActivity
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager

class MainActivity : AppCompatActivity() {
    var sqliteControler: SQLiteControler? = null
    private var adapter: NoteListAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        if (savedInstanceState != null) {
        } else {
            initViews()
        }
    }

    override fun onStart() {
        super.onStart()
        val notes = sqliteControler!!.allDatas
        adapter!!.updateData(notes)
    }

    override fun onDestroy() {
        sqliteControler!!.closeControler()
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
                    initViews()
                } else if (grantResult == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(applicationContext, "권한 설정이 필요합니다", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }
    }

    private fun initViews() {
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar_main)
        setSupportActionBar(toolbar)
        SQLiteControler.initControler(this)
        sqliteControler = SQLiteControler.instance
        //SQLiteControler.getInstance().clearAllDatabase();
        val recyclerView = findViewById<RecyclerView>(R.id.recycler)
        adapter = NoteListAdapter(this)
        recyclerView.adapter = adapter
        val lm = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = lm
    }
}