package com.gamadevlopment.biljeske.Adapters;

public interface firebase_editing {

   public void writeDataToFireBase(String userID, String title, String text);

   public void deleteDataFromFireBase(String title, String text, String id, String documentId);

    void updateDataToFireBase(String title, String text, String id, String documentId);

    void retriveDataFromFireBase(String userID);

    //runEditFragmentHelper
    void runEditFragmentHelper(Note note);

}
