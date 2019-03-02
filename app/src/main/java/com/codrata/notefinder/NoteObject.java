package com.codrata.notefinder;

public class NoteObject {
    private String noteName,
            uRL;

    public NoteObject (String noteName, String uRL){
        this.noteName = noteName;
        this.uRL = uRL;
    }


    public String getNoteName(){return noteName;}
    public String getuRL(){return uRL;}
}
