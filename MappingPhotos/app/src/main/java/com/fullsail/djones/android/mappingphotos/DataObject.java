package com.fullsail.djones.android.mappingphotos;

import android.graphics.Bitmap;

import java.io.Serializable;

public class DataObject implements Serializable {

    private static final long serialVersionUID = 738493756273847562L;

    private String mName;
    private String mNote;
    private Bitmap mBitmap;

    public DataObject(){
        // Empty Constructor
    }

    public DataObject(String _name, String _note, Bitmap _bitmap){
        mName = _name;
        mNote = _note;
        mBitmap = _bitmap;
    }

    public String getName() { return mName; }

    public String getNote() { return mNote; }

    public Bitmap getBitmap() { return mBitmap; }

    public void setName(String mName) { this.mName = mName; }

    public void setNote(String mNote) { this.mNote = mNote; }

    public void setBitmap(Bitmap mBitmap) { this.mBitmap = mBitmap; }
}
