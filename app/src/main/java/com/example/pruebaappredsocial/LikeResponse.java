package com.example.pruebaappredsocial;

public class LikeResponse {
    private boolean isLiked;
    private int likesCount;

    public boolean isLiked() {
        return isLiked;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLiked(boolean liked) {
        this.isLiked = liked;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }
}
