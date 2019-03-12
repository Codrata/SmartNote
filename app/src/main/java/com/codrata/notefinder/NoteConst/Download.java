package com.codrata.notefinder.NoteConst;

public class Download {

    public String name;
    public String url;


    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Download() {
    }

    public Download(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }


}
