package com.example.soundlevelmeter.ui.notes;

import java.io.Serializable;

public class Note {

    private String nameNote;
    private String contentNote;

    public Note(String nameNote, String contentNote) {
        this.contentNote = contentNote;
        this.nameNote = nameNote;
    }

    public String getNameNote() {
        return nameNote;
    }

    public void setNameNote(String nameNote) {
        this.nameNote = nameNote;
    }

    public String getContentNote() {
        return contentNote;
    }

    public void setContentNote(String contentNote) {
        this.contentNote = contentNote;
    }
}
