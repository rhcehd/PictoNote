package com.lhs94.pictonote.note

import com.rd.PageIndicatorView
import com.lhs94.pictonote.R
import com.lhs94.pictonote.note.image.AttachOptionDialog
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.content.DialogInterface
import android.widget.EditText
import com.lhs94.pictonote.AppController
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.lhs94.pictonote.note.image.DepthPageTransformer
import com.lhs94.pictonote.sqlite.SQLiteControler
import com.lhs94.pictonote.ui.note.NoteImageListAdapter
import android.os.Build
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import android.net.Uri
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.lhs94.pictonote.data.Note
import java.io.File

class NoteActivity : AppCompatActivity(), DialogInterface.OnClickListener {
    private var sqliteController: SQLiteControler? = null
    private var note: Note? = null
    private var editTitle: EditText? = null
    private var editText: EditText? = null
    private var haveToSave = false
    private var selectingImage = false
    private var pageAdapter: NoteImageListAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_note)
        AppController.instance?.setCurrentNoteActivity(this)
        val toolbar = findViewById<Toolbar>(R.id.toolbar_note)
        setSupportActionBar(toolbar)
        sqliteController = SQLiteControler.instance
        val actionBar = supportActionBar!!
        actionBar.setDisplayShowCustomEnabled(true)
        actionBar.setDisplayShowTitleEnabled(false)
        val v = LayoutInflater.from(this).inflate(R.layout.toolbar_title, toolbar)
        editTitle = v.findViewById(R.id.edit_title)
        editText = findViewById(R.id.edit_text)
        val v2 = findViewById<ViewPager2>(R.id.view_pager)
        pageAdapter = NoteImageListAdapter()
        v2.adapter = pageAdapter
        v2.setPageTransformer(DepthPageTransformer())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            v2.scrollIndicators = View.SCROLL_INDICATOR_BOTTOM
        }
        val indicator = findViewById<PageIndicatorView>(R.id.page_indicator)
        pageAdapter!!.setIndicator(indicator)
        indicator.radius = 4
        indicator.selectedColor = -0x13287a
        indicator.unselectedColor = -0x51868
        v2.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                indicator.selection = position
            }
        })
        note = intent.getParcelableExtra("note")
        if (note == null) {
            note = Note()
            editText?.hint = "빈 이미지를 터치해 새로운 이미지를 추가하세요\n\n텍스트를 입력하세요"
            pageAdapter!!.setImage(note?.imageList)
            haveToSave = true
        } else {
            editText?.setText(note?.title)
            editText?.setText(note?.text)
            pageAdapter!!.setImage(note?.imageList)
            editTitle?.isFocusable = false
            editTitle?.isFocusable = false
            pageAdapter!!.setImageEditable(false)
        }
    }

    override fun onPause() {
        if (!selectingImage && haveToSave) saveNote()
        super.onPause()
    }

    override fun onDestroy() {
        AppController.instance?.setCurrentNoteActivity(null)
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.actionbar_note, menu)
        if (note?.idx == Note.Companion.NEW_NOTE) {
            menu.findItem(R.id.action_edit).isVisible = false
            menu.findItem(R.id.action_delete).isVisible = false
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_edit -> {
                editTitle!!.isFocusable = true
                editTitle!!.isFocusableInTouchMode = true
                editText!!.isFocusable = true
                editText!!.isFocusableInTouchMode = true
                editText!!.hint = "빈 이미지를 터치해 새로운 이미지를 추가하세요\n\n텍스트를 입력하세요"
                pageAdapter!!.setImageEditable(true)
                item.isEnabled = false
                haveToSave = true
                return true
            }
            R.id.action_delete -> {
                AlertDialog.Builder(this).setCancelable(true).setMessage("삭제하시겠습니까?")
                        .setPositiveButton("삭제", this).setNegativeButton("취소", this).show()
                haveToSave = false
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        when (which) {
            AlertDialog.BUTTON_POSITIVE -> {
                sqliteController!!.deleteData(note?.idx!!)
                dialog.dismiss()
                finish()
            }
            AlertDialog.BUTTON_NEGATIVE -> dialog.dismiss()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        selectingImage = false
        var uri: Uri? = null
        if (data != null) {
            uri = data.data
        }
        if (uri == null) return
        val clip = data!!.clipData
        when (requestCode) {
            AttachOptionDialog.Companion.OPTION_CAMERA -> {
                val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                val path = intent.getStringExtra("path")
                val f = File(path)
                val contentUri = Uri.fromFile(f)
                mediaScanIntent.data = contentUri
                sendBroadcast(mediaScanIntent)
                pageAdapter!!.addImage(uri)
            }
            AttachOptionDialog.Companion.OPTION_GALLERY -> if (clip != null) {
                var i = 0
                while (i < clip.itemCount) {
                    pageAdapter!!.addImage(clip.getItemAt(i).uri)
                    i++
                }
            } else pageAdapter!!.addImage(uri)
            AttachOptionDialog.Companion.OPTION_URL -> pageAdapter!!.addImage(uri)
        }
    }

    fun setSelectingImage(b: Boolean) {
        selectingImage = b
    }

    fun saveNote() {
        val idx = note?.idx
        var title = editTitle!!.text.toString()
        val text = editText!!.text.toString()
        val image = note?.imageJson
        if (title == "" && text == "" && image == "[]") return
        if (idx == Note.Companion.NEW_NOTE) {
            //insertData
            if (title == "") title = "제목 없음"
            sqliteController!!.insertData(title, text, image)
        } else {
            //updateData
            sqliteController!!.updateData(idx!!, title, text, image)
        }
        Toast.makeText(this, "저장되었습니다", Toast.LENGTH_SHORT).show()
    }
}