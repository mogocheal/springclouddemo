package com.example.cmiss.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

/**
 * Created by Administrator on 2016/8/24 0024.
 */
public class FileUtil {

    public static String getExtension(String fileName){
        String extension = "";

        int i = fileName.lastIndexOf('.');
        int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

        if (i > p) {
            extension = fileName.substring(i+1);
        }

        return extension;
    }

    public static void download(HttpServletResponse response, String filepath, String fileName, String suffix){
        try {
            OutputStream os = response.getOutputStream();
            byte[] bt = new byte[1024];
            File fileLoad = new File(filepath);
            fileName = URLEncoder.encode(fileName, "UTF-8");//处理中文文件名
            fileName = new String(fileName.getBytes("UTF-8"),"GBK")+suffix;//处理中文文件名
            response.addHeader("Content-disposition", "attachment;filename=" + fileName);
            response.addHeader("Content-Length", "" + fileLoad.length());
            //response.setContentType("application/x-msdownload");
            response.setContentType("application/octet-stream");
            //response.setContentType("application/vnd.ms-excel");
            FileInputStream fis = new FileInputStream(fileLoad);
            int n = 0;
            while ((n = fis.read(bt)) != -1) {
                os.write(bt, 0, n);
            }
            os.flush();
            os.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除指定路径的文件
     * @param filePath
     */
    public static void deleteFile(String filePath){
        File file = new File(filePath);
        if(file.exists()){
            file.delete();
        }
    }

    /**检查磁盘上文件或者目录是否存在
     * @author LZQ
     * @param path 文件或者目录路径
     * @param fileType 文件还是目录
     */
    public static void checkFileOnDisk(String path,String fileType){
        if(path!=null){
            path = path.replaceAll("\\\\", "/");
        }
        if(fileType.equals("file")){
            File file = new File(path);
            if(!file.exists()){
                String folderPath = path.substring(0,path.lastIndexOf("/"));
                try {
                    File folderFile = new File(folderPath);
                    folderFile.mkdirs();
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else if(fileType.equals("folder")){
            File folderFile = new File(path);
            if(!folderFile.exists()){
                folderFile.mkdirs();
            }
        }else if(fileType.equals("dir")){
            File folderFile = new File(path);
            if(!folderFile.exists()){
                folderFile.mkdirs();
            }
        }
    }

}
