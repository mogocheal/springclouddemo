package com.example.cmiss.model.geojson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LM on 2017-08-28.
 */
public class FeatureCollection {
    private final String type = "FeatureCollection";
    private List<Feature> features = new ArrayList<>();

    public String getType() {
        return type;
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }
}
