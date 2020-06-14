package com.lhs94.pictonote;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lhs94.pictonote.note.Note;
import com.lhs94.pictonote.note.NoteActivity;

import java.util.ArrayList;

public class NoteListAdapter extends RecyclerView.Adapter {

    private ArrayList<Note> notes;

    private Context context;

    NoteListAdapter(Context context) {
        this.context = context;
        notes = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_note_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder vh = (ViewHolder)holder;
        Note note = notes.get(position);
        vh.position = position;
        vh.title.setText(note.getTitle());
        vh.text.setText(note.getText());
        Uri uri = note.getThumbnailUri();

        Glide.with(context).load(uri).error(R.drawable.image_empty).into(vh.thumbnail);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    void updateData(ArrayList<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        int position;

        ImageView thumbnail;
        TextView title;
        TextView text;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            thumbnail = itemView.findViewById(R.id.thumbnail);
            title  =  itemView.findViewById(R.id.title);
            text = itemView.findViewById(R.id.text);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, NoteActivity.class);
            intent.putExtra("note", notes.get(position));
            context.startActivity(intent);
        }
    }
}
