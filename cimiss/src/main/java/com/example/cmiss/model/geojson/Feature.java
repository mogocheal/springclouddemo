package com.example.cmiss.model.geojson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LM on 2017-08-28.
 */
public class Feature {
    private Map<String, Object> properties = new HashMap<String, Object>();
    private Geometry geometry;
    private final String type = "Feature";

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public String getType() {
        return type;
    }
}
