package com.gamadevlopment.biljeske.Adapters;

public class Note {

    private String Title;
    private String Text;

    private String id;
    private String documentId;

    public Note(String title, String text, String id, String documentId) {
        Title = title;
        Text = text;
        this.id = id;
        this.documentId = documentId;
    }


    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}
