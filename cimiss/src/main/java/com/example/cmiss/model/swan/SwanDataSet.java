package com.example.cmiss.model.swan;

import com.example.cmiss.utils.LEByteUtil;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;

public class SwanDataSet implements Serializable {
    public enum ProductType {
        CR, ET, VIL, QPE, R3D, RFST, TREC
    }

    ;

    public enum DataType {
        R, V, HGT, RAIN, VIL
    }

    ;

    public static double valueNa = -9999.0;

    private ProductType prdType;
    private DataType dataType;
    private SwanHeader header;
    private double[] gridX;
    private double[] gridY;
    private double xMin, xMax, yMin, yMax;
    private int xNum, yNum;
    private double[][][] data;

    public SwanDataSet() {

    }

    private static DataType InitType(ProductType prdType) {
        DataType dataType = null;
        switch (prdType) {
            case CR:
            case R3D:
                dataType = DataType.R;
                break;
            case ET:
                dataType = DataType.HGT;
                break;
            case RFST:
                dataType = DataType.R;
                break;
            case VIL:
                dataType = DataType.VIL;
                break;
            case QPE:
                dataType = DataType.RAIN;
                break;
            case TREC:
                dataType = DataType.V;
                break;
            default:
                break;
        }
        return dataType;
    }

    public boolean Open(byte[] bytesArray, ProductType prdType) throws IOException {
//		FileOutputStream fos = new FileOutputStream("F:/Z_OTHE_RADAMCR_20170623020000.bin.bz2",true);
//        fos.write(bytesArray);
//        fos.close();
//        System.out.println(bytesArray.toString());//字节数组打印
//	    String filePath = "F:/radar/Z_OTHE_RADAMCR_20170623020000.bin.bz2";
//		File file = new File(filePath);
//		FileInputStream fis = null;
//		BufferedInputStream dis = null;
//		InputStream is = null;
//		if (file.exists()) {
//			fis = new FileInputStream(file);
//			dis = new BufferedInputStream(fis);
//			//兼容压缩与非压缩格式
//			String[] format = filePath.split("\\.");
//			if (format[format.length - 1].equals("bz2")) {
//				is = new BZip2CompressorInputStream(dis);
//			} else {
//				is = dis;
//			}
//		}
        if (bytesArray == null || bytesArray.length < 278)//数据头
        {
            System.err.println("数据字节数不合要求");
            return false;
        }
        if (bytesArray[0] != 109 || bytesArray[1] != 100 || bytesArray[2] != 102 || bytesArray[3] != 115)//不以"mdfs"开头
        {
            System.err.println("尝试读取不以mdfs开头的字节数据");
            //return false;
        }

        //ByteBuffer byteBuffer = ByteBuffer.wrap(bytesArray, 4, bytesArray.length - 4);//Wraps a byte array into a buffer
        //byteBuffer.order(ByteOrder.LITTLE_ENDIAN);//设置为小端字节序
        //BufferedOutputStream bw = null;
//		String path = "F:/test.bin.bz2";
//		try {
//			// 创建文件对象
//			File f = new File(path);
//			// 创建文件路径
//			if (!f.getParentFile().exists())
//				f.getParentFile().mkdirs();
//			// 写入文件
//			bw = new BufferedOutputStream(new FileOutputStream(path));
//			bw.write(byteBuffer.array());
//		} catch (Exception e) {
//			System.out.println("保存文件错误,path=" + path+" "+ e.getMessage());
//		} finally {
//			try {
//				if (bw != null)
//					bw.close();
//			} catch (Exception e) {
//				System.out.println("finally BufferedOutputStream shutdown close"+" "+ e.getMessage());
//			}
//		}

        //System.out.println(bytesArray.toString());
        this.prdType = prdType;
        dataType = InitType(prdType);

        InputStream sis = null;
        ByteArrayInputStream sbis = null;
        BufferedInputStream sdis = null;

        try {
            //解压byte[]数据
            sbis = new ByteArrayInputStream(bytesArray);
            sdis = new BufferedInputStream(sbis);
            sis = new BZip2CompressorInputStream(sdis);

            if (sis == null) {
                return false;
            }

            header = ReadHeader(sis);

            xMin = header.getStartLon();
            yMax = header.getStartLat();
            xMax = xMin + (header.getCenterLon() - xMin) * 2;
            yMin = yMax + (header.getCenterLat() - yMax) * 2;
            xNum = header.getXNumGrids();
            yNum = header.getYNumGrids();
            double xUnit = (xMax - xMin) / xNum;
            double yUnit = (yMax - yMin) / yNum;
            gridX = new double[xNum];
            gridY = new double[yNum];
            for (int i = 0; i < xNum; i++) {
                gridX[i] = xMin + i * xUnit;
            }
            for (int i = 0; i < yNum; i++) {
                gridY[i] = yMin + i * yUnit;
            }

            switch (prdType) {
                case CR:
                case R3D:
                    data = ReadByteData(sis, header.getXNumGrids(), header
                            .getYNumGrids(), header.getZNumGrids(), dataType);
                    break;
                case ET:
                case VIL:
                case RFST:
                case TREC:
                case QPE:
                    data = ReadUInt16Data(sis, header.getXNumGrids(), header
                            .getYNumGrids(), header.getZNumGrids(), dataType);
                    break;
                default:
                    break;
            }
        } catch (Exception ex) {
            System.out.println("读取失败:" + ex.getMessage());
        } finally {
            if (sis != null) {
                sis.close();
            }
        }
        return true;
    }

    public void Open(String filePath, ProductType prdType) throws Exception {
        this.prdType = prdType;
        dataType = InitType(prdType);


        filePath = filePath.replace("\\", "/"); // 兼容windows目录
        File file = new File(filePath);
        FileInputStream fis = null;
        BufferedInputStream dis = null;
        InputStream is = null;
        if (file.exists()) {

            fis = new FileInputStream(file);
            dis = new BufferedInputStream(fis);
            //兼容压缩与非压缩格式
            String[] format = filePath.split("\\.");
            if (format[format.length - 1].equals("bz2")) {
                is = new BZip2CompressorInputStream(dis);
            } else {
                is = dis;
            }

            header = ReadHeader(is);

            xMin = header.getStartLon();
            yMax = header.getStartLat();
            xMax = xMin + (header.getCenterLon() - xMin) * 2;
            yMin = yMax + (header.getCenterLat() - yMax) * 2;
            xNum = header.getXNumGrids();
            yNum = header.getYNumGrids();
            double xUnit = (xMax - xMin) / xNum;
            double yUnit = (yMax - yMin) / yNum;
            gridX = new double[xNum];
            gridY = new double[yNum];
            for (int i = 0; i < xNum; i++) {
                gridX[i] = xMin + i * xUnit;
            }
            for (int i = 0; i < yNum; i++) {
                gridY[i] = yMin + i * yUnit;
            }

            switch (prdType) {
                case CR:
                case R3D:
                    data = ReadByteData(is, header.getXNumGrids(), header
                            .getYNumGrids(), header.getZNumGrids(), dataType);
                    break;
                case ET:
                case VIL:
                case RFST:
                case TREC:
                case QPE:
                    data = ReadUInt16Data(is, header.getXNumGrids(), header
                            .getYNumGrids(), header.getZNumGrids(), dataType);
                    break;
                default:
                    break;
            }

        }
    }

    public void GetMecatorGrid(double zoomFactor) {
        int mxNum = (int) (xNum * zoomFactor);
        int myNum = (int) (yNum * zoomFactor);

        double[][][] mdata = new double[data.length][myNum][mxNum];

        double[] mgridX = new double[mxNum];
        double[] mgridY = new double[myNum];
        double[] max = DotUtil.LonLat2Mercator(xMax, yMax);
        double[] min = DotUtil.LonLat2Mercator(xMin, yMin);
        double mxMax = max[0];
        double myMax = max[1];
        double mxMin = min[0];
        double myMin = min[1];

        double xUnit = (mxMax - mxMin) / mxNum;
        double yUnit = (myMax - myMin) / myNum;

        for (int i = 0; i < mxNum; i++) {
            mgridX[i] = mxMin + i * xUnit;
        }
        for (int i = 0; i < myNum; i++) {
            mgridY[i] = myMin + i * yUnit;
        }
        double[] p = new double[2];

        for (int i = 0; i < myNum; i++) {
            for (int j = 0; j < mxNum; j++) {
                p = DotUtil.Mercator2LonLat(mgridX[j], mgridY[i]);
                for (int t = 0; t < data.length; t++) {
                    mdata[t][i][j] = GetLatLonValue(p[0], p[1], t);
                }
            }
        }
        xNum = mxNum;
        yNum = myNum;
        xMin = mxMin;
        xMax = mxMax;
        yMin = myMin;
        yMax = myMax;
        gridX = mgridX;
        gridY = mgridY;
        data = mdata;

    }

    public double GetLatLonValue(double x, double y, int h) {
        double value = 0;
        int[] xindex = new int[2], yindex = new int[2];

        if (BinarySearch(gridX, x, xindex) && BinarySearch(gridY, y, yindex)) {
            value = DoubleLinearInter(xindex[0], xindex[1], yindex[0],
                    yindex[1], x, y, gridX, gridY, data[h], valueNa);
        } else {
            value = valueNa;
        }

        return value;
    }

    public void ClipData(ArrayList<Double> clipX, ArrayList<Double> clipY) {
        if (clipX == null || clipY == null || clipX.size() == 0
                || clipY.size() == 0) {
            return;
        }

        int minXI = 0, minYI = 0, maxXI = 0, maxYI = 0;
        double clippedxMin = clipX.get(0), clippedxMax = clipX.get(0), clippedyMin = clipY
                .get(0), clippedyMax = clipY.get(0);
        for (Double d : clipX) {
            if (d > clippedxMax) {
                clippedxMax = d;
            }
            if (d < clippedxMin) {
                clippedxMin = d;
            }
        }
        for (Double d : clipY) {
            if (d > clippedyMax) {
                clippedyMax = d;
            }
            if (d < clippedyMin) {
                clippedyMin = d;
            }
        }

        int[] index = new int[2];
        BinarySearch(gridX, clippedxMin, index);
        minXI = index[0];
        BinarySearch(gridX, clippedxMax, index);
        maxXI = index[1];
        BinarySearch(gridY, clippedyMin, index);
        minYI = index[0];
        BinarySearch(gridY, clippedyMax, index);
        maxYI = index[1];
        clippedxMin = gridX[minXI];
        clippedxMax = gridX[maxXI];
        clippedyMin = gridY[minYI];
        clippedyMax = gridY[maxYI];
        int clippedxNum = maxXI - minXI + 1;
        int clippedyNum = maxYI - minYI + 1;
        double[] clippedX = new double[clippedxNum];
        double[] clippedY = new double[clippedyNum];
        double[][][] clippedData = new double[data.length][clippedyNum][clippedxNum];

        boolean isPolygened = false;
        boolean isIn = false;
        for (int i = minYI; i <= maxYI; i++) {
            for (int j = minXI; j <= maxXI; j++) {
                clippedX[j - minXI] = gridX[j];
                clippedY[i - minYI] = gridY[i];

                for (int t = 0; t < clippedData.length; t++) {
                    /*if (DotUtil.isPointInPolygon(gridX[j], gridY[i],clipX, clipY)) {
                        clippedData[t][i - minYI][j - minXI] = 30;
					}*/
                    if (data[t][i][j] != valueNa) {
                        if (!isPolygened) {
                            isIn = DotUtil.isPointInPolygon(gridX[j], gridY[i],
                                    clipX, clipY);
                        }
                        if (isIn) {
                            clippedData[t][i - minYI][j - minXI] = data[t][i][j];
                        } else {
                            clippedData[t][i - minYI][j - minXI] = valueNa;
                        }
                    } else {
                        clippedData[t][i - minYI][j - minXI] = valueNa;
                    }
                }
                isPolygened = false;
            }
        }

        xNum = clippedxNum;
        yNum = clippedyNum;
        xMin = clippedxMin;
        xMax = clippedxMax;
        yMin = clippedyMin;
        yMax = clippedyMax;
        gridX = clippedX;
        gridY = clippedY;
        data = clippedData;
    }

    private double DoubleLinearInter(int xn1, int xn2, int yn1, int yn2,
                                     double x0, double y0, double[] x, double[] y, double[][] data,
                                     double valueNa) {
        double value = valueNa;

        try {
            double x1 = x[xn1], x2 = x[xn2], y1 = y[yn1], y2 = y[yn2];
            double y1x1 = data[yn1][xn1];
            double y1x2 = data[yn1][xn2];
            double y2x1 = data[yn2][xn1];
            double y2x2 = data[yn2][xn2];
            if (y1x1 == valueNa && y1x2 == valueNa && y2x1 == valueNa
                    && y2x2 == valueNa) {
                return valueNa;
            }

            if (y1x1 == valueNa) {
                y1x1 = 0;
            }
            if (y1x2 == valueNa) {
                y1x2 = 0;
            }
            if (y2x1 == valueNa) {
                y2x1 = 0;
            }
            if (y2x2 == valueNa) {
                y2x2 = 0;
            }

            double k1 = (x2 - x0) / (x2 - x1);
            double k2 = (y2 - y0) / (y2 - y1);
            double r1 = k1 * y1x1 + (1 - k1) * y1x2;
            double r2 = k1 * y2x1 + (1 - k1) * y2x2;
            value = k2 * r1 + (1 - k2) * r2;
        } catch (Exception e) {
            value = valueNa;
        }
        return value;
    }

    private boolean BinarySearch(double[] array, double value, int[] index) {
        index[0] = 0;
        index[1] = array.length - 1;
        int mid = 0;
        if (value < array[index[0]] || value > array[index[1]]) {
            return false;
        }
        if (value == array[index[0]]) {

            index[1] = index[0] + 1;
            return true;
        }
        if (value == array[index[1]]) {
            index[0] = index[1] - 1;
            return true;
        }

        while (index[0] <= index[1]) {
            mid = (index[1] + index[0]) / 2;
            if (array[mid] == value) {
                index[0] = mid;
                index[1] = mid + 1;
                return true;
            } else if (index[1] - index[0] == 1) {
                return true;
            } else if (array[mid] < value) {
                index[0] = mid;
            } else if (array[mid] > value) {
                index[1] = mid;
            }
        }
        return false;
    }

    private double[][][] ReadUInt16Data(InputStream dis, int xNumGrids,
                                        int yNumGrids, int zNumGrids, DataType type) throws IOException {
        double[][][] data = new double[zNumGrids][][];

        for (int i = 0; i < zNumGrids; i++) {
            double[][] levelData = new double[yNumGrids][];
            for (int j = 0; j < yNumGrids; j++) {
                double[] rowData = new double[xNumGrids];
                for (int k = 0; k < xNumGrids; k++) {
                    rowData[k] = CodeToValue(LEByteUtil.ReadInt16(dis), type);
                }
                levelData[yNumGrids - j - 1] = rowData;
            }
            data[i] = levelData;
        }

        return data;
    }

    private double[][][] ReadByteData(InputStream dis, int xNumGrids,
                                      int yNumGrids, int zNumGrids, DataType type) throws IOException {
        double[][][] data = new double[zNumGrids][][];

        for (int i = 0; i < zNumGrids; i++) {
            double[][] levelData = new double[yNumGrids][];
            for (int j = 0; j < yNumGrids; j++) {
                double[] rowData = new double[xNumGrids];
                for (int k = 0; k < xNumGrids; k++) {
                    rowData[k] = CodeToValue(dis.read(), type);
                }
                levelData[yNumGrids - j - 1] = rowData;
            }
            data[i] = levelData;
        }

        return data;
    }

    private double CodeToValue(int code, DataType type) {
        switch (type) {
            case R://反射率
                if (code == 0) {
                    return valueNa;
                } else {
                    return 0.5f * ((float) code - 66.0f);
                }
            case HGT:
            case VIL:
                if (code == 0) {
                    return valueNa;
                } else {
                    return 0.1f * code;
                }
            case RAIN://降水
                if (code == 0) {
                    return valueNa;
                } else if (code > 203) {
                    return 20.3f;
                } else {
                    return 0.1f * code;
                }
            case V://速度
                if (code == 0) {
                    return code;
                } else {
                    return 0.1f * code;
                }
            default:
                return code;
        }
    }

    private SwanHeader ReadHeader(InputStream dis) throws Exception {
        SwanHeader header = new SwanHeader();

        header.setZonName(LEByteUtil.ReadBytesString(dis, 12, "GBK"));
        header.setDataName(LEByteUtil.ReadBytesString(dis, 38, "GBK"));
        header.setFlag(LEByteUtil.ReadBytesString(dis, 8, "GBK"));
        header.setVersion(LEByteUtil.ReadBytesString(dis, 8, "GBK"));
        header.setTime(ReadTime(dis));
        header.setInterval(LEByteUtil.ReadUInt16(dis));
        header.setXNumGrids(LEByteUtil.ReadUInt16(dis));
        header.setYNumGrids(LEByteUtil.ReadUInt16(dis));
        header.setZNumGrids(LEByteUtil.ReadUInt16(dis));
        header.setRadarCount(LEByteUtil.ReadInt32(dis));
        header.setStartLon(LEByteUtil.ReadSingle(dis));
        header.setStartLat(LEByteUtil.ReadSingle(dis));
        header.setCenterLon(LEByteUtil.ReadSingle(dis));
        header.setCenterLat(LEByteUtil.ReadSingle(dis));
        header.setXReso(LEByteUtil.ReadSingle(dis));
        header.setYReso(LEByteUtil.ReadSingle(dis));

        float[] zhighGrids = new float[40];
        for (int i = 0; i < 40; i++) {
            zhighGrids[i] = LEByteUtil.ReadSingle(dis);
        }
        header.setZhighGrids(zhighGrids);

        String[] radarStationName = new String[20];
        for (int i = 0; i < 20; i++) {
            radarStationName[i] = LEByteUtil.ReadBytesString(dis, 16, "GBK").trim();
        }
        header.setRadarStationName(radarStationName);

        float[] radarLongitude = new float[20];
        for (int i = 0; i < 20; i++) {
            radarLongitude[i] = LEByteUtil.ReadSingle(dis);
        }
        header.setRadarLongitude(radarLongitude);

        float[] radarLatitude = new float[20];
        for (int i = 0; i < 20; i++) {
            radarLatitude[i] = LEByteUtil.ReadSingle(dis);
        }
        header.setRadarLatitude(radarLatitude);

        float[] radarAltitude = new float[20];
        for (int i = 0; i < 20; i++) {
            radarAltitude[i] = LEByteUtil.ReadSingle(dis);
        }
        header.setRadarAltitude(radarAltitude);

        byte[] mosaicFlag = new byte[20];
        dis.read(mosaicFlag);
        header.setMosaicFlag(mosaicFlag);

        dis.skip(172);

        return header;
    }

    private Date ReadTime(InputStream dis) throws IOException {

        Date dt = new Date(LEByteUtil.ReadUInt16(dis), LEByteUtil.ReadUInt16(dis),
                LEByteUtil.ReadUInt16(dis), LEByteUtil.ReadUInt16(dis), LEByteUtil
                .ReadUInt16(dis), 0);

        return dt;
    }

    public ProductType getPrdType() {
        return prdType;
    }

    public void setPrdType(ProductType prdType) {
        this.prdType = prdType;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public SwanHeader getHeader() {
        return header;
    }

    public void setHeader(SwanHeader header) {
        this.header = header;
    }

    public double[] getGridX() {
        return gridX;
    }

    public void setGridX(double[] gridX) {
        this.gridX = gridX;
    }

    public double[] getGridY() {
        return gridY;
    }

    public void setGridY(double[] gridY) {
        this.gridY = gridY;
    }

    public double getXMin() {
        return xMin;
    }

    public void setXMin(double min) {
        xMin = min;
    }

    public double getXMax() {
        return xMax;
    }

    public void setXMax(double max) {
        xMax = max;
    }

    public double getYMin() {
        return yMin;
    }

    public void setYMin(double min) {
        yMin = min;
    }

    public double getYMax() {
        return yMax;
    }

    public void setYMax(double max) {
        yMax = max;
    }

    public int getXNum() {
        return xNum;
    }

    public void setXNum(int num) {
        xNum = num;
    }

    public int getYNum() {
        return yNum;
    }

    public void setYNum(int num) {
        yNum = num;
    }

    public double[][][] getData() {
        return data;
    }

    public void setData(double[][][] data) {
        this.data = data;
    }
}
