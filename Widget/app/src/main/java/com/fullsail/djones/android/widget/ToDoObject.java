//////////////////////////
// David Jones          //
// MDF-3 1409           //
// Week 3 Widgets       //
// Full Sail University //
//////////////////////////

package com.fullsail.djones.android.widget;

import java.io.Serializable;

public class ToDoObject implements Serializable {

    private static final long serialVersionUID = 738493756273847562L;

    private String mEvent;
    private String mDate;
    private String mNotes;

    public ToDoObject(){

    }

    public ToDoObject(String _event, String _date, String _notes){
        mEvent = _event;
        mDate = _date;
        mNotes = _notes;
    }

    public String getEvent() { return mEvent; }

    public String getDate() { return mDate; }

    public String getNotes() { return mNotes; }

    public void setEvent(String mEvent) {
        this.mEvent = mEvent;
    }

    public void setDate(String mDate) {
        this.mDate = mDate;
    }

    public void setNotes(String mNotes) {
        this.mNotes = mNotes;
    }
}
