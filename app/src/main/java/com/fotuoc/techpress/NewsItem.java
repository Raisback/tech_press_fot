package com.fotuoc.techpress;

public class NewsItem {
    private String title;
    private String body;
    private String date;
    private String type; // e.g., "Sports", "Academic", "Faculty Events"
    private String imageUrl;

    // Required no-argument constructor for Firebase (good practice even with dummy data)
    public NewsItem() {
        // Default constructor required for calls to DataSnapshot.toObject(NewsItem.class)
    }

    public NewsItem(String title, String body, String date, String type, String imageUrl) {
        this.title = title;
        this.body = body;
        this.date = date;
        this.type = type;
        this.imageUrl = imageUrl;
    }

    // --- Getters ---
    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    // --- Setters (optional, but good for data manipulation if needed) ---
    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}