package com.lhs94.pictonote.note.image

import com.lhs94.pictonote.R
import android.content.Intent
import android.app.Activity
import com.lhs94.pictonote.AppController
import android.provider.MediaStore
import android.os.Environment
import android.widget.Toast
import androidx.core.content.FileProvider
import android.content.Context
import android.view.View
import androidx.appcompat.app.AlertDialog
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AttachOptionDialog internal constructor(context: Context) : AlertDialog(context) {
    private inner class AttachOptionListener internal constructor(var context: Context) : View.OnClickListener {
        override fun onClick(v: View) {
            val intent = Intent()
            val ac = context as Activity
            when (v.id) {
                R.id.attach_camera -> {
                    AppController.instance?.setSelectingImage(true)
                    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    if (takePictureIntent.resolveActivity(ac.packageManager) != null) {
                        var image: File? = null
                        val currentPhotoPath: String
                        try {
                            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.KOREA).format(Date())
                            val imageFileName = "JPEG_" + timeStamp + "_"
                            //File storageDir = ac.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                            var storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                            storageDir = File(storageDir, "PictoNote")
                            if (!storageDir.exists()) {
                                if (storageDir.mkdir()) Toast.makeText(context, "fail to mkdir..", Toast.LENGTH_SHORT).show()
                            }
                            image = File.createTempFile(imageFileName, ".jpg", storageDir)
                            currentPhotoPath = image.absolutePath
                            val path = Intent()
                            path.putExtra("path", currentPhotoPath)
                            (context as Activity).intent = path
                        } catch (ex: IOException) {
                            ex.printStackTrace()
                        }
                        if (image != null) {
                            val photoURI = FileProvider.getUriForFile(context, "com.lhs94.pictonote.fileprovider", image)
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                            ac.startActivityForResult(takePictureIntent, OPTION_CAMERA)
                        }
                    }
                }
                R.id.attach_gallery -> {
                    AppController.instance?.setSelectingImage(true)
                    intent.action = Intent.ACTION_PICK
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                    intent.type = "image/*"
                    ac.startActivityForResult(intent, OPTION_GALLERY)
                }
                R.id.attach_url -> {
                    AppController.instance?.setSelectingImage(true)
                    intent.setClass(context, UrlOptionActivity::class.java)
                    ac.startActivityForResult(intent, OPTION_URL)
                }
            }
            dismiss()
        }
    }

    companion object {
        const val OPTION_CAMERA = 0
        const val OPTION_GALLERY = 1
        const val OPTION_URL = 2
    }

    init {
        val v = layoutInflater.inflate(R.layout.dialog_options_attach_image, null)
        val listener = AttachOptionListener(context)
        v.findViewById<View>(R.id.attach_camera).setOnClickListener(listener)
        v.findViewById<View>(R.id.attach_gallery).setOnClickListener(listener)
        v.findViewById<View>(R.id.attach_url).setOnClickListener(listener)
        setView(v)
        setCancelable(true)
    }
}