package com.elcom.uploadservice.utils;

import com.elcom.uploadservice.config.PropertiesConfig;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author hanm
 */
public class ShellUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShellUtils.class);

    public static boolean executeCommand(String cmd) {
        try {
            Runtime run = Runtime.getRuntime();
            String[] commands = {"bash", "-c", cmd};

            Process pr = null;
            if (SystemUtils.IS_OS_LINUX) {
                pr = run.exec(commands);
            } else {
                pr = run.exec(cmd);
            }
            BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line = "";
            while ((line = buf.readLine()) != null) {
                LOGGER.info("data: " + line + " - " + Runtime.getRuntime().availableProcessors());
            }
            int exitVal = pr.waitFor();
            LOGGER.info(String.format("Execute: %s => exited with error code : %d", cmd, exitVal));
            return exitVal == 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static File mergeAudio(String filePathFrom, String filePathTo) {
        if( StringUtil.isNullOrEmpty(filePathFrom) || StringUtil.isNullOrEmpty(filePathTo) )
            return null;
        try {
            File directory = new File(PropertiesConfig.ROOT_FOLDER_FILE_MERGE_AUDIO);
            if( !directory.exists() )
                directory.mkdir();
            
            String fileOut = PropertiesConfig.ROOT_FOLDER_FILE_MERGE_AUDIO + "/"
                            + filePathFrom.substring(filePathFrom.lastIndexOf("/") + 1, filePathFrom.lastIndexOf(".")) + "_" 
                            + filePathTo.substring(filePathTo.lastIndexOf("/") + 1, filePathTo.lastIndexOf(".")) + ".mp3";
            if ( Files.exists(Paths.get(fileOut)) ) {
                LOGGER.info("File merged [{}] existed, return!", fileOut);
                return new File(fileOut);
            }

            // ffmpeg -y -i /home/gpcn/log/5.mp3 -i /home/gpcn/log/6.mp3 -filter_complex "[0:0][1:0] amix=inputs=2:duration=longest" -c:a libmp3lame /home/gpcn/log/merge3.mp3
            String[] cmd = new String[] { "ffmpeg", "-y", "-i", filePathFrom, "-i", filePathTo, "-filter_complex"
                                , "[0:0][1:0] amix=inputs=2:duration=longest", "-c:a", "libmp3lame", fileOut };
            LOGGER.info("cmd: {}", Arrays.toString(cmd));

            long startTime = System.currentTimeMillis();
            ProcessBuilder processBuilder = new ProcessBuilder(cmd);
            Process process = processBuilder.start();

//                BufferedReader buf = new BufferedReader(new InputStreamReader(process.getInputStream()));
//                String line = "";
//                while( (line = buf.readLine()) != null )
//                    LOGGER.info("line: [" + line + "]");

            int exitCode = process.waitFor();
            LOGGER.info("ShellUtils.mergeAudio() process finish, elapse time: " + getElapsedTime(System.currentTimeMillis() - startTime) + ", exitCode: " + exitCode);

            boolean isFileOutExisted = Files.exists(Paths.get(fileOut));
            boolean isFileOutReadable = Files.isReadable(Paths.get(fileOut));
            if( exitCode == 0 && isFileOutExisted && isFileOutReadable ) {
                LOGGER.info("ShellUtils.mergeAudio() merge SUCCESS, return file: [{}]", fileOut);
                return new File(fileOut);
            }else
                LOGGER.error("ShellUtils.mergeAudio() merge FAILED, isFileOutExisted: {}, isFileOutReadable: {}"
                            , isFileOutExisted, isFileOutReadable);
        } catch (Exception ex) {
            ex.printStackTrace();
            StringUtil.printException(ex);
        }
        return null;
    }

    public static String createImageFromVideo(String videoLink, String outputFile) {
        if (StringUtil.isNullOrEmpty(videoLink) || StringUtil.isNullOrEmpty(outputFile)) {
            return null;
        }
        String[] cmd = null;
        String videoName = null;
        if (videoLink.endsWith("m3u8")) {
            videoName = UUID.randomUUID().toString() + ".png";
            cmd = new String[]{"ffmpeg", "-i", videoLink, "-s", "400x222", "-ss", "00:00:14.435",
                "-vframes", "1", outputFile + "/" + videoName};
        } else if (videoLink.endsWith("mp4")) {
            videoName = UUID.randomUUID().toString() + ".jpg";
            cmd = new String[]{"ffmpeg", "-i", videoLink, "-vf", "fps=1", outputFile + "/" + videoName};
        }
        if (!StringUtil.isNullOrEmpty(videoName)) {
            try {
                System.out.println("cmd: " + Arrays.toString(cmd));
                ProcessBuilder processBuilder = new ProcessBuilder(cmd);
                Process process = processBuilder.start();
                BufferedReader buf = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line = "";
                while ((line = buf.readLine()) != null) {
                    System.out.println(line);
                }
                int exitCode = process.waitFor();
                System.out.println("exitCode: " + exitCode);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return videoName;
    }

    public static String createImageFromVideo2(String videoLink, String outputFilePath) {
        if (StringUtil.isNullOrEmpty(videoLink) || StringUtil.isNullOrEmpty(outputFilePath)) {
            return null;
        }
        String[] cmd = null;
        if (videoLink.endsWith("m3u8")) {
            cmd = new String[]{"ffmpeg", "-i", videoLink, "-s", "400x222", "-ss", "00:00:14.435",
                "-vframes", "1", outputFilePath};
        } else if (videoLink.endsWith("mp4")) {
            cmd = new String[]{"ffmpeg", "-i", videoLink, "-vf", "fps=1", outputFilePath};
        } else if (videoLink.endsWith("mjpg")) {
            cmd = new String[]{"ffmpeg", "-i", videoLink, "-vframes", "1", "-y", outputFilePath};
        }
        if (cmd != null && cmd.length > 0) {
            try {
                System.out.println("cmd: " + Arrays.toString(cmd));
                ProcessBuilder processBuilder = new ProcessBuilder(cmd);
                Process process = processBuilder.start();
                BufferedReader buf = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line = "";
                while ((line = buf.readLine()) != null) {
                    System.out.println(line);
                }
                int exitCode = process.waitFor();
                System.out.println("exitCode: " + exitCode);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return outputFilePath;
    }
    
    private static String getElapsedTime(long miliseconds) {
        return miliseconds + " (ms)";
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        String rootCmd = "cmd.exe /c";
        //String cmd = "dir -L";
        String cmd = "ping stackjava.com -n 5";
        //String cmd = "curl -X GET \"http://103.21.151.157:9200/_cat/indices?v\"";
        boolean result = executeCommand(rootCmd + " " + cmd);
        System.out.println("result: " + result);
    }
}
