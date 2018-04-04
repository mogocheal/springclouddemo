package com.example.cmiss.model.geojson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LM on 2017-08-28.
 */
public class Geometry {
    private String type;
    private List<List<Double[]>> coordinates = new ArrayList<>();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<List<Double[]>> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<List<Double[]>> coordinates) {
        this.coordinates = coordinates;
    }

    public Geometry() {
    }

}
