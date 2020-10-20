package fr.nonoland.nonorss.utils;

import fr.nonoland.nonoutils.logs.Logs;

public class Article {

    private String title;
    private String link;
    private String description;


    public Article(String title, String url, String description) {
        this.title = title;
        this.link = url;
        this.description = description;
    }

    public String getName() {
        return this.title;
    }

    public String getLink() {
        return this.link;
    }

    public String getDescription() {
        return this.description;
    }

    public String toString() {
        return getName();
    }
}
