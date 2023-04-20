package cn.miniants.toolkit;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.stream.Stream;


/**
 * @description: pdf相关的工具类
 */
@Component
public class PdfUtil {

    /**
     * 将图片转换为PDF文件
     *
     * @return PDF文件
     * @throws IOException IO异常
     */
    public static void writePdfOutputStream(OutputStream pdfOutputStream, Stream<Image> ims) throws RuntimeException {
        try {

            Document doc = new Document(PageSize.A4, 20, 20, 20, 20);
            PdfWriter.getInstance(doc, pdfOutputStream);
            doc.open();

            ims.forEach(i->{
                Rectangle pageSize = new Rectangle(i.getWidth(), i.getHeight());
                doc.setPageSize(pageSize);
                doc.newPage();
                try {
                    doc.add(i);
                } catch (DocumentException e) {
                    throw new RuntimeException(e);
                }
            });
            doc.close();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static void writePdfFile(String filename, Stream<Image> ims) throws RuntimeException{
        try {
            File docFile = new File(filename);
            OutputStream pdfOutputStream = new FileOutputStream(docFile);
            writePdfOutputStream(pdfOutputStream, ims);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static Image inputStreamToImage(InputStream inputStream) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n;
        try {
            while (-1 != (n = inputStream.read(buffer))) {
                output.write(buffer, 0, n);
            }

            Image image = Image.getInstance(output.toByteArray());
            float height = image.getHeight();
            float width = image.getWidth();
            //int percent = getPercent(height, width);
            image.setAlignment(Image.MIDDLE);
            //image.scalePercent(percent);
            return image;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 用于下载PDF文件
     *
     * @param pdfFile  PDF文件
     * @param response HttpServletResponse
     * @throws IOException IO异常
     */
    private static void downloadPdfFile(File pdfFile, HttpServletResponse response) throws IOException {
        FileInputStream fis = new FileInputStream(pdfFile);
        byte[] bytes = new byte[fis.available()];
        fis.read(bytes);
        fis.close();

        response.reset();
        response.setHeader("Content-Type", "application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(pdfFile.getName(), "UTF-8"));
        OutputStream out = response.getOutputStream();
        out.write(bytes);
        out.flush();
        out.close();
    }

}

