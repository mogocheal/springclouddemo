package com.example.cmiss.model.swan;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LM on 2017-08-23.
 */
public class SwanGridData {
    public double[] startPoint;
    public double[] endPoint;
    public Map<String, Object> style = new HashMap<>();
    public double value;

    public SwanGridData() {
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double[] getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(double[] startPoint) {
        this.startPoint = startPoint;
    }

    public double[] getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(double[] endPoint) {
        this.endPoint = endPoint;
    }

    public Map<String, Object> getStyle() {
        return style;
    }

    public void setStyle(Map<String, Object> style) {
        this.style = style;
    }
}
