package com.codrata.notefinder;

public class RepObject {
    public int numOfBooks;
    private int thumbnail;

    public RepObject(){

    }

    public RepObject(int numOfBooks, int thumbnail){
        this.numOfBooks = numOfBooks;
        this.thumbnail = thumbnail;
    }
    public int getNumOfBooks(){ return numOfBooks;}

    public int getBookThumbnail(){return thumbnail;}

    public void setThumbnail(int thumbnail){
        this.thumbnail = thumbnail;
    }

    public void setNumOfBooks(int numOfBooks){
        this.numOfBooks = numOfBooks;
    }
}
