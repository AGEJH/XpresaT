package com.example.pruebaappredsocial;

public class Post {
    private String content;
    private String author;

    public Post(String content, String author) {
        this.content = content;
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }
}
