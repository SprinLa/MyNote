package com.mycompany.mynote.data;

/**
 * Created by delia on 16/5/20.
 */
public class NoteItem {
    public final Integer id;
    public final String title;
    public final String date;
    public final String details;

    public NoteItem(Integer id,String title, String details,String strDate) {
        this.id = id;
        this.title = title;
        this.date = strDate;
        this.details = details;
    }

    @Override
    public String toString() {
        return title;
    }
}
