package com.feng.freader.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class CharsetNameUtil {
    // 识别TXT文件的编码格式
    public static String getFileCharsetName(String fileName) throws IOException {
        InputStream inputStream = new FileInputStream(fileName);
        byte[] head = new byte[3];
        inputStream.read(head);

        String charsetName = "GBK";//或GB2312，即ANSI
        if (head[0] == -1 && head[1] == -2) //0xFFFE
            charsetName = "UTF-16";
        else if (head[0] == -2 && head[1] == -1) //0xFEFF
            charsetName = "Unicode";//包含两种编码格式：UCS2-Big-Endian和UCS2-Little-Endian
        else if (head[0] == -27 && head[1] == -101 && head[2] == -98)
            charsetName = "UTF-8"; //UTF-8(不含BOM)
        else if (head[0] == -17 && head[1] == -69 && head[2] == -65)
            charsetName = "UTF-8"; //UTF-8-BOM

        inputStream.close();

        return charsetName;
    }
}
