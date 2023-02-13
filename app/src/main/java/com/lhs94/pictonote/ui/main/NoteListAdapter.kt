package com.lhs94.pictonote.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.lhs94.pictonote.R
import com.lhs94.pictonote.databinding.ItemNoteListBinding
import com.lhs94.pictonote.room.entity.Note
import java.util.*

class NoteListAdapter : RecyclerView.Adapter<NoteListViewHolder>() {
    var notes: ArrayList<Note> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val v = DataBindingUtil.inflate<ItemNoteListBinding>(layoutInflater, R.layout.item_note_list, parent, false)
        v.adapter = this
        return NoteListViewHolder(v)
    }

    override fun onBindViewHolder(holder: NoteListViewHolder, position: Int) {
        holder.binding.apply {
            note = notes[position]
            index = position
        }
        //val uri = note.thumbnailUri
        //Glide.with(vh.thumbnail.context).load(uri).error(R.drawable.image_empty).into(vh.thumbnail)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    fun updateData(notes: List<Note>) {
        this.notes.clear()
        this.notes.addAll(notes)
        notifyDataSetChanged()
    }

    fun onClickNoteListItem(item: View, index: Int) {
        val note = notes[index]
        item.findNavController().navigate(R.id.noteFragment, bundleOf("note" to note))
    }
}

class NoteListViewHolder(val binding: ItemNoteListBinding) : RecyclerView.ViewHolder(binding.root)

