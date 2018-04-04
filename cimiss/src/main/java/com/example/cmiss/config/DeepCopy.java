package com.example.cmiss.config;

import java.io.*;
import java.util.List;

/**
 * 深拷贝
 * @author Administrator
 *
 */
public class DeepCopy {
	 public static List copyBySerialize(List src) {
	        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
	        ObjectOutputStream out;
			try {
				out = new ObjectOutputStream(byteOut);
				out.writeObject(src);
				
				ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
				ObjectInputStream in =new ObjectInputStream(byteIn);
				List dest = (List)in.readObject();
				return dest;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
	    }


	public static <T> T deepClone(T obj) throws IOException, ClassNotFoundException {

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(obj);
		// 将流序列化成对象
		ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
		ObjectInputStream ois = new ObjectInputStream(bis);
		return (T) ois.readObject();
	}
}
