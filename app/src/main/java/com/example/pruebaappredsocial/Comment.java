package com.example.pruebaappredsocial;

import android.os.Parcel;
import android.os.Parcelable;

public class Comment implements Parcelable {
    private String content;

    // Constructor principal
    public Comment(String content) {
        this.content = content;
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
