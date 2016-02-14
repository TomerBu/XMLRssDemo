package com.example.tomerbuzaglo.xmlrssdemo.eventbus;

import com.example.tomerbuzaglo.xmlrssdemo.model.Rss;

/**
 * Created by tomerbuzaglo on 14/02/2016.
 */
public class YnetEvent {
    private Rss rss;

    public Rss getRss() {
        return rss;
    }

    public void setRss(Rss rss) {
        this.rss = rss;
    }

    public YnetEvent(Rss rss) {

        this.rss = rss;
    }
}
