package com.example.cmiss.model.swan;

import java.awt.*;
import java.util.Random;

public class ColorUtil {

    /**
     * 功能描述:生成十六进制随机颜色
     * 创建时间:2014-4-18 上午10:09:24<br>
     *
     * @return String
     */
    public static String getRamdonColor() {
        String hexColor = "000000";
        String red = "";//红色
        String green = "";//绿色
        String blue = "";//蓝色
        //生成随机对象
        Random random = new Random();
        //随机生成红色颜色代码
        red = Integer.toHexString(random.nextInt(256)).toUpperCase();
        //随机生成绿色颜色代码
        green = Integer.toHexString(random.nextInt(256)).toUpperCase();
        //随机生成蓝色颜色代码
        blue = Integer.toHexString(random.nextInt(256)).toUpperCase();
        red = red.length() == 1 ? "0" + red : red;
        green = green.length() == 1 ? "0" + green : green;
        blue = blue.length() == 1 ? "0" + blue : blue;
        //生成十六进制颜色
        hexColor = "#" + red + green + blue;
        return hexColor;
    }

    public static String rgbToHex(String rgb) {
        String hexColor = "#000000";
        int r = 0;
        int g = 0;
        int b = 0;
        String red = "";//红色
        String green = "";//绿色
        String blue = "";//蓝色
        if (rgb != null && rgb.length() > 0) {
            String[] tmpArr = rgb.split(",");
            if (tmpArr.length == 3) {
                r = Integer.parseInt(tmpArr[0]);
                g = Integer.parseInt(tmpArr[1]);
                b = Integer.parseInt(tmpArr[2]);
                red = Integer.toHexString(r).toUpperCase();
                green = Integer.toHexString(g).toUpperCase();
                blue = Integer.toHexString(b).toUpperCase();
                hexColor = "#" + red + green + blue;
            } else {
                System.out.println("输入的RGB值[" + rgb + "]不正确，请输入正确的RGB?,例如255,244,244");
            }
        } else {
            throw new NullPointerException("[rgbToHex] - 参数rgb值为");
        }
        return hexColor;
    }

    /**
     * 功能描述16进制颜色值转成rgb<br>
     *
     * @param hex
     * @return Color
     */
    public static Color hexToRgb(String hex) {
        Color color = Color.black;
        if (hex.length() == 6) {
            String red = hex.substring(0, 2);
            String green = hex.substring(2, 4);
            String blue = hex.substring(4, 6);
            int r = Integer.parseInt(red, 16);
            ;
            int g = Integer.parseInt(green, 16);
            ;
            int b = Integer.parseInt(blue, 16);
            ;
            color = new Color(r, g, b);
        }
        return color;
    }

}
