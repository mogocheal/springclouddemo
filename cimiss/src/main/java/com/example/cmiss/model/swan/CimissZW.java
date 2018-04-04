package com.example.cmiss.model.swan;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

public class CimissZW {
    protected static double bgLonMax = 120.13783;
    protected static double bgLatMax = 44.02021;
    protected static double bgLonMin = 94.82335;
    protected static double bgLatMin = 31.27716;

    public enum ProductType {
        CR(502), R(501), VIL(503);

        private int _value;

        private ProductType(int value) {
            _value = value;
        }

        public int value() {
            return _value;
        }

        public static ProductType valueOf(int value) { // 手写的从int到enum的转换函数
            switch (value) {
                case 501:
                    return R;
                case 502:
                    return CR;
                case 503:
                    return VIL;
                default:
                    return null;
            }
        }
    }

    public ProductType getProductType() {
        return productType;
    }

    public double getLongitudeOfWest() {
        return longitudeOfWest;
    }

    public double getLatitudeOfNorth() {
        return latitudeOfNorth;
    }

    public double getLongitudeOfEast() {
        return longitudeOfEast;
    }

    public double getLatitudeOfSouth() {
        return latitudeOfSouth;
    }

    private int col, row;
    private ProductType productType;
    private double longitudeOfWest;
    private double latitudeOfNorth;
    private double longitudeOfEast;
    private double latitudeOfSouth;
    private GregorianCalendar time;

    @SuppressWarnings("unchecked")
    public BufferedImage GeneratePic(String filePath) throws Exception {
        File file = new File(filePath);
        FileInputStream fis = null;
        BufferedInputStream dis = null;
        BufferedImage bfImage = null;
        if (file.exists()) {

            fis = new FileInputStream(file);
            dis = new BufferedInputStream(fis);
            dis.skip(8);
            longitudeOfWest = 0.0001 * BEByteUtil.ReadInt32(dis);
            latitudeOfNorth = 0.0001 * BEByteUtil.ReadInt32(dis);
            longitudeOfEast = 0.0001 * BEByteUtil.ReadInt32(dis);
            latitudeOfSouth = 0.0001 * BEByteUtil.ReadInt32(dis);

            System.out.println(longitudeOfWest + "," + latitudeOfNorth + "," + longitudeOfEast + "," + latitudeOfSouth);
            int num = BEByteUtil.ReadInt16(dis);
            dis.skip(128);
            int t = BEByteUtil.ReadInt16(dis);
            productType = ProductType.valueOf(t);

            // 读取数据时间
            time = new GregorianCalendar();
            time.set(1970, 0, 1, 0, 0, 0);
            int day = BEByteUtil.ReadInt16(dis);
            int second = BEByteUtil.ReadInt32(dis) / 1000;
            time.add(Calendar.DATE, day - 1);
            time.add(Calendar.SECOND, second);
            time.add(Calendar.HOUR, 8);// 北京时间
            dis.skip(6);
            int scaleOfData = BEByteUtil.ReadInt16(dis);
            dis.skip(90 + num * 36);
            row = BEByteUtil.ReadInt16(dis);
            col = BEByteUtil.ReadInt16(dis);
            row = 1108;
            col = 1456;

            dis.skip(4);
            bfImage = new BufferedImage(col, row, BufferedImage.TYPE_INT_ARGB);
            int n;
            HashMap<String, Object> productInfo = null;
            productInfo = RadarColor.loadRadarColorConfig();
            String type = productType.toString();
            HashMap<String, Object> productColorMap = (HashMap<String, Object>) productInfo.get(type);

            List<HashMap<String, Object>> colors = (List<HashMap<String, Object>>) productColorMap.get("colors");

            Color color;
            for (int i = 0; i < row; i++) {
                BEByteUtil.ReadInt16(dis);
                for (int j = 0; j < col; j++) {
                    n = BEByteUtil.ReadInt16(dis);

                    if (n != -32767 && n != -32768 && n != 0) {

                        color = RadarColor.getColorByValue(n * 0.01, colors);

                        bfImage.setRGB(j, i, color.getRGB());
                    }
                }
            }
        }

        return bfImage;
    }

    public void GeneratePic(String filePath, String picPath) throws Exception {
        BufferedImage bfImage = GeneratePic(filePath);
        ImageIO.write(bfImage, "png", new File(picPath));
    }

    public BufferedImage GenerateTmap(String filePath) throws Exception {
        BufferedImage bfImage = GeneratePic(filePath);
        String bgImgsPath = System.getProperty("user.dir") + "/src/bgImgs/";
        String titleStr = null;
        String acNameStr = "长江流域气象中心发布";
        switch (productType) {
            case CR:
                bgImgsPath += "backgroud_cr.png";
                titleStr = "长江流域雷达拼图组合反射率产品";
                break;
            case VIL:
                bgImgsPath += "backgroud_vil.png";
                titleStr = "长江流域雷达拼图垂直积累液态水含量产品";
                break;
            default:
                break;
        }
        File bgFile = new File(bgImgsPath);
        BufferedImage bgImage = ImageIO.read(bgFile);
        int bgWidth = bgImage.getWidth();
        int bgHeight = bgImage.getHeight();

        BufferedImage img = new BufferedImage(bgWidth, bgHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();

        double xRes = (bgLonMax - bgLonMin) / bgWidth;
        double yRes = (bgLatMax - bgLatMin) / bgHeight;
        int dx1 = (int) ((longitudeOfWest - bgLonMin) / xRes);
        int dy1 = (int) ((bgLatMax - latitudeOfNorth) / yRes);
        int dx2 = (int) ((longitudeOfEast - bgLonMin) / xRes);
        int dy2 = (int) ((bgLatMax - latitudeOfSouth) / yRes);

        g.drawImage(bfImage, dx1, dy1, dx2, dy2, 0, 0, bfImage.getWidth(), bfImage.getHeight(), null);
        g.drawImage(bgImage, 0, 0, bgWidth, bgHeight, null);

        g.setColor(Color.BLACK);
        g.setFont(new Font("宋体", 1, 24));
        int width = g.getFontMetrics().stringWidth(titleStr);
        g.drawString(titleStr, (bgWidth - width) / 2, 70);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String timeStr = format.format(time.getTime()) + " BJT";
        g.setFont(new Font("宋体", 1, 20));
        width = g.getFontMetrics().stringWidth(timeStr);
        g.drawString(timeStr, (bgWidth - width) / 2, 100);

        g.setFont(new Font("宋体", 1, 22));
        g.drawString(acNameStr, 600, 500);

        return img;
    }

    public void GenerateTmap(String filePath, String picPath) throws Exception {
        BufferedImage img = GenerateTmap(filePath);
        ImageIO.write(img, "png", new File(picPath));
    }
}
