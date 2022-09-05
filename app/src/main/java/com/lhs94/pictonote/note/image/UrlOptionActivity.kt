package com.lhs94.pictonote.note.image

import com.lhs94.pictonote.R
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.content.DialogInterface
import android.widget.EditText
import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AlertDialog

class UrlOptionActivity : AppCompatActivity() {
    private val intent = Intent()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UrlOptionDialog(this).show()
    }

    private inner class UrlOptionDialog internal constructor(context: Context) : AlertDialog(context), DialogInterface.OnClickListener, DialogInterface.OnCancelListener {
        var editText: EditText
        override fun onClick(dialog: DialogInterface, which: Int) {
            val url = editText.text.toString()
            val uri = Uri.parse(url)
            intent.data = uri
            setResult(RESULT_OK, intent)
            finish()
        }

        override fun onCancel(dialog: DialogInterface) {
            setResult(RESULT_CANCELED, intent)
            finish()
        }

        init {
            val v = layoutInflater.inflate(R.layout.dialog_option_url, null)
            editText = v.findViewById(R.id.url_edit)
            setView(v)
            setCancelable(true)
            setOnCancelListener(this)
            setButton(BUTTON_POSITIVE, "완료", this)
        }
    }
}