package com.example.tomerbuzaglo.xmlrssdemo.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "item")
public class Item {
    @Element(required = false, data = true)
    private String guid;

    @Element(required = false, data = true)
    private String tags;

    @Element(required = false, data = true)
    private String pubDate;

    @Element(required = false, data = true)
    private String category;

    @Element(required = false, data = true)
    private String title;

    @Element(required = false, data = true)
    private String description;

    @Element(required = false, data = true)
    private String link;


    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "Item{" +
                "guid='" + guid + '\'' +
                ", tags='" + tags + '\'' +
                ", pubDate='" + pubDate + '\'' +
                ", category='" + category + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}
