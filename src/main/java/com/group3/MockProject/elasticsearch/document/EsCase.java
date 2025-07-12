package com.group3.MockProject.elasticsearch.document;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "cases", createIndex = false)
@Setting(shards = 1)
public class EsCase {
    @Id
    @Field(type = FieldType.Keyword, name = "case_id")
    private String caseId;

    @Field(type = FieldType.Text, name = "case_name")
    private String caseName;

    @Field(type = FieldType.Keyword, name = "severity_key")
    private String severityKey;

    @Field(type = FieldType.Text, name = "severity_label")
    private String severityLabel;

    @Field(type = FieldType.Keyword, name = "type_case_key")
    private String typeCaseKey;

    @Field(type = FieldType.Text, name = "type_case_label")
    private String typeCaseLabel;

    @Field(type = FieldType.Keyword, name = "status_key")
    private String statusKey;

    @Field(type = FieldType.Text, name = "status_label")
    private String statusLabel;

    @Field(type = FieldType.Date, name = "create_at")
    private String createAt;

    @Field(type = FieldType.Text, name = "reporter_fullname")
    private String reporterFullname;

    @Field(type = FieldType.Text, name = "case_location")
    private String caseLocation;
}