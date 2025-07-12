package com.group3.MockProject.elasticsearch.service;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import com.group3.MockProject.constant.CaseType;
import com.group3.MockProject.constant.SeverityType;
import com.group3.MockProject.elasticsearch.document.EsCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class CaseIndexService {

    private final ElasticsearchTemplate elasticsearchTemplate;

    public SearchHits<EsCase> searchCases(String keyword, int page, int size, SeverityType severityType, CaseType caseType, LocalDateTime date) {
        PageRequest pageable = PageRequest.of(page, size);

        NativeQuery query;

        BoolQuery.Builder boolQueryBuilder = QueryBuilders.bool();

        if (keyword != null && !keyword.isBlank()) {
            boolQueryBuilder
                    .should(s -> s.match(m -> m.field("case_id").query(keyword).fuzziness("AUTO")))
                    .should(s -> s.match(m -> m.field("case_name").query(keyword).fuzziness("AUTO")))
                    .should(s -> s.match(m -> m.field("severity_label").query(keyword).fuzziness("AUTO")))
                    .should(s -> s.match(m -> m.field("type_case_label").query(keyword).fuzziness("AUTO")))
                    .should(s -> s.match(m -> m.field("status_label").query(keyword).fuzziness("AUTO")))
                    .should(s -> s.match(m -> m.field("reporter_fullname").query(keyword).fuzziness("AUTO")))
                    .should(s -> s.match(m -> m.field("case_location").query(keyword).fuzziness("AUTO")))
                    .minimumShouldMatch("1");
        } else {
            boolQueryBuilder.must(m -> m.matchAll(ma -> ma));
        }

        if (severityType != null) {
            boolQueryBuilder.filter(f -> f.term(t -> t.field("severity_key").value(severityType.name())));
        }

        if (caseType != null) {
            boolQueryBuilder.filter(f -> f.term(t -> t.field("type_case_key").value(caseType.name())));
        }

        if (date != null) {
            long startOfDayMillis = date.toInstant(ZoneOffset.UTC).toEpochMilli();
            long endOfDayMillis = date.plusDays(1).minusNanos(1).toInstant(ZoneOffset.UTC).toEpochMilli();

            boolQueryBuilder.filter(f -> f
                    .range(r -> r
                            .date(dr -> dr
                                    .field("create_at")
                                    .gte(String.valueOf(startOfDayMillis))
                                    .lte(String.valueOf(endOfDayMillis))
                            )
                    )
            );
        }

        query = NativeQuery.builder()
                .withQuery(q -> q.bool(boolQueryBuilder.build()))
                .withPageable(pageable)
                .build();

        return elasticsearchTemplate.search(query, EsCase.class);
    }

}
