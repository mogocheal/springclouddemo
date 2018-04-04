package com.example.cmiss.utils;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class ReadSatellite {
	
	//一级文件头参数
	String fileName = "";//文件名
	short byteOrder;//型数的字节顺序
	short firstHeaderLength;//第一级文件头长度
    short secondHeaderLength;//第二级文件头长度
    short fillSectionLength;//填充段数据长度
    short recoderLength;//记录长度
    short recodsOfHeader;//文件头占用记录数
    short recodsOfData;//产品数据占用记录数
    short typeOfProduct;//产品类型
    short typeOfCompress;//压缩方式
    String strVersion = "";//格式说明字符串
    short flagOfQuality;//产品数据质量标记
    
    //二级文件头参数
    String satelliteName = "";//卫星名
    short year;//时间（年）
    short month;//时间（月）
    short day;//时间（日）
    short hour;//时间（时）
    short minute;//时间（分）
    short channel;//通道号
    short flagOfProjection;//投影方式
    short widthOfImage;//图像宽度
    short heightOfImage;//图像高度
    short scanLineNumberOfImageTopleft;//图像左上角扫描线号
    short pixelNumberOfImageTopLeft;//图像左上角象元号
    short sampleRatio;//抽样率
    short latitudeOfNorth;//地理范围（北纬）
    short latitudeOfSouth;//地理范围（南纬）
    short longitudeOfWest;//地理范围（西经）
    short longitudeOfEast;//地理范围（东经）
    short centerLatitudeOfProjection;//投影中心纬度 度x100
    short centerLongitudeOfProjection;//投影中心经度 度x100
    short standardLatitude1;//标准投影纬度1（或标准经度）
    short standardLatitude2;//标准投影纬度2
    short horizontalResolution;//投影水平分辨率 公里x100
    short verticalResolution;//投影垂直分辨率 公里x100
    short overlapFlagGeoGrid;//地理网格叠加标志
    short overlapValueGeoGrid;//地理网格叠加值
    short dataLengthOfColorTable;//调色表数据块长度
    short dataLengthOfCalibration;//定标数据块长度
    short dataLengthOfGeolocation;//定位数据块长度
    short reserved;//保留
    
  //文件头填充数据变量
    byte[] fillData = new byte[5596];

    //定义short类型的1300x1520的二维数组存放图形数据
    int [][] imageData = new int [1300][1520];
    byte [] temp=new byte [1300*1900];

    //渲染
    private static Color[] ColorIndex = new Color[256];

    public ReadSatellite()
    {
    }
    
    private Color setRGB(int r, int g, int b,int a)
    {
        Color mycolor = new Color(r, g, b,a);
        return mycolor;
    }
    
    public void readFile(File file){
    	try {
			DataInputStream fis = new DataInputStream(new FileInputStream(file));
			
			fileName = getChar(12,fis);	
			byteOrder = fis.readShort();
			firstHeaderLength = fis.readShort();
			secondHeaderLength = fis.readShort();
            fillSectionLength = fis.readShort();
            recoderLength = fis.readShort();
            recodsOfHeader = fis.readShort();
            recodsOfData = fis.readShort();
            typeOfProduct = fis.readShort();
            typeOfCompress = fis.readShort();            
            strVersion = getChar(8,fis);
            flagOfQuality = fis.readShort();
            
          //读取第二级文件头的参数值
            satelliteName = getChar(8,fis);
            year = getShort(fis);
            month = fis.readShort();
            day = fis.readShort();
            hour = fis.readShort();
            minute = fis.readShort();
            channel = getShort(fis);
            flagOfProjection = getShort(fis);
            widthOfImage = fis.readShort();
            heightOfImage = fis.readShort();
            scanLineNumberOfImageTopleft = fis.readShort();
            pixelNumberOfImageTopLeft = fis.readShort();
            sampleRatio = fis.readShort();
            latitudeOfNorth = getShort(fis);
            latitudeOfSouth = getShort(fis);
            longitudeOfWest = getShort(fis);
            longitudeOfEast = getShort(fis);
            centerLatitudeOfProjection = getShort(fis);
            centerLongitudeOfProjection = getShort(fis);
            standardLatitude1 = fis.readShort();
            standardLatitude2 = fis.readShort();
            horizontalResolution = fis.readShort();
            verticalResolution = fis.readShort();
            overlapFlagGeoGrid = fis.readShort();
            overlapValueGeoGrid = fis.readShort();
            dataLengthOfColorTable = fis.readShort();
            dataLengthOfCalibration = fis.readShort();
            dataLengthOfGeolocation = fis.readShort();
            reserved = fis.readShort();
            
          //读取填充段数据
            fis.read(fillData);

            //读取图像数据
            fis.read(temp);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    //保存图像
    public void saveImage(String imagePath){
    	System.out.println("---"+imagePath);
        //读取色卡文件，并存入颜色数组
        try {
        	String pal = "/I-01.pal";
        	if(channel == 4){ //可见光
        		pal = "/V-01.pal";
        	}else if(channel == 2){ //水汽
        		pal = "/W-01.pal";
        	}
            ResourceLoader resourceLoader=new DefaultResourceLoader();
            Resource resource=resourceLoader.getResource("classpath:satellite/"+pal);
			FileReader fr = new FileReader( resource.getFile());
			BufferedReader br = new BufferedReader(fr);
			String line = null;
			int r=0,g=0,b=0,i=0;
			line = br.readLine();
			while((line=br.readLine())!=null){
				String[] strTemp = line.trim().split("\\s+");
				b = Integer.valueOf(strTemp[1]);
				g = Integer.valueOf(strTemp[2]);
				r = Integer.valueOf(strTemp[3]);
				
				ColorIndex[i]=setRGB(r,g,b,127);
				i++;
			}
			
			File imageFile = new File(imagePath); 
			int width = 1900;
			int height = 1300;
			BufferedImage bi = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);

			Color c ;
			for(int j =0;j<1300*1900;j++){
				if(temp[j] < 0){
					c = ColorIndex[temp[j] + 256];
				}else{
					c = ColorIndex[temp[j]];
				}

                bi.setRGB(j%1900, j/1900, c.getRGB());
			}
			FileUtils.createFile(imagePath);
			ImageIO.write(bi,"PNG",imageFile);// 输出到文件流  
			bi.flush();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public static short byteToInt2(byte[] b) {
    	 return (short) (((b[1] << 8) | b[0] & 0xff)); 
}
    //读取指定长度的字符，未考虑是否读到末尾的情况
    private String getChar(int len,DataInputStream dis ){
    	byte []b = new byte[len];
    	try {
    		dis.read(b);
			return new String(b);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return "";
    }
    //读取指定长度的字符，未考虑是否读到末尾的情况
    private short getShort(DataInputStream dis ){
    	byte []b = new byte[2];
    	try {
    		dis.read(b);
    		return byteToInt2(b);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return 0;
    }
    
    public static void main(String[] args) {
    	String filePath = "D:\\SATE_L2_F2E_VISSR_MWB_OTG_SEC-IR1-20150507-0300.AWX";
    	String imagePath = "d://003.png";
    	ReadSatellite rs = new ReadSatellite();
		rs.readFile(new File(filePath));
		rs.saveImage(imagePath);
		System.out.println("OK.");
		
		System.out.println(rs.fileName);
		System.out.println(rs.strVersion);
		System.out.println(rs.satelliteName);
		System.out.println(rs.year);
		System.out.println(rs.channel);

		System.out.println("OK.");
		System.out.println(rs.latitudeOfNorth);//投影中心经度 度x100
		System.out.println(rs.latitudeOfSouth);//投影中心经度 度x100
		System.out.println(rs.longitudeOfWest);//投影中心经度 度x100
		System.out.println(rs.longitudeOfEast);//投影中心经度 度x100
		System.out.println("OK.");
		System.out.println(rs.flagOfProjection);
		System.out.println(rs.centerLatitudeOfProjection);//投影中心经度 度x100
		System.out.println(rs.centerLongitudeOfProjection);//投影中心经度 度x100
		
/**
 * OK.
5997
65040
5002
14497
OK.
2750
9750
 */
	}
}
