package com.elcom.contact.service;

import com.elcom.gateway.message.MessageContent;

import com.elcom.contact.model.dto.request.media.AddCommentRequest;
import com.elcom.contact.model.dto.request.media.DeleteFileMediaRequest;
import com.elcom.contact.model.dto.request.media.GetInfoByMediaIdRequest;
import com.elcom.contact.model.dto.request.media.UpdateStatusByMediaIdRequest;
import com.elcom.contact.model.media.MediaRaw;
import com.elcom.vsat.model.MediaType;
import com.elcom.vsat.model.dto.controller.request.media.SearchListMediaRequest;
import org.springframework.util.MultiValueMap;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface MediaService {
    List<MediaType> getListMediaTypes(HttpServletRequest request, MultiValueMap postInput, String token);

//    void testFindMedia(String startDate, String endDate);
//
//    void testFindAis(String startDate, String endDate);
//
//    void testInsertAis(Long ruleId, Long mmsi);
//
//    void testInsertMedia(Long ruleId, Long mmsi);

    MessageContent search(SearchListMediaRequest data);

    MediaRaw getInfoByMediaId(GetInfoByMediaIdRequest data);

    Boolean updateStatusMediaId(UpdateStatusByMediaIdRequest data);

    Boolean addCommentCommon(AddCommentRequest addCommentRequest);

    Boolean deleteFileMedia(DeleteFileMediaRequest deleteFileMediaRequest);
}
