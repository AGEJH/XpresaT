package com.example.pruebaappredsocial;

public class FriendStatusResponse {
    private boolean isFriend;
    private boolean requestSent;

    // Constructor
    public FriendStatusResponse(boolean isFriend, boolean requestSent) {
        this.isFriend = isFriend;
        this.requestSent = requestSent;
    }

    // Getters y setters
    public boolean isFriend() {
        return isFriend;
    }

    public void setFriend(boolean friend) {
        isFriend = friend;
    }

    public boolean isRequestSent() {
        return requestSent;
    }

    public void setRequestSent(boolean requestSent) {
        this.requestSent = requestSent;
    }
}
