package com.elcom.uploadservice.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonBuildFileM3u8Url {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonBuildFileM3u8Url.class);
    
    public static String createFileUrl(HttpServletRequest request, String path_local) {
        String urlFileNameM3u8 = "";
        BufferedWriter w;
        try {
            File fileLocal = new File(path_local);
            urlFileNameM3u8 = fileLocal.getParentFile() + File.separator + fileLocal.getName().replace(".ts", ".m3u8");
            String urlFileNameTs = fileLocal.getParentFile() + File.separator + fileLocal.getName();
            LOGGER.info("---- url steam m3u8: " + urlFileNameM3u8);

            File file = new File(urlFileNameM3u8);
            if (!file.exists()) {
                w = new BufferedWriter(new FileWriter(file));
                w.write("#EXTM3U");
                w.newLine();
                w.write("#EXT-X-VERSION:3");
                w.newLine();
                w.write("#EXT-X-PLAYLIST-TYPE:VOD");
                w.newLine();
                w.write("#EXT-X-TARGETDURATION:13");
                w.newLine();
                w.write("#EXT-X-MEDIA-SEQUENCE:0");
                w.newLine();
                w.write("#EXTINF:9.000000");
                w.newLine();
                w.write(request.getContextPath() + "/media/list/stream?file=" + urlFileNameTs);
                w.newLine();

                w.flush();
                w.close();
            }
        } catch (Exception e) {
            //System.err.println("Not build file .m3u8" + ex);
            e.printStackTrace();
            LOGGER.error(StringUtil.printException(e));
        }
        return urlFileNameM3u8;
    }

    public static String getFileExtension(String path_local) {
        File file = new File(path_local);
        String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            return "";
        }
    }

    public static void buildStreamFile(HttpServletRequest request, HttpServletResponse response
                                        , String filePathM3u8) throws UnsupportedEncodingException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        try {
            File fileLocal = new File(filePathM3u8);
            LOGGER.info("fileLocal.name: " + fileLocal.getName());
            OutputStream outStream;
            try (FileInputStream inStream = new FileInputStream(fileLocal)) {
                response.setContentType("application/octet-stream");
                response.setContentLength((int) fileLocal.length());
                String headerKey = "Content-Disposition";
                String headerValue = String.format("attachment; filename=\"%s\"", fileLocal.getName());
                response.setHeader(headerKey, headerValue);
                outStream = response.getOutputStream();
                byte[] buffer = new byte[4096];
                int bytesRead = -1;
                while ((bytesRead = inStream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, bytesRead);
                }
                LOGGER.info("FINISH 01 !!!!!!!!!!!");
            }
            LOGGER.info("FINISH 02 !!!!!!!!!!!");
            outStream.close();
        } catch (Exception ex) {
            try (PrintWriter out = response.getWriter()) {
                out.print("File không tồn tại!");
                out.flush();
            }
            LOGGER.error("Loi stream file : " + ex);
        }
    }
}
