package com.elcom.media.service;

import com.elcom.gateway.message.MessageContent;
import com.elcom.media.model.dto.request.media.AddCommentRequest;
import com.elcom.media.model.dto.request.media.DeleteFileMediaRequest;
import com.elcom.media.model.dto.request.media.GetDetailMediaRelationRequest;
import com.elcom.media.model.dto.request.media.GetDetailMediaRequest;
import com.elcom.media.model.dto.request.media.GetListlMediaByIdRelationRequest;
import com.elcom.media.model.dto.request.media.GetTotalMediaByObjectRequest;
import com.elcom.media.model.dto.request.media.SearchListMediaRequest;
import com.elcom.media.model.media.Comment;
import com.elcom.vsat.model.MediaType;
import java.util.List;

public interface MediaService {
    
    List<MediaType> getListMediaTypes();
    
    List<Comment> getListComment(String refId, Integer commentTypeId);

    MessageContent search(SearchListMediaRequest data);
    
    MessageContent searchMediaRelation(SearchListMediaRequest data);

    MessageContent findTotalMediaByObjectId(GetTotalMediaByObjectRequest data);
    
    MessageContent getDetailMediaRelation(GetDetailMediaRelationRequest data);
    
    MessageContent getListMediaByRelationId(GetListlMediaByIdRelationRequest data);
    
    String getM3U8File(GetDetailMediaRequest data);

    boolean updateReadStatus(List<String> uuidKeys, String updateType);

    boolean addCommentCommon(AddCommentRequest addCommentRequest);

    void setMediaRelation();

    Boolean deleteFileMedia(DeleteFileMediaRequest deleteFileMediaRequest);
}
