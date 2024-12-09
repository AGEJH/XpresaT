package com.example.pruebaappredsocial;

public class Alert {
    private String title;
    private String reason;
    private String date;
    private String type;

    public Alert(String title, String reason, String date, String type) {
        this.title = title;
        this.reason = reason;
        this.date = date;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public String getReason() {
        return reason;
    }

    public String getDate() {
        return date;
    }

    public String getType() {
        return type;
    }
}


