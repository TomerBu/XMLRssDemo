package com.example.tomerbuzaglo.xmlrssdemo.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

import java.util.List;

@Root(name = "channel", strict = false)
public class Channel {
    @Element(required = false, data = true)
    private String pubDate;

    /*@Path(value = "title")*/
    @Path("title")
    @Text(required=false, data = true)
    //@Element(name = "title", required = false, data = true)
    private String title;

    @Element(required = false, data = true)
    private String description;

    //@Element(required = false, data = true)
    @Path("link")
    @Text(required=false, data = true)
    private String link;

    @Element(required = false, data = true)
    private String lastBuildDate;

    @Element(required = false, data = true)
    private String image;

    @Element(required = false, data = true)
    private String copyright;

    @Element(required = false, data = true)
    private String language;

    @ElementList(entry = "item", inline = true)
    private List<Item> items;

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
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

    public String getLastBuildDate() {
        return lastBuildDate;
    }

    public void setLastBuildDate(String lastBuildDate) {
        this.lastBuildDate = lastBuildDate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    //Get the items!
    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "pubDate='" + pubDate + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", link='" + link + '\'' +
                ", lastBuildDate='" + lastBuildDate + '\'' +
                ", image='" + image + '\'' +
                ", copyright='" + copyright + '\'' +
                ", language='" + language + '\'' +
                ", items=" + items +
                '}';
    }
}