package com.example.soundlevelmeter.ui.notes;


class Note {

    private String nameNote;
    private String contentNote;

    Note(String nameNote, String contentNote) {
        this.contentNote = contentNote;
        this.nameNote = nameNote;
    }

    String getNameNote() {
        return nameNote;
    }

    void setNameNote(String nameNote) {
        this.nameNote = nameNote;
    }

    String getContentNote() {
        return contentNote;
    }

    void setContentNote(String contentNote) {
        this.contentNote = contentNote;
    }
}
