package com.example.cmiss.model.swan;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class GenerateInfo implements Serializable {

    private BufferedImage bImage;
    private double xMin;
    private double yMin;
    private double xMax;
    private double yMax;
    private String prdtType;
    private List<HashMap<String, Object>> colors;
    private double height;

    public List<HashMap<String, Object>> getColors() {
        return colors;
    }

    public void setColors(List<HashMap<String, Object>> colors) {
        this.colors = colors;
    }

    public BufferedImage getBImage() {
        return bImage;
    }

    public void setBImage(BufferedImage image) {
        bImage = image;
    }

    public double getXMin() {
        return xMin;
    }

    public void setXMin(double min) {
        xMin = min;
    }

    public double getYMin() {
        return yMin;
    }

    public void setYMin(double min) {
        yMin = min;
    }

    public double getXMax() {
        return xMax;
    }

    public void setXMax(double max) {
        xMax = max;
    }

    public double getYMax() {
        return yMax;
    }

    public void setYMax(double max) {
        yMax = max;
    }

    public String getPrdtType() {
        return prdtType;
    }

    public void setPrdtType(String prdtType) {
        this.prdtType = prdtType;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }
}
