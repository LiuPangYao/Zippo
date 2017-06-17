package com.example.user.zippo.data;

/**
 * 棄用
 */
public class DataNote
{
    String title;
    String date;
    String url;

    public DataNote(String title, String date, String url)
    {
        this.title = title;
        this.date = date;
        this.url = url;
    }

    public String getTitle()
    {
        return title;
    }

    public String getDate()
    {
        return date;
    }

    public String getUrl()
    {
        return url;
    }
}
