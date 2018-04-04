package com.example.cmiss.model.swan;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class DotUtil {

    /**
     * @param 多边形点序列[x1,y1;x2,y2;x3,y3] regPntStr
     * @param 要判断的点X坐标                  XDot
     * @param 要判断的点Y坐标                  YDot
     * @return boolean 点在多边形里面返回为true,不在为false
     * @method �?查所查点坐标是否在多边形�?
     */
    public static boolean checkDotInPolygon(String regPntStr, double XDot,
                                            double YDot) {
        boolean t_bCheckFlg = false;
        String[] strArr = regPntStr.split(";");
        int pntNum = strArr.length;
        double[] PQXarray = new double[pntNum];
        double[] PQYarray = new double[pntNum];
        for (int i = 0; i < pntNum; i++) {
            PQXarray[i] = Double.parseDouble(strArr[i].split(",")[0]);
            PQYarray[i] = Double.parseDouble(strArr[i].split(",")[1]);
        }
        int t_cout = 0;
        if ((PQXarray[PQXarray.length - 1] == XDot)
                && (PQYarray[PQYarray.length - 1] == YDot))
            t_bCheckFlg = true;
        else {
            if (((PQYarray[PQYarray.length - 1] > YDot) && (PQYarray[0] < YDot))
                    || ((PQYarray[PQYarray.length - 1] < YDot) && (PQYarray[0] > YDot))) {
                double t_X = (PQXarray[0] * YDot
                        + PQXarray[PQXarray.length - 1] * PQYarray[0]
                        - PQXarray[PQXarray.length - 1] * YDot - PQXarray[0]
                        * PQYarray[PQYarray.length - 1])
                        / (PQYarray[0] - PQYarray[PQYarray.length - 1]);
                if (t_X == XDot)
                    t_bCheckFlg = true;
                else if (t_X > XDot)
                    t_cout++;
            }
        }
        if (!t_bCheckFlg) {
            for (int i = 0; i < (PQXarray.length - 1); i++) {
                if ((PQXarray[i] == XDot) && (PQYarray[i] == YDot)) {
                    t_bCheckFlg = true;
                    break;
                } else {
                    if (((PQYarray[i] > YDot) && (PQYarray[i + 1] < YDot))
                            || ((PQYarray[i] < YDot) && (PQYarray[i + 1] > YDot))) {
                        double t_X = (PQXarray[i] * YDot + PQXarray[i + 1]
                                * PQYarray[i] - PQXarray[i + 1] * YDot - PQXarray[i]
                                * PQYarray[i + 1])
                                / (PQYarray[i] - PQYarray[i + 1]);
                        if (t_X == XDot) {
                            t_bCheckFlg = true;
                            break;
                        } else if (t_X > XDot)
                            t_cout++;
                    }
                }
            }
        }
        if ((!t_bCheckFlg) && (t_cout % 2 == 1))
            t_bCheckFlg = true;
        return t_bCheckFlg;
    }

    public static boolean isPointInPolygon(double px, double py, ArrayList<Double> polygonXA, ArrayList<Double> polygonYA) {
        //log.info("method --> isPointInPolygon");
        boolean isInside = false;
        double ESP = 1e-9;
        int count = 0;
        double linePoint1x;
        double linePoint1y;
        double linePoint2x = 180;//2.0037508342789E7;//180;//2.0037508342789E7;
        double linePoint2y;

        linePoint1x = px;
        linePoint1y = py;
        linePoint2y = py;

        for (int i = 0; i < polygonXA.size() - 1; i++) {
            double cx1 = polygonXA.get(i);
            double cy1 = polygonYA.get(i);
            double cx2 = polygonXA.get(i + 1);
            double cy2 = polygonYA.get(i + 1);
            if (isPointOnLine(px, py, cx1, cy1, cx2, cy2)) {
                return true;
            }
            if (Math.abs(cy2 - cy1) <= ESP) {
                continue;
            }

            if (isPointOnLine(cx1, cy1, linePoint1x, linePoint1y, linePoint2x, linePoint2y)) {
                if (cy1 > cy2) count++;
            } else if (isPointOnLine(cx2, cy2, linePoint1x, linePoint1y, linePoint2x, linePoint2y)) {
                if (cy2 > cy1) count++;
            } else if (isIntersect(cx1, cy1, cx2, cy2, linePoint1x, linePoint1y, linePoint2x, linePoint2y)) {
                count++;
            }
        }
        if (count % 2 == 1) {
            isInside = true;
        }

        return isInside;
    }


    public static double Multiply(double px0, double py0, double px1, double py1, double px2, double py2) {
        return ((px1 - px0) * (py2 - py0) - (px2 - px0) * (py1 - py0));
    }


    public static boolean isPointOnLine(double px0, double py0, double px1, double py1, double px2, double py2) {
        boolean flag = false;
        double ESP = 1e-9;
        if ((Math.abs(Multiply(px0, py0, px1, py1, px2, py2)) < ESP) && ((px0 - px1) * (px0 - px2) <= 0) && ((py0 - py1) * (py0 - py2) <= 0)) {
            flag = true;
        }
        return flag;
    }


    public static boolean isIntersect(double px1, double py1, double px2, double py2, double px3, double py3, double px4, double py4) {
        boolean flag = false;
        double d = (px2 - px1) * (py4 - py3) - (py2 - py1) * (px4 - px3);
        if (d != 0) {
            double r = ((py1 - py3) * (px4 - px3) - (px1 - px3) * (py4 - py3)) / d;
            double s = ((py1 - py3) * (px2 - px1) - (px1 - px3) * (py2 - py1)) / d;
            if ((r >= 0) && (r <= 1) && (s >= 0) && (s <= 1)) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 判断�?个点是否在圆�?
     *
     * @param centerX 圆心坐标X
     * @param centerY 圆心坐标Y
     * @param radius  圆半�?
     * @param px      判断点的X坐标
     * @param py      判断点的Y坐标
     * @return 在里面返回true，不在里面返回false
     * @author 雷志�?
     */
    public static boolean inCircle(double centerX, double centerY, double radius, double px, double py) {
        double a = Math.sqrt(Math.pow(px - centerX, 2) + Math.pow(py - centerY, 2));
        if (a > radius) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean inCircle(String circlePntStr, double radius, double px, double py) {
        String[] pntArr = circlePntStr.split(";");
        int pntNum = pntArr.length;
        boolean flag = false;
        for (int i = 0; i < pntNum; i++) {
            double cx = Double.parseDouble(pntArr[i].split(",")[0]);
            double cy = Double.parseDouble(pntArr[i].split(",")[1]);
            flag = inCircle(cx, cy, radius, px, py);
            if (flag) {
                break;
            }
        }
        return flag;
    }

    /**
     * 标准度分秒经纬度字符串转数字经纬�?
     *
     * @param coordString 度分秒经纬度字符�?
     * @return 数字经纬�?
     * @author LZQ
     */
    public static double PointCoordChange(String coordString) {
        double d = 0.0;
        //log.info("coordString --> "+coordString);
        if (coordString.length() > 0) {
            String str = coordString.substring(0, coordString.length() - 1);
            //log.info("DotStr --> "+str);
            String type = coordString.substring(coordString.length() - 1, coordString.length());
            //log.info("DotType --> "+type);
            String degree = str.substring(0, str.length() - 4);
            //log.info("DotDegree --> "+degree);
            String minute = str.substring(str.length() - 4, str.length() - 2);
            //log.info("DotMinute --> "+minute);
            String second = str.substring(str.length() - 2, str.length());
            //log.info("DotSecond --> "+second);
            double d1 = Double.parseDouble(degree);
            //log.info("DotDegree --> "+d1);
            double d2 = (Integer.parseInt(minute) * 60 + Integer.parseInt(second)) / 3600.0000;
            //log.info("DotPoint --> "+d2);
            d = d1 + d2;
            //log.info("d --> "+d);
            DecimalFormat df = new DecimalFormat("#0.000000");
            String s = df.format(d);
            //log.info("s --> "+s);
            d = Double.parseDouble(s);
            if (type.equals("E") || type.equals("N")) {

            } else {
                d = -d;
            }
        }
        //log.info("d --> "+d);
        return d;
    }

    public static double changeStrXyToDoubleXy(String coordString) {
        double d = 0.0;
        //log.info("coordString --> "+coordString);
        if (coordString.length() > 0) {
            String str = coordString.substring(0, coordString.length());
            //log.info("DotStr --> "+str);
            String degree = str.substring(0, str.length() - 4);
            //log.info("DotDegree --> "+degree);
            String minute = str.substring(str.length() - 4, str.length() - 2);
            //log.info("DotMinute --> "+minute);
            String second = str.substring(str.length() - 2, str.length());
            //log.info("DotSecond --> "+second);
            double d1 = Double.parseDouble(degree);
            //log.info("DotDegree --> "+d1);
            double d2 = (Integer.parseInt(minute) * 60 + Integer.parseInt(second)) / 3600.0000;
            //log.info("DotPoint --> "+d2);
            d = d1 + d2;
            //log.info("d --> "+d);
            DecimalFormat df = new DecimalFormat("#0.000");
            String s = df.format(d);
            //log.info("s --> "+s);
            d = Double.parseDouble(s);
        }
        return d;
    }

    /**
     * 转换科学计数为一般的，并取小数点之前的数�?
     *
     * @param num 数字�?
     * @return
     */
    static String parseSciNum(String num) {
        String number = num;
        if (number != null && number != "") {
            if (number.contains("E")) {
                int index = Integer.valueOf(number.substring(number.length() - 1));
                number = number.substring(number.indexOf(".") + 1, index + 2);
                number = num.substring(0, 1) + number;
            } else if (number.contains(".")) {
                number = number.substring(0, number.indexOf("."));
            }
        }
        return number;
    }

    static double parseSciNum(double num) {
        double d = 0;
        String number = String.valueOf(num);
        String numStr = String.valueOf(num);
        if (number != null && number != "") {
            if (number.contains("E")) {
                int index = Integer.valueOf(number.substring(number.length() - 1));
                number = number.substring(number.indexOf(".") + 1, index + 2);
                number = numStr.substring(0, 1) + number;
            } else if (number.contains(".")) {
                number = number.substring(0, number.indexOf("."));
            }
        }
        d = Double.parseDouble(number);
        return d;
    }

    /**
     * WEB墨卡托转经纬�?
     *
     * @param px 墨卡托投影X坐标
     * @param py 墨卡托投影Y坐标
     * @return
     */
    public static double[] Mercator2LonLat(double px, double py) {
        //DecimalFormat df = new DecimalFormat("#0.000");
        double[] xy = new double[2];
        double x = px * 0.0000089831528411953233266080947888;///20037508.342789*180;
        double y = py * 0.0000089831528411953233266080947888;///20037508.342789*180;
        double M_PI = Math.PI;
        //y = 180/M_PI*(2*Math.atan(Math.exp(y*M_PI/180))-0.5*M_PI);
        y = 57.295779513082320876846364344191 * (2 * Math.atan(Math.exp(y * 0.017453292519943295)) - 0.5 * M_PI);
        xy[0] = x;//Double.parseDouble(df.format(x));
        xy[1] = y;//Double.parseDouble(df.format(y));
        return xy;
    }

    /**
     * 经纬度转WEB墨卡�?
     *
     * @param px 经度
     * @param py 纬度
     * @return WEB墨卡托XY坐标
     */
    public static double[] LonLat2Mercator(double px, double py) {
        DecimalFormat df = new DecimalFormat("#0.000000");
        double[] xy = new double[2];
        double x = px * 20037508.342789 / 180;
        double y = Math.log(Math.tan((90 + py) * Math.PI / 360)) / (Math.PI / 180);
        y = y * 20037508.342789 / 180;
        xy[0] = Double.parseDouble(df.format(x));
        xy[1] = Double.parseDouble(df.format(y));
        return xy;
    }

    public static double PointCoorChange(String coord) {
        double d = 0.0;
        if (coord.length() >= 7) {
            String str = coord.substring(0, coord.length() - 1);
            String degree = str.substring(0, str.length() - 4);
            String minite = str.substring(str.length() - 4, str.length() - 2);
            String second = str.substring(str.length() - 2, str.length());
            double d1 = Double.parseDouble(degree);
            double d2 = (Integer.parseInt(minite) * 60 + Integer.parseInt(second)) / 3600.0;
            d = d1 + d2;
            DecimalFormat df = new DecimalFormat("#0.0000");
            d = Double.parseDouble(df.format(d));
        }
        return d;
    }

//	public static void main(String[] args) {
//		String box = "108.363412,29.032582,116.130947,33.273221";//湖北省区域范�?
//		box = "115.413810,39.441978,117.499200,41.059284";//北京市区域范�?
//		String[] strArr = box.split(",");
//		double xmin = Double.parseDouble(strArr[0]);
//		double ymin = Double.parseDouble(strArr[1]);
//		double xmax = Double.parseDouble(strArr[2]);
//		double ymax = Double.parseDouble(strArr[3]);
//		DecimalFormat df = new DecimalFormat("#0.000000");
//		double[] minXY = LonLat2Mercator(xmin, ymin);
//		double[] maxXY = LonLat2Mercator(xmax, ymax);
//		double dx = maxXY[0] - minXY[0];
//		double dy = maxXY[1] - minXY[1];
//		int width = 2000;
//		double height = dy*(width/dx);
//	}
}
