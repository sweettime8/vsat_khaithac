package com.elcom.rule.service;

import com.elcom.rule.model.Source;
import com.elcom.rule.model.dto.request.media.DeleteFileMediaRequest;

import java.util.List;

public interface SourceService {
    List<Source> getAll();
}
