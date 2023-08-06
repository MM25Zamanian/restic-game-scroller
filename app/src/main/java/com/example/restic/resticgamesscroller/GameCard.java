package com.example.restic.resticgamesscroller;

import org.json.JSONArray;

public class GameCard {
    private String title;
    private Number meta;
    private JSONArray playType;
    private String imageUrl;
    private String link;

    public GameCard(String title, JSONArray playType, String imageUrl, Number meta, String link) {
        this.title = title;
        this.playType = playType;
        this.imageUrl = imageUrl;
        this.meta = meta;
        this.link=link;
    }

    public String getTitle() {
        return title;
    }

    public JSONArray getPlayType() {
        return playType;
    }

    public Number getMeta() {
        return meta;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public String getLink() {
        return link;
    }
}