package com.elcom.media.service.impl;

import com.elcom.gateway.message.MessageContent;
import com.elcom.media.model.dto.request.media.AddCommentRequest;
import com.elcom.media.model.dto.request.media.DeleteFileMediaRequest;
import com.elcom.media.model.dto.request.media.GetDetailMediaRelationRequest;
import com.elcom.media.model.dto.request.media.GetDetailMediaRequest;
import com.elcom.media.model.dto.request.media.GetListlMediaByIdRelationRequest;
import com.elcom.media.model.dto.request.media.GetTotalMediaByObjectRequest;
import com.elcom.media.model.dto.request.media.SearchListMediaRequest;
import com.elcom.media.model.media.Comment;
import com.elcom.media.repository.media.MediaRelationRepository;
import com.elcom.media.repository.media.MediaRepository;
import com.elcom.media.service.MediaService;
import com.elcom.media.utils.MediaUtils;
import com.elcom.media.utils.StringUtil;
import com.elcom.vsat.model.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MediaServiceImpl implements MediaService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(MediaServiceImpl.class);

    @Autowired
    private MediaRepository mediaRepository;
    @Autowired
    private MediaRelationRepository mediaRelationRepository;
    
//    @Autowired
//    private VsatMediaRepository vsatMediaRepository;
    
//    @Resource
//    @Autowired
//    private MediaMapper mediaMapper;

    @Override
    public List<MediaType> getListMediaTypes() {
        return mediaRepository.getListMediaTypes();
    }
    
    @Override
    public List<Comment> getListComment(String refId, Integer commentTypeId) {
        return mediaRepository.getListComment(refId, commentTypeId);
    }

    @Override
    public MessageContent search(SearchListMediaRequest input){
        try {
            if( input.getCurrentPage() > 0 )
                input.setCurrentPage(input.getCurrentPage() - 1);
            return this.mediaRepository.searchMedia(input);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(StringUtil.printException(e));
        }
        return null;
    }
    
    @Override
    public MessageContent searchMediaRelation(SearchListMediaRequest input) {
        try {
            if( input.getCurrentPage() > 0 )
                input.setCurrentPage(input.getCurrentPage() - 1);
            return this.mediaRelationRepository.searchMediaRelation(input);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(StringUtil.printException(e));
        }
        return null;
    }

    @Override
    public MessageContent findTotalMediaByObjectId(GetTotalMediaByObjectRequest input) {
        try {
            return this.mediaRepository.findTotalMediaByObjectId(input);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(StringUtil.printException(e));
        }
        return null;
    }
    
    @Override
    public MessageContent getDetailMediaRelation(GetDetailMediaRelationRequest data) {
        return this.mediaRepository.getDetailMediaRelation(data);
    }
    
    @Override
    public MessageContent getListMediaByRelationId(GetListlMediaByIdRelationRequest data) {
        return this.mediaRepository.getListMediaByRelationId(data);
    }
    
    @Override
    public String getM3U8File(GetDetailMediaRequest req) {
        return MediaUtils.generateM3u8ByTs(req.getFilePathLocal());
    }
    
    @Override
    public boolean updateReadStatus(List<String> uuidKeys, String updateType){
        return mediaRepository.updateReadStatus(uuidKeys, updateType);
    }

    @Override
    public boolean addCommentCommon(AddCommentRequest addCommentRequest){
        return mediaRepository.addCommentCommon(addCommentRequest);
    }

    @Override
    public void setMediaRelation(){
        try {
            mediaRelationRepository.setMediaRelation();
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(StringUtil.printException(e));
        }
    }

    @Override
    public Boolean deleteFileMedia(DeleteFileMediaRequest deleteFileMediaRequest){
        try {
            return mediaRepository.deleteFileMedia(deleteFileMediaRequest);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(StringUtil.printException(e));
        }
        return null;
    }
}
