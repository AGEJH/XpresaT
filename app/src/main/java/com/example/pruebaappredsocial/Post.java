package com.example.pruebaappredsocial;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Post implements Parcelable {
    private String content;
    private String author;
    private String username;
    private String userProfileImage;
    private int likesCount;
    private boolean isLiked;
    private long timestamp;
    private ArrayList<Comment>  comments = new ArrayList<>();

    // Constructor principal
    public Post(String content, String author, long timestamp) {
        this.content = content;
        this.author = author;
        this.timestamp = timestamp;
        this.comments = new ArrayList<>();  // Inicialización de la lista de comentarios
    }

    // Constructor para Parcel
    protected Post(Parcel in) {
        content = in.readString();
        author = in.readString();
        username = in.readString();
        userProfileImage = in.readString();
        likesCount = in.readInt();
        isLiked = in.readByte() != 0;
        timestamp = in.readLong();
        comments = in.createTypedArrayList(Comment.CREATOR); // Leer lista de comentarios desde el Parcel
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(content);
        dest.writeString(author);
        dest.writeString(username);
        dest.writeString(userProfileImage);
        dest.writeInt(likesCount);
        dest.writeByte((byte) (isLiked ? 1 : 0));
        dest.writeLong(timestamp);
        dest.writeTypedList(comments); // Escribir lista de comentarios en el Parcel
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Getters y setters (agrega otros si los necesitas)
    public int getLikesCount() {
        return likesCount;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public String getUsername() {
        return username;
    }

    public String getUserProfileImage() {
        return userProfileImage;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    // Parcelable Creator
    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };
}
