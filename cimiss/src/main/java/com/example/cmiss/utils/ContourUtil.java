package com.example.cmiss.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.cmiss.model.PolygonEx;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.util.StringUtils;
import wContour.Contour;
import wContour.Global.Border;
import wContour.Global.PointD;
import wContour.Global.PolyLine;
import wContour.Interpolate;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lm on 2017/5/26.
 */

public class ContourUtil {
    private static Font noData_font = new Font("微软雅黑", 1, 90);
    private static Color noData_color = Color.CYAN;
    private static String noData_tip = "当前时次无数据";
    private static Font mainTitle_font = new Font("微软雅黑", 0, 55);
    private static Font secondTitle_font = new Font("宋体", 1, 35);
    private static Font lqyb_secondTitle_font = new Font("微软雅黑", 0, 30);//落区二级标题
    private static Font lqyb_thridTitle_font = new Font("微软雅黑", 0, 35);//落区落款


    private static Font lqybjy_secondTitle_font = new Font("微软雅黑", 0, 30);//落区  叠加实况数据
    private static Font lqybjy_thridTitle_font = new Font("微软雅黑", 0, 35);//落区  叠加实况数据

    private static Color mainTitle_color = Color.BLACK;
    private static Color secondTitle_color = Color.BLACK;
    private static Font legend_font = new Font("微软雅黑", 0, 20);
    private static Font unit_font = new Font("微软雅黑", 1, 20);


    /**
     * 拟合自动站数据数据为Polygon
     *
     * @param xs            lon数组
     * @param ys            lat数组
     * @param values        值
     * @param contourValues 拟合的值范围
     * @param xMin          最小x
     * @param xMax          最大x
     * @param yMin          最小y
     * @param yMax          最大y
     * @param xNum          行数
     * @param yNum          列数
     * @param undefValue    无效值
     * @param clipXs        边界线x数组
     * @param clipYs        边界线y数组
     * @throws Exception
     */
    public static List<wContour.Global.Polygon> genereateContour(
            double[] xs, double[] ys, double[] values, double[] contourValues, double xMin,
            double xMax, double yMin, double yMax, int xNum, int yNum, double undefValue,
            double[] clipXs, double[] clipYs) throws Exception {

        if (xs == null || ys == null || values == null) {
            return null;
        }
        double[] gridValue = null;
        double[][] discreteData = null;
        double[][] gridData = new double[yNum][xNum];
        double[] gridX = new double[xNum];
        double[] gridY = new double[yNum];
        List<PointD> clipPList = new ArrayList<PointD>();
        List<Border> borders = new ArrayList<Border>();
        List<PolyLine> contourLines = new ArrayList<PolyLine>();
        List<wContour.Global.Polygon> contourPolygons = new ArrayList<wContour.Global.Polygon>();
        List<wContour.Global.Polygon> clippedPolygons = new ArrayList<wContour.Global.Polygon>();
        discreteData = new double[xs.length][3];
        for (int i = 0; i < xs.length; i++) {
            discreteData[i][0] = xs[i];
            discreteData[i][1] = ys[i];
            discreteData[i][2] = values[i];
        }
        // 创建栅格坐标
        Interpolate.createGridXY_Num(xMin, yMin, xMax, yMax, gridX, gridY);
        gridData = Interpolate.interpolation_IDW_Neighbor(discreteData, gridX,
                gridY, 3, undefValue);
        // 追踪等值线
        int nc = contourValues.length;
        int[][] S1 = new int[gridData.length][gridData[0].length];
        borders = Contour
                .tracingBorders(gridData, gridX, gridY, S1, undefValue);
        contourLines = Contour.tracingContourLines(gridData, gridX, gridY, nc,
                contourValues, undefValue, borders, S1);
        contourLines = Contour.smoothLines(contourLines);
        contourPolygons = Contour.tracingPolygons(gridData, contourLines,
                borders, contourValues);

        if (clipXs == null && clipYs == null) {
            clippedPolygons = contourPolygons;
        } else {
            for (int i = 0; i < clipXs.length; i++) {
                clipPList.add(new PointD(clipXs[i], clipYs[i]));
            }
            contourPolygons = Contour.clipPolygons(contourPolygons, clipPList);
        }
        return contourPolygons;
    }

    public static List<wContour.Global.Polygon> genCountourLines(double[] xs,
                                                                 double[] ys, double[] values, double[] contourValues, double xMin, double xMax,
                                                                 double yMin, double yMax, int xNum, int yNum, double undefValue, double[] clipXs, double[] clipYs, boolean type) {

        double[] gridValue = null;
        double[][] discreteData = null;
        double[][] gridData = new double[yNum][xNum];
        double[] gridX = new double[xNum];
        double[] gridY = new double[yNum];
        List<PointD> clipPList = new ArrayList<PointD>();
        List<Border> borders = new ArrayList<Border>();
        List<PolyLine> contourLines = new ArrayList<PolyLine>();
        List<wContour.Global.Polygon> contourPolygons = new ArrayList<wContour.Global.Polygon>();
        List<wContour.Global.Polygon> clippedPolygons = new ArrayList<wContour.Global.Polygon>();

        discreteData = new double[xs.length][3];
        for (int i = 0; i < xs.length; i++) {
            discreteData[i][0] = xs[i];
            discreteData[i][1] = ys[i];
            if (values[i] <= contourValues[0]) {
                //是不是针对气温进行处理,气温不会低于10;
                //discreteData[i][2]=-10;
                //修改日期20170628,修改人：王明生,解决出现:色标最小值小于-10的异常
                discreteData[i][2] = contourValues[0] - 1;
                continue;
            }
            if (values[i] >= contourValues[contourValues.length - 1]) {
                discreteData[i][2] = 1.5 * contourValues[contourValues.length - 1];
                continue;
            }
            for (int j = 0; j < contourValues.length - 1; j++) {
                if (values[i] >= contourValues[j] && values[i] < contourValues[j + 1]) {
                    discreteData[i][2] = 0.5 * (contourValues[j] + contourValues[j + 1]);
                    break;
                }
            }
        }
        // 创建栅格坐标
        Interpolate.createGridXY_Num(xMin, yMin, xMax, yMax, gridX, gridY);
        if (type)//type表示是否进行全图搜索
            //温度的插值方法  缺少按经度搜索的参数  表示 在每个格点中插值时不按经度进行搜索，而是按照全图进行搜索  速度较慢
            gridData = Interpolate.interpolation_IDW_Neighbor(discreteData, gridX, gridY, 3, undefValue);
        else
            //第五个参数  1  和第四个参数3  表示在一个经度的范围内 搜索到3个站 来搜索要素数据  来插值
            gridData = Interpolate.interpolation_IDW_Radius(discreteData, gridX, gridY, 2, 1, undefValue);

        // 追踪等值线
        int nc = contourValues.length;
        int[][] S1 = new int[gridData.length][gridData[0].length];
        borders = Contour
                .tracingBorders(gridData, gridX, gridY, S1, undefValue);
        contourLines = Contour.tracingContourLines(gridData, gridX, gridY, nc,
                contourValues, undefValue, borders, S1);
        contourLines = Contour.smoothLines(contourLines);

        contourPolygons = Contour.tracingPolygons(gridData, contourLines,
                borders, contourValues);
        if (clipXs != null && clipYs != null) {
            for (int i = 0; i < clipXs.length; i++) {
                clipPList.add(new PointD(clipXs[i], clipYs[i]));
            }
            clippedPolygons = Contour.clipPolygons(contourPolygons, clipPList);
        } else {
            clippedPolygons = contourPolygons;
        }

        return clippedPolygons;
    }

    public static List<wContour.Global.Polygon> genCountourLinesEary(double[] xs,
                                                                 double[] ys, double[] values, double[] contourValues, double xMin, double xMax,
                                                                 double yMin, double yMax, int xNum, int yNum, double undefValue, double[] clipXs, double[] clipYs, boolean type) {

        double[] gridValue = null;
        double[][] discreteData = null;
        double[][] gridData = new double[yNum][xNum];
        double[] gridX = new double[xNum];
        double[] gridY = new double[yNum];
        List<PointD> clipPList = new ArrayList<PointD>();
        List<Border> borders = new ArrayList<Border>();
        List<PolyLine> contourLines = new ArrayList<PolyLine>();
        List<wContour.Global.Polygon> contourPolygons = new ArrayList<wContour.Global.Polygon>();
        List<wContour.Global.Polygon> clippedPolygons = new ArrayList<wContour.Global.Polygon>();

        discreteData = new double[xs.length][3];
        for (int i = 0; i < xs.length; i++) {
            discreteData[i][0] = xs[i];
            discreteData[i][1] = ys[i];
            if (values[i] <= contourValues[0]) {
                //是不是针对气温进行处理,气温不会低于10;
                //discreteData[i][2]=-10;
                //修改日期20170628,修改人：王明生,解决出现:色标最小值小于-10的异常
                discreteData[i][2] = contourValues[0] - 1;
                continue;
            }
            if (values[i] >= contourValues[contourValues.length - 1]) {
                discreteData[i][2] = 1.5 * contourValues[contourValues.length - 1];
                continue;
            }
      /*      for (int j = 0; j < contourValues.length - 1; j++) {
                if (values[i] >= contourValues[j] && values[i] < contourValues[j + 1]) {
                    discreteData[i][2] = 0.5 * (contourValues[j] + contourValues[j + 1]);
                    break;
                }
            }*/
            discreteData[i][2] = values[i];
        }
        // 创建栅格坐标
        Interpolate.createGridXY_Num(xMin, yMin, xMax, yMax, gridX, gridY);
        if (type)//type表示是否进行全图搜索
            //温度的插值方法  缺少按经度搜索的参数  表示 在每个格点中插值时不按经度进行搜索，而是按照全图进行搜索  速度较慢
            gridData = Interpolate.interpolation_IDW_Neighbor(discreteData, gridX, gridY, 3, undefValue);
        else
            //第五个参数  1  和第四个参数3  表示在一个经度的范围内 搜索到3个站 来搜索要素数据  来插值
            gridData = Interpolate.interpolation_IDW_Radius(discreteData, gridX, gridY, 2, 1, undefValue);

        // 追踪等值线
        int nc = contourValues.length;
        int[][] S1 = new int[gridData.length][gridData[0].length];
        borders = Contour
                .tracingBorders(gridData, gridX, gridY, S1, undefValue);
        contourLines = Contour.tracingContourLines(gridData, gridX, gridY, nc,
                contourValues, undefValue, borders, S1);
        contourLines = Contour.smoothLines(contourLines);

        contourPolygons = Contour.tracingPolygons(gridData, contourLines,
                borders, contourValues);
        if (clipXs != null && clipYs != null) {
            for (int i = 0; i < clipXs.length; i++) {
                clipPList.add(new PointD(clipXs[i], clipYs[i]));
            }
            clippedPolygons = Contour.clipPolygons(contourPolygons, clipPList);
        } else {
            clippedPolygons = contourPolygons;
        }

        return clippedPolygons;
    }



    public static BufferedImage genImgFromMoapJson(String bgImgPath, String pgwPath,
                                                   String[] titles, String unit, String[] polygons,
                                                   double[] xs, double[] ys, double[] values, String[] stationNames,
                                                   Color[] colors, double[] contourValues,
                                                   boolean isDrawValue, boolean isDrawPolygon) throws Exception {
        BufferedImage bgImg = ImageIO.read(new File(bgImgPath));
        int width = bgImg.getWidth();
        int height = bgImg.getHeight();
        BufferedImage img = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        double[] bound = getBound(pgwPath, width, height);
        double value;
        int index;
        Color color;
        float[] pointf = new float[2];
        PolygonEx pEx = new PolygonEx();
        //1 绘制色斑图
        if (isDrawPolygon && polygons != null) {
            for (int i = 0; i < polygons.length; i++) {
                String polyGon = polygons[i];
                if (StringUtils.isEmpty(polyGon)) {
                    continue;
                }
                JSONObject jsonObject = JSONObject.parseObject(polyGon);
                String colorStr = jsonObject.get("color") + "";
                colorStr = colorStr.replace("rgba(", "").replace(")", "");
                String[] colorStrs = colorStr.split(",");
                Color tempColor = new Color(Integer.parseInt(colorStrs[0]), Integer.parseInt(colorStrs[1]), Integer.parseInt(colorStrs[2]));
                JSONArray _data = jsonObject.getJSONObject("geometry").getJSONArray("coordinates").getJSONArray(0);
                pEx.reset();
                color = tempColor;
                List<wContour.Global.PointD> pointdList = new ArrayList<>();
                for (int j = 0; j < _data.size(); j++) {
                    JSONArray jsonArray = _data.getJSONArray(j);
                    double x = Double.parseDouble(jsonArray.get(0) + "");
                    double y = Double.parseDouble(jsonArray.get(1) + "");
                    PointD pointD = new PointD(x, y);
                    pointdList.add(pointD);
                }

                for (wContour.Global.PointD pointD : pointdList) {
                    latLon2XY(pointf, pointD.X, pointD.Y, bound[0], bound[1],
                            bound[2], bound[3], width, height);
                    pEx.addPoint(pointf[0], pointf[1]);
                }
                g.setColor(color);
                g.fill(pEx);
            }

            //4 绘制图例
            //4.1绘制色卡
            //积雪倒置色标色卡
          /*  if (titles[0].contains("积雪")) {
                //   contourValues = ArrayUtils.remove(contourValues,0);
                //  colors = ArrayUtils.remove(colors,0);
                ArrayUtils.reverse(contourValues);
                ArrayUtils.reverse(colors);
            }*/
            if (titles[0].contains("能见度")) {
                //   contourValues = ArrayUtils.remove(contourValues,0);
                //  colors = ArrayUtils.remove(colors,0);
                ArrayUtils.reverse(contourValues);
                ArrayUtils.reverse(colors);
            }
            int cell_h = 36, cell_w = 64;
            float sy;
            if (colors.length < 10) {
                sy = 700 - colors.length * cell_h;
            } else if (colors.length < 15) {
                sy = 800 - colors.length * cell_h;
            } else {
                sy = 900 - colors.length * cell_h;
            }
            float sx = 2300;
            Rectangle2D rect = null;
            for (int i = 0; i < colors.length; i++) {
                rect = new Rectangle2D.Float(sx, sy + cell_h * 1.2f * i, cell_w, cell_h);
                g.setColor(colors[i]);
                g.fill(rect);
                g.setColor(Color.BLACK);
                g.draw(rect);
            }
            //4.2绘制色卡标注
            g.setColor(Color.BLACK);
            g.setFont(legend_font);
            if (titles[0].contains("1小时降水实况图")) {
                DrowSbtVal.Drow_PRE_1h(titles, contourValues, colors, cell_h, cell_w, unit, sx, sy, g);
            } else {
                for (int i = 0; i < colors.length; i++) {
                        if (i == 0) {
                             if (titles[0].contains("能见度")) {
                                g.drawString("≥ " + (int) contourValues[0] + " " + unit, sx + cell_w * 1.2f
                                        , sy + cell_h * 1.2f * i + cell_h * 0.5f + g.getFont().getSize() * 0.5f);
                            } else if (titles[0].contains("积雪")) {
                                g.drawString("无积雪 ", sx + cell_w * 1.2f
                                        , sy + cell_h * 1.2f * i + cell_h * 0.5f + g.getFont().getSize() * 0.5f);
                            } else if(titles[0].contains("降水")){
                                g.drawString("无降水 ", sx + cell_w * 1.2f
                                        , sy + cell_h * 1.2f * i + cell_h * 0.5f + g.getFont().getSize() * 0.5f);
                            }else{
                                     g.drawString("< " + (int) contourValues[0] + " " + unit, sx + cell_w * 1.2f
                                             , sy + cell_h * 1.2f * i + cell_h * 0.5f + g.getFont().getSize() * 0.5f);
                             }
                        } else if (i == colors.length - 1) {
                            if (titles[0].contains("能见度")) {
                                g.drawString((int) contourValues[i] + "- " + contourValues[i - 1] + " " + unit, sx + cell_w * 1.2f
                                        , sy + cell_h * 1.2f * i + cell_h * 0.5f + g.getFont().getSize() * 0.5f);
                            } else if (titles[0].contains("积雪")) {
                                g.drawString(">=" + contourValues[i], sx + cell_w * 1.2f
                                        , sy + cell_h * 1.2f * i + cell_h * 0.5f + g.getFont().getSize() * 0.5f);
                                g.setFont(unit_font);
                                g.drawString("单位:" + unit, sx
                                        , sy + cell_h * 1.2f * (i + 1) + cell_h * 0.5f + g.getFont().getSize() * 0.5f);
                                g.setFont(legend_font);
                            } else {
                                g.drawString("≥ " + (int) contourValues[i - 1] + " " + unit, sx + cell_w * 1.2f
                                        , sy + cell_h * 1.2f * i + cell_h * 0.5f + g.getFont().getSize() * 0.5f);
                            }
                        } else {
                            if ((i == colors.length - 2 || i == colors.length - 3) && titles[0].contains("能见度")) {
                                g.drawString(contourValues[i] + "- " + contourValues[i - 1] + " " + unit, sx + cell_w * 1.2f
                                        , sy + cell_h * 1.2f * i + cell_h * 0.5f + g.getFont().getSize() * 0.5f);

                            } else {
                                if (titles[0].contains("积雪")) {
                                    if (i == 1) {
                                        g.drawString(contourValues[i] + " - " + (int)contourValues[i + 1] + " "
                                                , sx + cell_w * 1.2f, sy + cell_h * 1.2f * i + cell_h * 0.5f + g.getFont().getSize() * 0.5f);
                                    } else
                                        g.drawString((int)contourValues[i] + " - " + (int)contourValues[i + 1] + " "
                                                , sx + cell_w * 1.2f, sy + cell_h * 1.2f * i + cell_h * 0.5f + g.getFont().getSize() * 0.5f);
                                } else if (contourValues[i - 1] == 0.1) {
                                    g.drawString(contourValues[i - 1] + " - " + (int) contourValues[i] + " " + unit
                                            , sx + cell_w * 1.2f, sy + cell_h * 1.2f * i + cell_h * 0.5f + g.getFont().getSize() * 0.5f);
                                } else {
                                    if (titles[0].contains("能见度")) {
                                        g.drawString((int) contourValues[i] + " - " + (int) contourValues[i - 1] + " " + unit
                                                , sx + cell_w * 1.2f, sy + cell_h * 1.2f * i + cell_h * 0.5f + g.getFont().getSize() * 0.5f);
                                    } else {
                                        g.drawString((int) contourValues[i - 1] + " - " + (int) contourValues[i] + " " + unit
                                                , sx + cell_w * 1.2f, sy + cell_h * 1.2f * i + cell_h * 0.5f + g.getFont().getSize() * 0.5f);
                                    }
                                }
                            }
                        }
                }
            }
            //绘制单位
            //绘制边框
            g.setColor(Color.BLACK);
            g.setStroke(new BasicStroke(3f));//设置边框宽度
            g.drawRect(5, 5, width - 10, height - 10);
        }
        //2 绘制地图
        g.drawImage(bgImg, 0, 0, null);


        //2.0绘制站点值
        if (isDrawValue) {
            g.setFont(legend_font);
            for (int i = 0; i < xs.length; i++) {//xs ---lons
                g.setColor(Color.blue);
                if (999999 == (values[i]) || 999998 == values[i] || 999990 == values[i]) {
                    continue;
                }
                //雪过滤0值
                if (titles[0].contains("积雪") && 0 == values[i]) {
                    continue;
                }
                latLon2XY(pointf, xs[i], ys[i], bound[0], bound[1],
                        bound[2], bound[3], width, height);
                if (xs[i] == 114.9)//黄冈
                    g.drawString(String.format("%.0f", values[i]), pointf[0] + 10, pointf[1] - 20);
                else if (xs[i] == 114.87)//鄂州
                    g.drawString(String.format("%.0f", values[i]), pointf[0] - 20, pointf[1] - 20);
                else
                    g.drawString(String.format("%.0f", values[i]), pointf[0], pointf[1]);
                g.setColor(Color.black);
                g.drawString(stationNames[i], pointf[0] - 10, pointf[1] + 20);
            }
        }

        g.setStroke(new BasicStroke()); //恢复默认线宽

        //3 绘制标题
        if (titles != null && titles.length > 0) {
            g.setFont(mainTitle_font);
            g.setColor(mainTitle_color);
            g.drawString(titles[0], width * 0.5f - g.getFontMetrics().stringWidth(titles[0]) * 0.5f, 90);
//			g.setFont(secondTitle_font);
            if (titles[0].endsWith("小 时 降 水 实 况 图")) {
                g.setFont(lqybjy_thridTitle_font);
            } else {
                g.setFont(lqybjy_secondTitle_font);
            }
//			g.setFont(lqybjy_secondTitle_font);
            g.setColor(secondTitle_color);
            // for (int i = 1; i < titles.length - 1; i++) {
            g.drawString(titles[1], width * 0.5f - g.getFontMetrics().stringWidth(titles[1]) * 0.5f, 90 + 60 * 1);
            //  }
        }
        //绘制落款
        if (titles[0].contains("积雪")) {
            g.setFont(lqybjy_thridTitle_font);
            g.drawString("湖北省气象局", width - 280, height - 72);
        }
      /*  if (titles[0].endsWith("小 时 降 水 实 况 图") || titles[0].endsWith("")) {
            g.setFont(lqyb_secondTitle_font);//落款字体
            g.drawString(titles[titles.length - 1], width * 0.8f - 40, height * 0.95f);
        } else {
            g.setFont(lqybjy_thridTitle_font);
            g.drawString(titles[titles.length - 1], width * 0.8f, height * 0.95f);
        }*/
        return img;
    }


    private static double[] getBound(String pgwPath, int width, int height)
            throws Exception {
        double[] bound = new double[4];
        String[] twfInfo = new String[6];
        FileReader fReader = null;
        BufferedReader brReader = null;
        try {
            fReader = new FileReader(new File(pgwPath));
            brReader = new BufferedReader(fReader);
            for (int i = 0; i < 6; i++) {
                twfInfo[i] = brReader.readLine();
            }
            bound[0] = Double.parseDouble(twfInfo[4]);
            bound[1] = Double.parseDouble(twfInfo[4]) + width
                    * Double.parseDouble(twfInfo[0]);
            bound[2] = Double.parseDouble(twfInfo[5]) + height
                    * Double.parseDouble(twfInfo[3]);
            bound[3] = Double.parseDouble(twfInfo[5]);

            return bound;
        } catch (Exception e) {
            throw e;
        } finally {
            if (brReader != null) {
                brReader.close();
            }
            if (fReader != null) {
                fReader.close();
            }
        }
    }

    private static int indexOfArray(double[] array, double value) {
        int n = -1;
        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) {
                n = i;
                break;
            }
        }
        return n;
    }

    private static void latLon2XY(float[] xy, double lon, double lat,
                                  double xMin, double xMax, double yMin, double yMax, int width,
                                  int height) {
        xy[0] = (float) ((lon - xMin) / (xMax - xMin) * width);
        xy[1] = (float) ((yMax - lat) / (yMax - yMin) * height);
    }


}
