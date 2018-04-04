package com.example.cmiss.model;

import java.util.Properties;

public class FeatureCollection {

    private String type;           //默认FeatureCollection类型
    private Feature feature;       //点、线、面几何数据对象
    private Properties properties; //元数据的属性信息：包括数据类型、数据说明、文件名、数据日期以及其他可扩充属性

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Feature getFeature() {
        return feature;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

}
