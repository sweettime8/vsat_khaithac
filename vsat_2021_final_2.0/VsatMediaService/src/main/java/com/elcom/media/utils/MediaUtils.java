package com.elcom.media.utils;

import com.elcom.media.config.ApplicationConfig;
import com.elcom.media.enums.MediaStreamFileType;
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

public class MediaUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(MediaUtils.class);
    
    /** Tạo file .m3u8 từ file .ts
     * @param tsFileInput (đường dẫn file .ts)
     * @return m3u8FullPath: đường dẫn truy cập file .m3u8 */
    public static String generateM3u8ByTs(String tsFileInput) {
        String m3u8FullPath = "";
        BufferedWriter w;
        try {
            LOGGER.info("==> tsFileInput: " + tsFileInput);
            File tsFileInputLocal = new File(tsFileInput);
            String urlFileNameTs = tsFileInputLocal.getParentFile() + File.separator + tsFileInputLocal.getName();
            String m3u8LocalFile = tsFileInputLocal.getParentFile() + File.separator + tsFileInputLocal.getName().replace(MediaStreamFileType.TS.toVal(), MediaStreamFileType.M3U8.toVal());
            LOGGER.info("==> m3u8LocalFile will be generate: " + m3u8LocalFile);
            
            File file = new File(m3u8LocalFile);
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
                String tsLocalFilePath = ApplicationConfig.MEDIA_LINK_ROOT_API + urlFileNameTs.replace(ApplicationConfig.ROOT_FOLDER_FILE_PATH_INTERNAL, "");
                w.write(tsLocalFilePath);
                m3u8FullPath = tsLocalFilePath.replace(ApplicationConfig.ROOT_FOLDER_FILE_PATH_INTERNAL, "").replace(MediaStreamFileType.TS.toVal(), MediaStreamFileType.M3U8.toVal());
                LOGGER.info("==> m3u8FullPath return: {}", m3u8FullPath);
                // http://192.168.61.106:8413/v1.0/stream?file=/ttttbien2/vsat/media_files/video/20210205/15/29/cdd459de-7761-4c6f-81db-3858e9de0818_mpeg2.ts
                // http://192.168.61.106:8413/v1.0/media-files/
                // http://192.168.61.106:8413/v1.0/media-files/video/20210205/15/29/cdd459de-7761-4c6f-81db-3858e9de0818_mpeg2.ts
                w.newLine();

                w.flush();
                w.close();
            }else {
                m3u8FullPath = ApplicationConfig.MEDIA_LINK_ROOT_API + m3u8LocalFile.replace(ApplicationConfig.ROOT_FOLDER_FILE_PATH_INTERNAL, "");
                LOGGER.info("==> m3u8FullPath return (existed): {}", m3u8FullPath);
            }     
        } catch (Exception e) {
            //System.err.println("Not build file .m3u8" + ex);
            e.printStackTrace();
            LOGGER.error(StringUtil.printException(e));
        }
        return m3u8FullPath;
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
            }
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
