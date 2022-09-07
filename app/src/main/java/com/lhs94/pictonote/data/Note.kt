package com.lhs94.pictonote.data

import android.os.Parcelable
import android.os.Parcel
import org.json.JSONArray
import org.json.JSONException
import android.os.Parcelable.Creator
import android.net.Uri
import java.util.ArrayList

class Note : Parcelable {
    var idx: Int
        private set
    var title: String? = null
        private set
    var text: String? = null
        private set
    var imageList: ArrayList<Uri?>
        private set

    internal constructor() {
        idx = NEW_NOTE
        imageList = ArrayList()
        imageList.add(null)
    }

    constructor(idx: Int, title: String?, text: String?, image: String?) {
        this.idx = idx
        this.title = title
        this.text = text
        imageList = jsonToList(image)
    }

    private constructor(`in`: Parcel) {
        idx = `in`.readInt()
        title = `in`.readString()
        text = `in`.readString()
        val image = `in`.readString()
        imageList = jsonToList(image)
    }

    val imageJson: String
        get() {
            imageList.remove(null)
            return listToJson(imageList)
        }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(idx)
        dest.writeString(title)
        dest.writeString(text)
        val image = listToJson(imageList)
        dest.writeString(image)
    }

    private fun jsonToList(image: String?): ArrayList<Uri?> {
        val imageList = ArrayList<Uri?>()
        try {
            val array = JSONArray(image)
            for (i in 0 until array.length()) {
                val uri = Uri.parse(array.getString(i))
                imageList.add(uri)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return imageList
    }

    private fun listToJson(imageList: ArrayList<Uri?>): String {
        val image: String
        val array = JSONArray()
        for (i in imageList.indices) {
            array.put(imageList[i])
        }
        image = array.toString()
        return image
    }

    val thumbnailUri: Uri?
        get() = if (imageList.size == 0) null else imageList[0]

    companion object {
        const val NEW_NOTE = -1
        @JvmField
        val CREATOR: Creator<Note?> = object : Creator<Note?> {
            override fun createFromParcel(`in`: Parcel): Note? {
                return Note(`in`)
            }

            override fun newArray(size: Int): Array<Note?> {
                return arrayOfNulls(size)
            }
        }
    }

}