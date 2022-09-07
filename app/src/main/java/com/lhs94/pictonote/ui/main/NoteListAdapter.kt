package com.lhs94.pictonote.ui.main

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lhs94.pictonote.R
import com.lhs94.pictonote.data.Note
import com.lhs94.pictonote.note.NoteActivity
import java.util.*

class NoteListAdapter() : RecyclerView.Adapter<NoteListAdapter.ViewHolder>() {
    private var notes: ArrayList<Note>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_note_list, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val vh = holder as ViewHolder
        val note = notes[position]
        //vh.position = position
        vh.title.text = note.title
        vh.text.text = note.text
        val uri = note.thumbnailUri
        Glide.with(vh.thumbnail.context).load(uri).error(R.drawable.image_empty).into(vh.thumbnail)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    fun updateData(notes: ArrayList<Note>) {
        this.notes = notes
        notifyDataSetChanged()
    }



    init {
        notes = ArrayList()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var pos: Int = 0
        var thumbnail: ImageView
        var title: TextView
        var text: TextView

        /*override fun onClick(v: View) {
            val intent = Intent(context, NoteActivity::class.java)
            intent.putExtra("note", notes[pos])
            context.startActivity(intent)
        }*/

        init {
            //itemView.setOnClickListener(this)
            thumbnail = itemView.findViewById(R.id.thumbnail)
            title = itemView.findViewById(R.id.title)
            text = itemView.findViewById(R.id.text)
        }
    }
}

