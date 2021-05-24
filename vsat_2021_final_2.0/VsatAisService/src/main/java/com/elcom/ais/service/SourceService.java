package com.elcom.ais.service;

import com.elcom.ais.model.Source;
import com.elcom.ais.model.dto.request.media.DeleteFileMediaRequest;

import java.util.List;

public interface SourceService {
    List<Source> getAll();
}
