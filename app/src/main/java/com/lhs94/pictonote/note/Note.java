package com.lhs94.pictonote.note;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class Note implements Parcelable {
    static final int NEW_NOTE = -1;

    private int idx;
    private String title;
    private String text;
    private ArrayList<Uri> imageList;

    Note() {
        idx = NEW_NOTE;
        imageList = new ArrayList<>();
        imageList.add(null);
    }

    public Note(int idx, String title, String text, String image) {
        this.idx = idx;
        this.title = title;
        this.text = text;
        imageList = jsonToList(image);
    }

    private Note(Parcel in) {
        idx = in.readInt();
        title = in.readString();
        text = in.readString();
        String image = in.readString();
        imageList = jsonToList(image);

    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    int getIdx() {
        return idx;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    ArrayList<Uri> getImageList() {
        return imageList;
    }

    String getImageJson() {
        imageList.remove(null);
        return listToJson(imageList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idx);
        dest.writeString(title);
        dest.writeString(text);
        String image = listToJson(imageList);
        dest.writeString(image);
    }

    private ArrayList<Uri> jsonToList(String image) {
        ArrayList<Uri> imageList = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(image);
            for(int i = 0; i < array.length(); i++) {
                Uri uri = Uri.parse(array.getString(i));
                imageList.add(uri);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return imageList;
    }

    private String listToJson(ArrayList<Uri> imageList) {
        String image;

        JSONArray array = new JSONArray();
        for(int i = 0; i < imageList.size(); i++) {
            array.put(imageList.get(i));
        }
        image = array.toString();
        return image;
    }

    public Uri getThumbnailUri() {
        return imageList.size() == 0 ? null : imageList.get(0);
    }
}
