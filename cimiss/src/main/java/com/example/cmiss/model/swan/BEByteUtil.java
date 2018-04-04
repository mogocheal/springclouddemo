package com.example.cmiss.model.swan;


import java.io.IOException;
import java.io.InputStream;

/**
 * 大端存储模式格式读取
 *
 * @author blq
 */
public class BEByteUtil {
    public static float ReadSingle(InputStream in) throws IOException {
        return Float.intBitsToFloat(ReadInt32(in));
    }

    public static int ReadInt32(InputStream in) throws IOException {
        int ch1 = in.read();
        int ch2 = in.read();
        int ch3 = in.read();
        int ch4 = in.read();
        if ((ch1 | ch2 | ch3 | ch4) < 0)
            return 0;
        return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
    }

    public static short ReadInt16(InputStream in) throws Exception {
        int ch1 = in.read();
        int ch2 = in.read();
        if ((ch1 | ch2) == -1) {
            //throw new Exception("已经读到文件结尾");
            return -1111;
        }
        if ((ch1 | ch2) < 0)
            return 0;
        return (short) ((ch1 << 8) + (ch2 << 0));
    }

    public static int ReadUInt16(InputStream in) throws IOException {
        int ch1 = in.read();
        int ch2 = in.read();
        if ((ch1 | ch2) < 0)
            return 0;
        return (ch1 << 8) + (ch2 << 0);
    }

    public static String ReadBytesString(InputStream dis, int byetesLen, String encode)
            throws IOException {

        byte[] bytes = new byte[byetesLen];
        dis.read(bytes);
        return new String(bytes, encode);
    }

    public static char[] ReadChars(InputStream dis, int len)
            throws IOException {
        char[] chars = new char[len];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char) dis.read();
        }
        return chars;
    }

}
