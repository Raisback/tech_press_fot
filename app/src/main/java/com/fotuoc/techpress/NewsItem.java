package com.fotuoc.techpress;

public class NewsItem {
    private final String title;
    private final String body;
    private final String date;
    private final String type; // e.g., "Sports", "Academic", "Faculty Events"
    private final String imageUrl;

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

}