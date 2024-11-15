package com.example.pruebaappredsocial;

import android.os.Parcel;
import android.os.Parcelable;

public class Comment implements Parcelable {
    private String content;
    private String authorName;
    private String authorLastName;
    // Constructor principal
    public Comment(String content, String authorName, String authorLastName) {
        this.content = content;
        this.authorName = authorName;
        this.authorLastName = authorLastName;
    }

    // Constructor para Parcel
    protected Comment(Parcel in) {
        content = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(content);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Getter para obtener el contenido del comentario
    public String getContent() {
        return content;
    }
    public String getAuthorName() {return authorName;}

    public String getAuthorLastName() {return authorLastName;}

    // Parcelable Creator
    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };
}
