package com.example.tomerbuzaglo.xmlrssdemo.model;

import com.google.gson.Gson;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "rss")
public class Rss {

    @Element(required = false)
    private Channel channel;

    @Attribute(required = false)
    private String version;


    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Rss{" +
                "channel=" + channel +
                ", version='" + version + '\'' +
                '}';
    }

    public String json() {
        Gson g = new Gson();
        return g.toJson(this);
    }

    public static Rss fromJson(String json) {
        Gson g = new Gson();
        return g.fromJson(json, Rss.class);
    }
}

