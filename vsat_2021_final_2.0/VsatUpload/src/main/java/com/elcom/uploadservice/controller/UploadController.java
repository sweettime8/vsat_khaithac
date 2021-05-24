package com.elcom.uploadservice.controller;

import com.elcom.gateway.message.RequestMessage;
import com.elcom.gateway.message.ResponseMessage;
import com.elcom.uploadservice.config.PropertiesConfig;
import com.elcom.uploadservice.config.UploadConfig;
import com.elcom.uploadservice.constant.Constant;
import com.elcom.uploadservice.exception.ValidationException;
import com.elcom.uploadservice.messaging.rabbitmq.RabbitMQClient;
import com.elcom.uploadservice.messaging.rabbitmq.RabbitMQProperties;
import com.elcom.uploadservice.model.dto.MediaRawDTO;
import com.elcom.uploadservice.model.dto.MediaRawRelationDTO;
import com.elcom.uploadservice.model.dto.MediaRelationDTO;
import com.elcom.uploadservice.model.dto.ResponseMessageDTO;
import com.elcom.uploadservice.model.dto.UploadDTO;
import com.elcom.uploadservice.service.ExcelService;
import com.elcom.uploadservice.service.impl.FileStorageServiceImpl;
import com.elcom.uploadservice.upload.UploadFileResponse;
import com.elcom.uploadservice.utils.CommonBuildFileM3u8Url;
import com.elcom.uploadservice.utils.DateUtil;
import com.elcom.uploadservice.utils.JSONConverter;
import com.elcom.uploadservice.utils.ShellUtils;
import com.elcom.uploadservice.utils.StringUtil;
import com.elcom.uploadservice.validation.UploadValidation;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 *
 * @author Admin
 */
@RestController
@RequestMapping("/v1.0")
public class UploadController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadController.class);
    
    @Autowired
    private FileStorageServiceImpl fileStorageService;

    @Autowired
    private RabbitMQClient rabbitMQClient;

    @Value("${user.authen.use}")
    private String authenUse;

    @Value("${user.authen.http.url}")
    private String authenHttpUrl;

    /**
     * Upload file
     *
     * @param files
     * @param headerMap
     * @param request
     * @return image upload link
     */
    @RequestMapping(value = "/upload/**", method = RequestMethod.POST)
    public ResponseEntity<Object> uploadFile(@RequestParam(value = "file", required = false) MultipartFile[] files,
            @RequestHeader Map<String, String> headerMap, HttpServletRequest request) {
        //Request path
        String requestPath = request.getRequestURI();
        if (requestPath != null && requestPath.contains(Constant.API_ROOT_PATH)) {
            requestPath = requestPath.replace(Constant.API_ROOT_PATH, "/");
        }
        //Service
        int index = requestPath.indexOf("/", "/upload/".length());
        String service = null;
        if (index != -1) {
            service = requestPath.substring("/upload/".length(), index);
        } else {
            service = requestPath.replace("/upload/", "");
        }
        System.out.println("requestPath: " + requestPath + ",service: " + service);
        UploadDTO dto = UploadConfig.UPLOAD_DEFINE_MAP.get(requestPath);
        //Validation
        new UploadValidation().validate(requestPath, service, files, dto);

        //Authen 
        ResponseMessage response = null;
        if ("http".equalsIgnoreCase(authenUse)) {
            LOGGER.info("Http authen - authorization " + headerMap.get("authorization"));
            // Http -> Call api authen
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", headerMap.get("authorization"));
            headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Dữ liệu đính kèm theo yêu cầu.
            HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<ResponseMessage> result = restTemplate.exchange(authenHttpUrl, HttpMethod.GET, requestEntity, ResponseMessage.class);
            if (result != null && result.getStatusCode() == HttpStatus.OK) {
                response = result.getBody();
            }
            LOGGER.info("Http authen response : {}", response != null ? response.toJsonString() : null);
        } else {
            //Authen -> call rpc authen headerMap
            RequestMessage userRpcRequest = new RequestMessage();
            userRpcRequest.setRequestMethod("POST");
            userRpcRequest.setRequestPath(RabbitMQProperties.USER_RPC_AUTHEN_URL);
            userRpcRequest.setBodyParam(null);
            userRpcRequest.setUrlParam(null);
            userRpcRequest.setHeaderParam(headerMap);
            LOGGER.info("Call RPC authen - authorization " + headerMap.get("authorization"));
            LOGGER.info("RequestMessage userRpcRequest : " + userRpcRequest.toJsonString());
            String result = rabbitMQClient.callRpcService(RabbitMQProperties.USER_RPC_EXCHANGE,
                    RabbitMQProperties.USER_RPC_QUEUE, RabbitMQProperties.USER_RPC_KEY, userRpcRequest.toJsonString());
            LOGGER.info("RPC authen response : {}", result);
            if (result != null) {
                ObjectMapper mapper = new ObjectMapper();
                //DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //mapper.setDateFormat(df);
                try {
                    response = mapper.readValue(result, ResponseMessage.class);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    LOGGER.info(StringUtil.printException(ex));
                }
            }
        }

        if (response != null && response.getStatus() == HttpStatus.OK.value()) {
            //Process upload
            String ddmmyyyy = DateUtil.today("ddMMyyyy");
            String uploadDir = dto.getFolder();

            List<UploadFileResponse> list = new ArrayList<>();
            UploadFileResponse uploadFileResponse = null;
            for (MultipartFile file : files) {
                String fileName = fileStorageService.storeFile(file, uploadDir);
                String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path(Constant.API_ROOT_PATH + uploadDir.replace("{ddmmyyyy}", ddmmyyyy) + "/")
                        .path(fileName)
                        .toUriString();
                LOGGER.info("Upload file url: " + fileDownloadUri);
                uploadFileResponse = new UploadFileResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize());
                list.add(uploadFileResponse);
            }
            //if (list != null && list.size() == 1) {
            //    return new ResponseEntity(new ResponseMessageDTO(list), HttpStatus.OK);
            //}
            return new ResponseEntity(new ResponseMessageDTO(list), HttpStatus.OK);
        } else {
            return new ResponseEntity(new ResponseMessageDTO("Token đăng nhập hết hạn"), HttpStatus.FORBIDDEN);
        }
    }

    /**
     * Response file upload
     *
     * @param request
     * @return file
     * @throws IOException
     */
    @RequestMapping(value = "/upload/**", method = RequestMethod.GET)
    public ResponseEntity<Resource> viewFile(HttpServletRequest request) throws IOException {
        
        String filePath = request.getRequestURI();
        if( filePath == null ) 
            return ResponseEntity.badRequest().body(null);
        
        if ( filePath.contains("/v1.0/") ) filePath = filePath.replace("/v1.0/", "");
        
        int lastIndex = filePath.lastIndexOf("/");
        String fileName = lastIndex != -1 ? filePath.substring(lastIndex + 1) : filePath;

        // Fallback to the default content type if type could not be determined
        ContentDisposition contentDisposition = ContentDisposition.builder("inline").filename(fileName).build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(contentDisposition);

        // Load file as Resource
        Resource resource;
        try {
            resource = fileStorageService.loadFileAsResource(filePath);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.info(StringUtil.printException(e));
            return ResponseEntity.badRequest().body(null);
        }
        // Try to determine file's content type
        String contentType;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.info(StringUtil.printException(ex));
            contentType = "application/octet-stream";
        }
        
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(30, TimeUnit.MINUTES))
                .contentType(MediaType.parseMediaType(contentType))
                .contentLength(resource.contentLength())
                .headers(headers)
                .body(resource);
    }
    
    /**
     * Response media file
     *
     * @param request
     * @return file
     * @throws IOException
     */
    @RequestMapping(value = "/media-files/**", method = RequestMethod.GET)
    public ResponseEntity<Resource> responseMediaFiles(HttpServletRequest request) throws IOException {
        
        String filePath = request.getRequestURI();
        if( filePath == null ) 
            return ResponseEntity.badRequest().body(null);
        
        String localPath = PropertiesConfig.ROOT_FOLDER_FILE_PATH_INTERNAL + "/" + filePath.substring(filePath.indexOf("media-files/")+12);
        LOGGER.info("==> localPath: [{}]", localPath);
        
        // Load file as Resource
        Resource resource;
        try {
            resource = fileStorageService.loadFileAsResource(localPath);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.info(StringUtil.printException(e));
            return ResponseEntity.badRequest().body(null);
        }
//        LOGGER.info("==> resource.fileName:   [{}]", resource.getFilename());
//        LOGGER.info("==> resource.isFile:     [{}]", resource.isFile());
//        LOGGER.info("==> resource.isReadable: [{}]", resource.isReadable());
        
        // Fallback to the default content type if type could not be determined
        ContentDisposition contentDisposition = ContentDisposition.builder("inline")
                                                                  .filename(resource.getFilename()).build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(contentDisposition);
        
        // Try to determine file's content type
        String contentType;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            LOGGER.info("contentType: {}", contentType);
            if( StringUtil.isNullOrEmpty(contentType) ) {
                if( localPath.endsWith(".ts") )
                    contentType = "video/MP2T";
                else if( localPath.endsWith(".m3u8") )
                    contentType = "application/x-mpegURL"; // hoặc application/vnd.apple.mpegURL
                else if( localPath.endsWith(".eml") )
                    contentType = "message/rfc822"; // hoặc application/octet-stream
                else if( localPath.endsWith(".pcap") )
                    contentType = "application/vnd.tcpdump.pcap";
                else {
                    contentType = "text/html";
                    LOGGER.info("set default contentType is text/html.");
                }
                LOGGER.info("contentType: {}", contentType);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.info(StringUtil.printException(ex));
            contentType = "application/octet-stream";
        }
        
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(30, TimeUnit.MINUTES))
                .contentType(MediaType.parseMediaType(contentType))
                .contentLength(resource.contentLength())
                .headers(headers)
                .body(resource);
    }
    
    @RequestMapping(value = {"/stream/**"}, method = RequestMethod.GET)
    public void stream(HttpServletRequest request, HttpServletResponse response, @RequestParam("file") String filePathM3u8) {
        try {
            CommonBuildFileM3u8Url.buildStreamFile(request, response, filePathM3u8);
        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.info(StringUtil.printException(ex));
        }
    }
    
    /**
     * Trả media file cho client cần download.
     *
     * @param requestBody
     * @return vsat-media-files.zip chứa 01 file Excel chứa thông tin các bản tin media cần tải và (n) file media cần tải.
     */
    @RequestMapping(value = "/download-media-files", method = RequestMethod.POST, produces = "application/zip")
    public ResponseEntity<StreamingResponseBody> downloadMediaFiles(@RequestBody(required = true)
            Map<String, Object> requestBody) {
        if (requestBody == null || requestBody.isEmpty())
            return ResponseEntity.badRequest().body((StreamingResponseBody) (OutputStream out) -> {
                throw new ValidationException("PayLoad body is missing!");
            });
        
        List<MediaRawDTO> mediaFilesInput;
        try {
            String dataLstStr = JSONConverter.toJSON(requestBody.get("mediaFiles"));
            if( StringUtil.isNullOrEmpty(dataLstStr) ) {
                String msgErr = "PayLoad body is invalid!";
                return ResponseEntity.badRequest().body((StreamingResponseBody) (OutputStream out) -> {
                    LOGGER.error(msgErr);
                    throw new ValidationException(msgErr);
                });
            }
            mediaFilesInput = new ObjectMapper().readValue(dataLstStr, new TypeReference<List<MediaRawDTO>>(){});
        } catch (Exception e) {
            LOGGER.error(StringUtil.printException(e));
            return ResponseEntity.badRequest().body((StreamingResponseBody) (OutputStream out) -> {
                throw new ValidationException("PayLoad body is invalid!");
            });
        }
        
        LOGGER.info("==> mediaFilesInput.size: {}", mediaFilesInput!=null && !mediaFilesInput.isEmpty() ? mediaFilesInput.size() : 0);
        if( mediaFilesInput==null || mediaFilesInput.isEmpty() )
            return ResponseEntity.badRequest().body((StreamingResponseBody) (OutputStream out) -> {
                throw new ValidationException("PayLoad body is invalid!");
            });
        
        String zipFileName = "vsat-media-files.zip";
        ContentDisposition contentDisposition = ContentDisposition.builder("inline").filename(zipFileName).build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(contentDisposition);

        return ResponseEntity.ok().headers(headers).cacheControl(CacheControl.maxAge(30, TimeUnit.MINUTES))
                .contentType(MediaType.parseMediaType("application/zip"))
                .body(out -> {
                    try (ZipOutputStream zipOutputStream = new ZipOutputStream(out)) {
                        List<File> mediaFilesToZip = new ArrayList<>();
                        
                        // Tạo file excel
                        File excelFile = new File("vsat-media-raws.xlsx");
                        ExcelService.writeMediaRawToExcel(mediaFilesInput, excelFile);
                        
                        // Thêm file excel
                        if( excelFile.exists() )
                            mediaFilesToZip.add(excelFile);
                        
                        // Thêm các file media
                        mediaFilesInput.forEach((media) -> {
                            try {
                                File file = new File(media.getFilePathLocal());
                                if( file.exists() )
                                    mediaFilesToZip.add(file);
                            } catch (Exception e) {
                                LOGGER.error(StringUtil.printException(e));
                            }
                        });
                        
                        // package files
                        for (File file : mediaFilesToZip) {
                            //new zip entry and copying inputstream with file to zipOutputStream, after all closing streams
                            zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
                            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                                IOUtils.copy(fileInputStream, zipOutputStream);
                            }
                            zipOutputStream.closeEntry();
                        }
                        
                        if( excelFile.exists() )
                            excelFile.delete();
                    }
                });
    }
    
    /**
     * Trả media file đã xử lý cho client cần download.
     *
     * @param requestBody
     * @return vsat-media-files.zip chứa 01 file Excel chứa thông tin các bản tin media cần tải và (n) file media cần tải.
     */
    @RequestMapping(value = "/download-media-files-relation", method = RequestMethod.POST, produces = "application/zip")
    public ResponseEntity<StreamingResponseBody> downloadMediaFilesRelation(
            @RequestBody(required = true) Map<String, Object> requestBody) {
        if (requestBody == null || requestBody.isEmpty())
            return ResponseEntity.badRequest().body((StreamingResponseBody) (OutputStream out) -> {
                throw new ValidationException("PayLoad body is missing!");
            });
        
        List<MediaRawRelationDTO> mediaFilesInput;
        try {
            String dataLstStr = JSONConverter.toJSON(requestBody.get("mediaFiles"));
            if( StringUtil.isNullOrEmpty(dataLstStr) ) {
                String msgErr = "PayLoad body is invalid!";
                return ResponseEntity.badRequest().body((StreamingResponseBody) (OutputStream out) -> {
                    LOGGER.error(msgErr);
                    throw new ValidationException(msgErr);
                });
            }
            mediaFilesInput = new ObjectMapper().readValue(dataLstStr, new TypeReference<List<MediaRawRelationDTO>>(){});
        } catch (Exception e) {
            LOGGER.error(StringUtil.printException(e));
            return ResponseEntity.badRequest().body((StreamingResponseBody) (OutputStream out) -> {
                throw new ValidationException("PayLoad body is invalid!");
            });
        }
        
        LOGGER.info("==> mediaFilesInput.size: {}", mediaFilesInput!=null && !mediaFilesInput.isEmpty() ? mediaFilesInput.size() : 0);
        if( mediaFilesInput==null || mediaFilesInput.isEmpty() )
            return ResponseEntity.badRequest().body((StreamingResponseBody) (OutputStream out) -> {
                throw new ValidationException("PayLoad body is invalid!");
            });
        
        List<String> mediaUuidLst = new ArrayList<>();
        Set<Long> mediaPartNameLst = new HashSet<>();
        mediaFilesInput.forEach((media) -> {
            mediaUuidLst.add(media.getUuidKeyFrom());
            mediaUuidLst.add(media.getUuidKeyTo());
            mediaPartNameLst.add(media.getPartNameFrom());
        });
        
        RequestMessage userRpcRequest = new RequestMessage();
        userRpcRequest.setRequestMethod("POST");
        userRpcRequest.setRequestPath("/v1.0/media/list-by-relation-id");
        Map<String, Object> map = new HashMap<>();
        map.put("uuidLst", mediaUuidLst);
        map.put("partNameLst", mediaPartNameLst);
        userRpcRequest.setBodyParam(map);
        LOGGER.info("==> Rpc request: {}", userRpcRequest.toJsonString());
        String result = rabbitMQClient.callRpcService(RabbitMQProperties.MEDIA_RPC_EXCHANGE,
                        RabbitMQProperties.MEDIA_RPC_QUEUE, RabbitMQProperties.MEDIA_RPC_KEY, userRpcRequest.toJsonString());
        LOGGER.info("<== Rpc response : {}", result);
        
        if( StringUtil.isNullOrEmpty(result) )
            return ResponseEntity.badRequest().body((StreamingResponseBody) (OutputStream out) -> {
                LOGGER.info("Rpc response is null");
                throw new ValidationException("Internal server error");
            });
        
        ResponseMessage response = null;
        try {
            response = new ObjectMapper().readValue(result, ResponseMessage.class);
        } catch (Exception ex) {
            LOGGER.info("Json parse err: " + StringUtil.printException(ex));
        }
        if( response == null || response.getStatus() != HttpStatus.OK.value()
            || response.getData()==null || response.getData().getData()==null )
            return ResponseEntity.badRequest().body((StreamingResponseBody) (OutputStream out) -> {
                LOGGER.info("Rpc response is invalid");
                throw new ValidationException("Internal server error");
            });
        
        List<MediaRelationDTO> mediaLstReceive = null;
        try {
            mediaLstReceive = new ObjectMapper().readValue(JSONConverter.toJSON(response.getData().getData())
                                , new TypeReference<List<MediaRelationDTO>>(){});
        } catch (Exception e) {
            LOGGER.error(StringUtil.printException(e));
        }
        
        if( mediaLstReceive!=null && !mediaLstReceive.isEmpty() ) {
            for( MediaRawRelationDTO mediaMain : mediaFilesInput ) {
                for( int i = mediaLstReceive.size() - 1; i >= 0; i-- ) {
                    if( mediaLstReceive.get(i).getUuidKey().equals(mediaMain.getUuidKeyFrom()) ) {
                        mediaMain.setSrcIdFrom(mediaLstReceive.get(i).getSrcId());
                        mediaMain.setSrcNameFrom(mediaLstReceive.get(i).getSrcName());
                        mediaMain.setSourcePortFrom(mediaLstReceive.get(i).getSourcePort());
                        mediaMain.setSourcePhoneFrom(mediaLstReceive.get(i).getSourcePhone());
                        mediaMain.setDestIdFrom(mediaLstReceive.get(i).getDestId());
                        mediaMain.setDestNameFrom(mediaLstReceive.get(i).getDestName());
                        mediaMain.setDestPortFrom(mediaLstReceive.get(i).getDestPort());
                        mediaMain.setDestPhoneFrom(mediaLstReceive.get(i).getDestPhone());
                        mediaMain.setFileNameFrom(mediaLstReceive.get(i).getFileName());
                        mediaMain.setFilePathLocalFrom(mediaLstReceive.get(i).getFilePathLocal());
                        mediaLstReceive.remove(i);
                    }else if( mediaLstReceive.get(i).getUuidKey().equals(mediaMain.getUuidKeyTo()) ) {
                        mediaMain.setSrcIdTo(mediaLstReceive.get(i).getSrcId());
                        mediaMain.setSrcNameTo(mediaLstReceive.get(i).getSrcName());
                        mediaMain.setSourcePortTo(mediaLstReceive.get(i).getSourcePort());
                        mediaMain.setSourcePhoneTo(mediaLstReceive.get(i).getSourcePhone());
                        mediaMain.setDestIdTo(mediaLstReceive.get(i).getDestId());
                        mediaMain.setDestNameTo(mediaLstReceive.get(i).getDestName());
                        mediaMain.setDestPortTo(mediaLstReceive.get(i).getDestPort());
                        mediaMain.setDestPhoneTo(mediaLstReceive.get(i).getDestPhone());
                        mediaMain.setFileNameTo(mediaLstReceive.get(i).getFileName());
                        mediaMain.setFilePathLocalTo(mediaLstReceive.get(i).getFilePathLocal());
                        mediaLstReceive.remove(i);
                    }
                }
            }
        }
        
        String zipFileName = "vsat-media-files-processed.zip";
        ContentDisposition contentDisposition = ContentDisposition.builder("inline").filename(zipFileName).build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(contentDisposition);

        return ResponseEntity.ok().headers(headers).cacheControl(CacheControl.maxAge(30, TimeUnit.MINUTES))
                .contentType(MediaType.parseMediaType("application/zip"))
                .body(out -> {
                    try (ZipOutputStream zipOutputStream = new ZipOutputStream(out)) {
                        List<File> mediaFilesToZip = new ArrayList<>();
                        
                        // Tạo file excel
                        File excelFile = new File("vsat-media-raws-processed.xlsx");
                        ExcelService.writeMediaRawRelationToExcel(mediaFilesInput, excelFile);
                        
                        // Thêm file excel
                        if( excelFile.exists() )
                            mediaFilesToZip.add(excelFile);
                        
                        // Thêm các file media
                        mediaFilesInput.forEach((media) -> {
                            try {
                                File fileFrom = new File(media.getFilePathLocalFrom());
                                if( fileFrom.exists() )
                                    mediaFilesToZip.add(fileFrom);
                                
                                File fileTo = new File(media.getFilePathLocalTo());
                                if( fileTo.exists() )
                                    mediaFilesToZip.add(fileTo);
                            } catch (Exception e) {
                                LOGGER.error(StringUtil.printException(e));
                            }
                        });
                        
                        // package files
                        for (File file : mediaFilesToZip) {
                            //new zip entry and copying inputstream with file to zipOutputStream, after all closing streams
                            zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
                            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                                IOUtils.copy(fileInputStream, zipOutputStream);
                            }
                            zipOutputStream.closeEntry();
                        }
                        
                        if( excelFile.exists() )
                            excelFile.delete();
                    }
                });
    }
    
    /**
     * Trả file audio đã được merge cho client cần download.
     *
     * @param requestBody
     * @return vsat-audio-file.zip chứa 01 file audio đã được merge.
     */
    @RequestMapping(value = "/merge-and-download-audio", method = RequestMethod.POST, produces = "application/zip")
    public ResponseEntity<StreamingResponseBody> mergeAndDownloadAudio(@RequestBody(required = true)
            Map<String, Object> requestBody) {
        if (requestBody == null || requestBody.isEmpty())
            return ResponseEntity.badRequest().body((StreamingResponseBody) (OutputStream out) -> {
                throw new ValidationException("PayLoad body is missing!");
            });
        
        String filePathFrom;
        String filePathTo;
        try {
            filePathFrom = (String) requestBody.get("filePathFrom");
            filePathTo = (String) requestBody.get("filePathTo");
            if( StringUtil.isNullOrEmpty(filePathFrom) || StringUtil.isNullOrEmpty(filePathTo)) {
                String msgErr = "PayLoad body is invalid!";
                return ResponseEntity.badRequest().body((StreamingResponseBody) (OutputStream out) -> {
                    LOGGER.error(msgErr);
                    throw new ValidationException(msgErr);
                });
            }
        } catch (Exception e) {
            LOGGER.error(StringUtil.printException(e));
            return ResponseEntity.badRequest().body((StreamingResponseBody) (OutputStream out) -> {
                throw new ValidationException("PayLoad body is invalid!");
            });
        }
        
        String zipFileName = "vsat-audio-file.zip";
        ContentDisposition contentDisposition = ContentDisposition.builder("inline").filename(zipFileName).build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(contentDisposition);

        return ResponseEntity.ok().headers(headers).cacheControl(CacheControl.maxAge(30, TimeUnit.MINUTES))
                .contentType(MediaType.parseMediaType("application/zip"))
                .body(out -> {
                    try (ZipOutputStream zipOutputStream = new ZipOutputStream(out)) {
                        File fileMerged = ShellUtils.mergeAudio(filePathFrom, filePathTo);
                        if( fileMerged == null || !fileMerged.exists() ) // Nếu không merge được file thì trả file audio thứ nhất.
                            fileMerged = new File(filePathFrom);
                        
                        //new zip entry and copying inputstream with file to zipOutputStream, after all closing streams
                        zipOutputStream.putNextEntry(new ZipEntry(fileMerged.getName()));
                        try (FileInputStream fileInputStream = new FileInputStream(fileMerged)) {
                            IOUtils.copy(fileInputStream, zipOutputStream);
                        }
                        zipOutputStream.closeEntry();
//                            if( fileMerged.exists() )
//                                fileMerged.delete();
                    }
                });
    }
}
