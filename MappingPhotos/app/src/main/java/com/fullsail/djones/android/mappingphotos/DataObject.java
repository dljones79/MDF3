// David Jones
// MDF3 Week 4
// Mapping Photos
// Full Sail University

package com.fullsail.djones.android.mappingphotos;

import java.io.Serializable;

public class DataObject implements Serializable {

    private static final long serialVersionUID = 738493756273847562L;

    private String mName;
    private String mNote;
    private String mUri;
    private Double mLatitude;
    private Double mLongitude;

    public DataObject(){
        // Empty Constructor
    }

    public DataObject(String _name, String _note, String _uri, Double _latitude, Double _longitude){
        mName = _name;
        mNote = _note;
        mUri = _uri;
        mLatitude = _latitude;
        mLongitude = _longitude;
    }

    public String getName() { return mName; }

    public String getNote() { return mNote; }

    public String getUri() { return mUri; }

    public Double getLatitude() { return mLatitude; }

    public Double getLongitude() { return mLongitude; }

    public void setName(String mName) { this.mName = mName; }

    public void setNote(String mNote) { this.mNote = mNote; }

    public void setUri(String mUri) { this.mUri = mUri; }

    public void setLatitude(Double mLatitude) { this.mLatitude = mLatitude; }

    public void setLongitude(Double mLongitude) { this.mLongitude = mLongitude; }
}
