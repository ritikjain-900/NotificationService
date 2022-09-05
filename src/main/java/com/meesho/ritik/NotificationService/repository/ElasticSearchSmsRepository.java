package com.meesho.ritik.NotificationService.repository;

import com.meesho.ritik.NotificationService.model.elasticsearch.ElasticSearchSmsDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElasticSearchSmsRepository extends ElasticsearchRepository<ElasticSearchSmsDocument, String> {

    Page<ElasticSearchSmsDocument> findAllByMessage(String message, Pageable pageable);

    Page<ElasticSearchSmsDocument> findAllByCreatedAtBetween(long startEpoch, long endEpoch, Pageable pageable);

    Page<ElasticSearchSmsDocument> findAll();
}
