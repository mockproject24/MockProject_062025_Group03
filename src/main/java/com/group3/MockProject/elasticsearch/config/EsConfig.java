package com.group3.MockProject.elasticsearch.config;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.ExistsRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
@RequiredArgsConstructor
@Slf4j
public class EsConfig {

    private final ElasticsearchClient elasticsearchClient;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        this.createIndexIfNotExists("cases", "es/case-index.json");
    }

    private void createIndexIfNotExists(String indexName, String mappingFilePath) {
        try {
            boolean indexExists = false;
            try {
                elasticsearchClient.indices().get(b -> b.index(indexName));
                indexExists = true;
            } catch (co.elastic.clients.elasticsearch._types.ElasticsearchException e) {
                if (e.status() == 404) {
                    indexExists = false;
                } else {
                    throw e;
                }
            }

            if (!indexExists) {
                log.info("Index '{}' does not exist. Creating...", indexName);
                try (InputStream mappingInputStream = new ClassPathResource(mappingFilePath).getInputStream()) {
                    CreateIndexRequest createIndexRequest = CreateIndexRequest.of(b -> b
                            .index(indexName)
                            .withJson(mappingInputStream)
                    );
                    elasticsearchClient.indices().create(createIndexRequest);
                    log.info("Successfully created Elasticsearch index: {}", indexName);
                }
            } else {
                log.info("Elasticsearch index '{}' already exists.", indexName);
            }

        } catch (Exception e) {
            log.error("Failed to create or check Elasticsearch index: {}", indexName, e);
        }
    }

}
