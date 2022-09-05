package com.lhs94.pictonote.note.image

import androidx.recyclerview.widget.RecyclerView
import com.rd.PageIndicatorView
import android.view.ViewGroup
import android.view.LayoutInflater
import com.lhs94.pictonote.R
import com.bumptech.glide.Glide
import android.content.Context
import android.net.Uri
import android.view.View
import android.widget.ImageView
import java.util.ArrayList

class ImageListAdapter(private val context: Context) : RecyclerView.Adapter<ImageListAdapter.ViewHolder>() {
    private var imageEditable = true
    private var imageList: ArrayList<Uri?>? = null
    private var pageIndicator: PageIndicatorView? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.item_note_image, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ImageListAdapter.ViewHolder, position: Int) {
        val vh = holder as ViewHolder
        vh.imageUri = imageList!![position]
        vh.setViews()
        Glide.with(context).load(vh.imageUri).error(R.drawable.image_empty).into(vh.image)
    }

    override fun getItemCount(): Int {
        return if (imageList == null) 0 else imageList!!.size
    }

    fun setImage(imageList: ArrayList<Uri?>?) {
        this.imageList = imageList
        if (imageList!!.size == 0) imageList.add(null)
        pageIndicator!!.count = imageList.size
        notifyDataSetChanged()
    }

    fun setImageEditable(b: Boolean) {
        imageEditable = b
        if (b && imageList!!.lastIndexOf(null) != 0) {
            imageList!!.add(null)
            pageIndicator!!.count = imageList!!.size
            notifyDataSetChanged()
        }
    }

    fun addImage(uri: Uri?) {
        imageList!![imageList!!.size - 1] = uri
        imageList!!.add(null)
        pageIndicator!!.count = imageList!!.size
        notifyDataSetChanged()
    }

    fun removeImage(uri: Uri?) {
        imageList!!.remove(uri)
        pageIndicator!!.count = imageList!!.size
        notifyDataSetChanged()
    }

    fun setIndicator(indicator: PageIndicatorView?) {
        pageIndicator = indicator
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var image: ImageView
        var delete: ImageView
        var imageUri: Uri? = null
        private var hasImage = false
        override fun onClick(v: View) {
            when (v.id) {
                R.id.image -> if (imageEditable && !hasImage) {
                    val dialog = AttachOptionDialog(context)
                    dialog.show()
                }
                R.id.icon_delete -> removeImage(imageUri)
            }
        }

        fun setViews() {
            hasImage = imageUri != null
            if (hasImage && imageEditable) {
                delete.visibility = View.VISIBLE
                delete.isEnabled = true
            } else {
                delete.visibility = View.INVISIBLE
                delete.isEnabled = false
            }
        }

        init {
            image = itemView.findViewById(R.id.image)
            delete = itemView.findViewById(R.id.icon_delete)
            image.setOnClickListener(this)
            delete.setOnClickListener(this)
        }
    }
}