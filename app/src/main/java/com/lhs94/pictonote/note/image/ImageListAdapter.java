package com.lhs94.pictonote.note.image;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lhs94.pictonote.R;
import com.rd.PageIndicatorView;

import java.util.ArrayList;

public class ImageListAdapter extends RecyclerView.Adapter {

    private Context context;

    private boolean imageEditable = true;

    private ArrayList<Uri> imageList;
    private PageIndicatorView pageIndicator;

    public ImageListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_note_image, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder vh = (ViewHolder) holder;
        vh.imageUri = imageList.get(position);
        vh.setViews();
        Glide.with(context).load(vh.imageUri).error(R.drawable.image_empty).into(vh.image);
    }

    @Override
    public int getItemCount() {
        return imageList == null ? 0 : imageList.size();
    }

    public void setImage(ArrayList<Uri> imageList) {
        this.imageList = imageList;
        if(imageList.size() == 0)
            imageList.add(null);
        pageIndicator.setCount(imageList.size());
        notifyDataSetChanged();
    }

    public void setImageEditable(boolean b) {
        imageEditable = b;
        if(b && !(imageList.lastIndexOf(null) == 0)) {
            imageList.add(null);
            pageIndicator.setCount(imageList.size());
            notifyDataSetChanged();
        }
    }

    public void addImage(Uri uri) {
        imageList.set(imageList.size() - 1, uri);
        imageList.add(null);
        pageIndicator.setCount(imageList.size());
        notifyDataSetChanged();
    }

    public void removeImage(Uri uri) {
        imageList.remove(uri);
        pageIndicator.setCount(imageList.size());
        notifyDataSetChanged();
    }

    public void setIndicator(PageIndicatorView indicator) {
        pageIndicator = indicator;
    }

    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView image;
        ImageView delete;
        Uri imageUri;

        private boolean hasImage = false;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            delete = itemView.findViewById(R.id.icon_delete);

            image.setOnClickListener(this);
            delete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.image :
                    if(imageEditable && !hasImage) {
                        AttachOptionDialog dialog = new AttachOptionDialog(context);
                        dialog.show();
                    }
                    break;

                case R.id. icon_delete :
                    removeImage(imageUri);
                    break;
            }
        }

        void setViews() {
            hasImage = imageUri != null;
            if(hasImage && imageEditable) {
                delete.setVisibility(View.VISIBLE);
                delete.setEnabled(true);
            }
            else {
                delete.setVisibility(View.INVISIBLE);
                delete.setEnabled(false);
            }
        }
    }

}
