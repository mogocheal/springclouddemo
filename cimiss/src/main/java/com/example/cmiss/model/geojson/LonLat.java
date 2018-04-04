package com.example.cmiss.model.geojson;

/**
 * Created by LM on 2017-08-28.
 */
public class LonLat {
    private Double[] lonlat = new Double[2];

    public Double[] getLonlat() {
        return lonlat;
    }

    public void setLonlat(Double[] lonlat) {
        this.lonlat = lonlat;
    }

    public LonLat() {
    }

    public LonLat(Double[] lonlat) {
        this.lonlat = lonlat;
    }
}
