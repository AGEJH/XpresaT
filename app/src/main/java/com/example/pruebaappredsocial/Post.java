package com.example.pruebaappredsocial;

import android.os.Parcel;
import android.os.Parcelable;

public class Post implements Parcelable {
    private String content;
    private String author;
    private String username;
    private String userProfileImage;

    public Post(String content, String author) {
        this.content = content;
        this.author = author;
    }

    protected Post(Parcel in) {
        content = in.readString();
        author = in.readString();
        username = in.readString();
        userProfileImage = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(content);
        dest.writeString(author);
        dest.writeString(username);
        dest.writeString(userProfileImage);
    }

    @Override
    public int describeContents() {
        return 0;
    }

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

    // Getters
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
}
