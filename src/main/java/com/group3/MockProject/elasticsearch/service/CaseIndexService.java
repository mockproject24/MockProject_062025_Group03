package com.group3.MockProject.elasticsearch.service;

import com.group3.MockProject.elasticsearch.document.EsCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CaseIndexService {

    private final ElasticsearchTemplate elasticsearchTemplate;

    public SearchHits<EsCase> searchCases(String keyword, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);

        NativeQuery query;

        if (keyword == null || keyword.isBlank()) {
            query = NativeQuery.builder()
                    .withQuery(q -> q.matchAll(m -> m))
                    .withPageable(pageable)
                    .build();
        } else {
            query = NativeQuery.builder()
                    .withQuery(q -> q
                            .bool(b -> b
                                    .should(s -> s.match(m -> m.field("case_name").query(keyword).fuzziness("AUTO")))
                                    .should(s -> s.match(m -> m.field("severity_label").query(keyword).fuzziness("AUTO")))
                                    .should(s -> s.match(m -> m.field("type_case_label").query(keyword).fuzziness("AUTO")))
                                    .should(s -> s.match(m -> m.field("status_label").query(keyword).fuzziness("AUTO")))
                                    .should(s -> s.match(m -> m.field("reporter_fullname").query(keyword).fuzziness("AUTO")))
                                    .should(s -> s.match(m -> m.field("case_location").query(keyword).fuzziness("AUTO")))
                                    .minimumShouldMatch("1")
                            )
                    )
                    .withPageable(pageable)
                    .build();
        }

        return elasticsearchTemplate.search(query, EsCase.class);
    }


}
