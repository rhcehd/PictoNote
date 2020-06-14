package com.lhs94.pictonote;

import com.lhs94.pictonote.note.NoteActivity;

public class AppControler {

    private static AppControler instance = null;

    private NoteActivity currentNoteActivity;

    private AppControler() {

    }

    public static AppControler getInstance() {
        if(instance == null) {
            instance = new AppControler();
        }
        return instance;
    }

    public void setCurrentNoteActivity(NoteActivity noteActivity) {
        currentNoteActivity = noteActivity;
    }

    public void setSelectingImage(boolean b) {
        currentNoteActivity.setSelectingImage(b);
    }
}
