package com.elcom.media.repository.media;

import com.elcom.media.model.media.MediaRelation;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
@Qualifier("vsatEntityManagerFactory")
public interface MediaRelationCusRepository extends PagingAndSortingRepository<MediaRelation, UUID> {

}
