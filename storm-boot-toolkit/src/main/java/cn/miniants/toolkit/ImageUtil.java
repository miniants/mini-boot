package cn.miniants.toolkit;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

public class ImageUtil {
    public static String readBase64FromInputStream(InputStream inputStream) throws IOException {
        return readBase64FromInputStream("image/jpeg", inputStream);
    }

    public static String readBase64FromInputStream(String imageType, InputStream inputStream) throws IOException {

        // 将输入流转换为字节数组
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, length);
        }
        byte[] bytes = outputStream.toByteArray();

        // 将字节数组转换为 Base64 编码的字符串
        String base64 = Base64.getEncoder().encodeToString(bytes);

        // 关闭输入流和输出流
        inputStream.close();
        outputStream.close();

        return "data:%s;base64,%s".formatted(imageType, base64);
    }

    public static String readBase64FromBytes(byte[] imgBytes) throws IOException {
        return readBase64FromBytes("image/jpeg", imgBytes);
    }
    public static String readBase64FromBytes(String imageType, byte[] imgBytes) throws IOException {

        // 将输入流转换为字节数组
        String srcFace= java.util.Base64.getEncoder().encodeToString(imgBytes);
        return "data:%s;base64,%s".formatted(imageType, srcFace);
    }
}


