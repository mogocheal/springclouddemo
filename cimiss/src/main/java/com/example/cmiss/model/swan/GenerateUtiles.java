package com.example.cmiss.model.swan;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lm on 2017/4/19.
 */
public class GenerateUtiles {

    /**
     * 生成指定要求的swan图片
     *
     * @param type       swan 产品类型
     * @param isMercator 是否转换为Mercator投影
     * @param zoomFactor Mercator投影缩放系数(0-1)
     * @param clipX      裁剪边框横坐标（经度）集合(如果不裁剪则为null)
     * @param clipY      裁剪边框纵坐标（纬度）集合(如果不裁剪则为null)
     * @param bmpWidth   生成图片宽度
     * @param level      雷达数据的层次
     * @return 生成信息
     * @throws Exception
     */
    public static GenerateInfo GenerateSwanPic(SwanDataSet dataSet, SwanDataSet.ProductType type,
                                               boolean isMercator, double zoomFactor, ArrayList<Double> clipX,
                                               ArrayList<Double> clipY, int bmpWidth, int level) throws Exception {
        GenerateInfo info = new GenerateInfo();
        double xmin = 0, ymin = 0, xmax = 0, ymax = 0, xNum = 0, yNum = 0;
        double[] gridX = null, gridY = null;
        double[][][] data = null;
        double[][] data1 = null;
        if (isMercator) {
            dataSet.GetMecatorGrid(zoomFactor);
        }
        long st = System.currentTimeMillis();
        dataSet.ClipData(clipX, clipY);
        long et = System.currentTimeMillis();
        System.out.println(et - st);
        xmin = dataSet.getXMin();
        xmax = dataSet.getXMax();
        ymin = dataSet.getYMin();
        ymax = dataSet.getYMax();
        xNum = dataSet.getXNum();
        yNum = dataSet.getYNum();
        gridX = dataSet.getGridX();
        gridY = dataSet.getGridY();
        data = dataSet.getData();

        if (level < 0 || level >= data.length) {
            level = 0;
        }

        data1 = data[level];
        String dataType = dataSet.getDataType().toString();

        HashMap<String, Object> productInfo = RadarColor.loadRadarColorConfig();
        HashMap<String, Object> productColorMap = (HashMap<String, Object>) productInfo.get(dataType);
        List<HashMap<String, Object>> colors = (List<HashMap<String, Object>>) productColorMap.get("colors");

		/*BufferedImage bfImage = drawGridPic(bmpWidth, colors, gridX, gridY,
                data1, SwanDataSet.valueNa);*/
        BufferedImage bfImage = drawGridPic(bmpWidth, colors, gridX, gridY,
                data1, SwanDataSet.valueNa);

        info.setBImage(bfImage);
        info.setPrdtType(type.toString());
        info.setXMin(xmin);
        info.setYMin(ymin);
        info.setXMax(xmax);
        info.setYMax(ymax);

        return info;
    }


    /**
     * 获取radar数据
     *
     * @param filePath swan数据路径
     * @return 生成信息
     * @throws Exception
     */
    public static SwanDataSet GenerateSwanPic(String filePath, SwanDataSet.ProductType type) throws Exception {
        SwanDataSet dataSet = new SwanDataSet();
        dataSet.Open(filePath, type);
        return dataSet;
    }


    public static BufferedImage drawGridPic(int bmpWidth, List<HashMap<String, Object>> colors,
                                            float[] gridX, float[] gridY, float[][] data, float missingValue) throws Exception {
        double[] x = new double[gridX.length];
        double[] y = new double[gridY.length];
        double[][] d = new double[gridY.length][gridX.length];
        for (int i = 0; i < x.length; i++) {
            x[i] = gridX[i];
        }
        for (int i = 0; i < y.length; i++) {
            y[i] = gridY[i];
        }
        for (int i = 0; i < y.length; i++) {
            for (int j = 0; j < x.length; j++) {
                d[i][j] = data[i][j];
            }
        }
        // return drawGridPic(bmpWidth, colors, x, y, d,missingValue);
        return drawGridPic(bmpWidth, colors, x, y, d, missingValue);
    }

    private static BufferedImage drawGridPic(int bmpWidth, List<HashMap<String, Object>> colors,
                                             double[] gridX, double[] gridY, double[][] data, double missingValue
    ) throws Exception {

        double xNum = gridX.length;
        double yNum = gridY.length;
        double xmin = gridX[0];
        double xmax = gridX[gridX.length - 1];
        double ymin = gridY[0];
        double ymax = gridY[gridY.length - 1];
        int height = (int) (bmpWidth * (ymax - ymin) / (xmax - xmin));
        double xUnit = bmpWidth / xNum;
        double yUnit = height / yNum;

        BufferedImage bfImage = new BufferedImage(bmpWidth, height,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bfImage.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, bfImage.getWidth(), bfImage.getHeight());


        Color color = null;
        Rectangle2D rectangle2D = new Rectangle2D.Double();
        double x = 0, y = 0;
        for (int i = 0; i < yNum; i++) {
            y = (yNum - i - 1) * yUnit;
            for (int j = 0; j < xNum; j++) {
                x = j * xUnit;
                if (data[i][j] != missingValue) {
                    color = RadarColor.getColorByValue(data[i][j], colors);
                    //color=Color.red;
                    if (color != null) {
                        g.setColor(color);
                        rectangle2D.setRect(x, y, xUnit, yUnit);
                        g.fill(rectangle2D);
                    }
                }
                /*
				 * if (isPointInPolygon(gridX[j],gridY[i], clipX, clipY)) {
				 * rectangle2D.setRect(j * xUnit, (yNum - i - 1) * yUnit, xUnit,
				 * yUnit); g.fill(rectangle2D); }
				 */
            }
        }
        g.dispose();

        return bfImage;
    }

//	private static BufferedImage drawSGridPic(int bmpWidth,
//			List<HashMap<String, Object>> colors, float[][] lons,
//			float[][] lats, float xmin, float xmax, float ymin, float ymax,
//			float xNum, float yNum, float[][] data, float missingValue) throws Exception {
//
//		int height = (int) (bmpWidth * (ymax - ymin) / (xmax - xmin));
//		float xUnit = (xmax - xmin) / bmpWidth;
//		float yUnit = (ymax - ymin) / height;
//
//		BufferedImage bfImage = new BufferedImage(bmpWidth, height,
//				BufferedImage.TYPE_INT_ARGB);
//		Graphics2D g = bfImage.createGraphics();
//		Color color = null;
//		PolygonEx pEx = new PolygonEx();
//		float[] point = new float[2];
//		for (int i = 0; i < yNum - 1; i++) {
//			for (int j = 0; j < xNum - 1; j++) {
//				if (data[i][j] != missingValue && data[i][j] > 3) {
//					color = RadarColor.getColorByValue(data[i][j], colors);
//					if (color != null) {
//						pEx.reset();
//						CoorChange(point, new float[] { lons[i][j], lats[i][j] }, xmin, ymax, xUnit, yUnit);
//						pEx.addPoint(point[0], point[1]);
//						CoorChange(point, new float[] { lons[i][j + 1], lats[i][j + 1] }, xmin, ymax, xUnit, yUnit);
//						pEx.addPoint(point[0], point[1]);
//						CoorChange(point, new float[] { lons[i + 1][j + 1], lats[i + 1][j + 1] }, xmin, ymax, xUnit, yUnit);
//						pEx.addPoint(point[0], point[1]);
//						CoorChange(point, new float[] { lons[i + 1][j], lats[i + 1][j] }, xmin, ymax, xUnit, yUnit);
//						pEx.addPoint(point[0], point[1]);
//						g.setColor(color);
//						g.fill(pEx);
//					}
//				}
//			}
//		}
//
//		return bfImage;
//	}

    private static void CoorChange(float[] point, float[] latLon, float xMin, float yMax, float xUint, float yUint) {
        point[0] = (latLon[0] - xMin) / xUint;
        point[1] = (yMax - latLon[1]) / yUint;
    }

    private static int GetArrayIndex(double value, double[] array) {
        int index = -1;
        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) {
                index = i;
            }
        }
        return index;
    }

    public static void getBound(String boundfile,
                                ArrayList<ArrayList<Double>> boundxs,
                                ArrayList<ArrayList<Double>> boundys) throws Exception {
        boundfile = boundfile.replace("\\", "/"); // 兼容windows目录
        File file = new File(boundfile);
        FileReader fr = null;
        BufferedReader br = null;

        if (file.exists()) {
            fr = new FileReader(file);
            br = new BufferedReader(fr);
            String id, bound;
            String[] points;
            String[] point;
            ArrayList<Double> boundx = null;
            ArrayList<Double> boundy = null;
            while ((id = br.readLine()) != null
                    && ((bound = br.readLine()) != null)) {
                boundx = new ArrayList<Double>();
                boundy = new ArrayList<Double>();
                points = bound.split(";");
                for (String p : points) {
                    point = p.split(",");
                    boundx.add(Double.parseDouble(point[0]));
                    boundy.add(Double.parseDouble(point[1]));
                }
                boundxs.add(boundx);
                boundys.add(boundy);
            }
        }
    }

}
