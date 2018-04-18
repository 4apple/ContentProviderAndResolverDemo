package com.mango.contentproviderandresolverdemo;

import android.net.Uri;

/**
 * class
 * package com.mango.contentproviderandresolverdemo
 *
 * @author swd1
 * Date 18-4-10
 */
public class Article {

    Article(int id, String title, String abs, String url) {
        this.id = id;
        this.title = title;
        this.abs = abs;
        this.url = url;
    }
    /**
     * data field
     */
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbstract() {
        return abs;
    }

    public void setAbstract(String abs) {
        this.abs = abs;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String title;
    private String abs;
    private String url;
}
