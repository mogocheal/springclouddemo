package com.example.cmiss.model.swan;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class RadarColor {
//	public static void main(String[] args) {
//		try {
//			loadRadarColorConfig();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

    public static HashMap<String, Object> loadRadarColorConfig() throws IOException, SAXException, ParserConfigurationException {
        HashMap<String, Object> colorConfigMap = new LinkedHashMap<String, Object>();
        Document doc = null;
        InputStream is = RadarColor.class.getResourceAsStream("/static/palettes/RadarColorConfig.xml");
        if (is != null) {
            doc = DOMUtils.readXml(is);
            is.close();
            is = null;
            if (doc != null) {
                NodeList prdctColorList = doc.getElementsByTagName("PrdctColor");
                int count = prdctColorList.getLength();
                Node prdctColorNode = null;
                String prdctType = null;
                HashMap<String, Object> infoMap = null;
                List<HashMap<String, Object>> colors = null;
                HashMap<String, Object> colorMap = null;
                NodeList colorNodeList = null;
                Node colorNode = null;
                String value = null;
                String rgb = null;
                String[] min_max = null;
                for (int i = 0; i < count; i++) {
                    infoMap = new LinkedHashMap<String, Object>();
                    colors = new ArrayList<HashMap<String, Object>>();
                    prdctColorNode = prdctColorList.item(i);
                    prdctType = DOMUtils.getAttribute(prdctColorNode, "DataType");
                    colorNodeList = DOMUtils.getChild(prdctColorNode, "LevelList").getChildNodes();
                    infoMap.put("colorLevel", DOMUtils.getAttribute(prdctColorNode, "DataLevel"));
                    infoMap.put("unit", DOMUtils.getAttribute(prdctColorNode, "Unit"));
                    infoMap.put("name", DOMUtils.getAttribute(prdctColorNode, "Name"));
                    int colorsCount = colorNodeList.getLength();
                    for (int j = 0; j < colorsCount; j++) {
                        colorNode = colorNodeList.item(j);
                        if (colorNode.getNodeType() == Node.ELEMENT_NODE) {
                            colorMap = new LinkedHashMap<String, Object>();
                            value = DOMUtils.getContent(DOMUtils.getChild(colorNode, "value"));
                            rgb = DOMUtils.getContent(DOMUtils.getChild(colorNode, "rgb"));
                            min_max = value.split(",");
                            if (min_max.length == 2) {
                                colorMap.put("min", Double.parseDouble(min_max[0]));
                                colorMap.put("max", Double.parseDouble(min_max[1]));
                            }
                            colorMap.put("rgb", rgb);
                            colorMap.put("color", ColorUtil.hexToRgb(rgb.substring(1, 7)));
                            colors.add(colorMap);
                        }
                    }
                    infoMap.put("colors", colors);
                    colorConfigMap.put(prdctType, infoMap);

                }
            }
        } else {
            System.out.println("读取雷达配置文件失败");
        }
        return colorConfigMap;
    }

    //通过要素值获取对应的颜色
    public static Color getColorByValue(double value, List<HashMap<String, Object>> colors) {
        Color color = null;
        int count = colors.size();
        double min = 0.0;
        double max = 0.0;
        HashMap<String, Object> colorMap = null;
        if (value <= (Double) colors.get(0).get("min")) {
            return (Color) colors.get(0).get("color");
        }
        if (value > (Double) colors.get(count - 1).get("max")) {
            return (Color) colors.get(count - 1).get("color");
        }

        for (int i = 0; i < count; i++) {
            colorMap = colors.get(i);
            min = (Double) colorMap.get("min");
            max = (Double) colorMap.get("max");
            if (value > min && value <= max) {
                color = (Color) colorMap.get("color");
            }
        }
        return color;
    }

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        loadRadarColorConfig();
    }
}
