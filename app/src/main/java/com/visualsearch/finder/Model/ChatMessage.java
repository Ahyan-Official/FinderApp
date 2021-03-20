package com.visualsearch.finder.Model;

public class ChatMessage {
    String content;
    boolean isMine, iImage;


    public ChatMessage() {
    }

    public ChatMessage(String content, boolean isMine, boolean iImage) {
        this.content = content;
        this.isMine = isMine;
        this.iImage = iImage;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public boolean isiImage() {
        return iImage;
    }

    public void setiImage(boolean iImage) {
        this.iImage = iImage;
    }
}
