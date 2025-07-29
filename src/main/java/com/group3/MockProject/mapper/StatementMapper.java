package com.group3.MockProject.mapper;

import com.group3.MockProject.dto.response.StatementResponse;
import com.group3.MockProject.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

/**
 * StatementMapper
 * <p>
 * Mapper for converting Statement entity to DTO
 * <p>
 * Version 1.0
 * Date: 7/23/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 7/23/2025      User      Create
 */
@Component
@Slf4j
public class StatementMapper {
    /**
     * Converts Statement entity to StatementResponse DTO
     *
     * @param statement The statement entity to convert
     * @return StatementResponse DTO
     */
    public StatementResponse toStatementResponse(Statement statement) {
        log.debug("Converting statement entity to response DTO: {}", statement.getStatementId());

        return StatementResponse.builder()
                .statementId(statement.getStatementId())
                .caseId(statement.getCaseEntity().getCaseId())
                .initialName(statement.getInitialName())
                .statementDate(statement.getStatementDate() != null ?
                        statement.getStatementDate().atZone(ZoneOffset.UTC).toInstant() : null)
                .contactInformation(extractContactInformation(statement))
                .initialRoleType(statement.getInitialRoleType())
                .content(statement.getContent())
                .createdAt(statement.getCreateAt() != null ?
                        statement.getCreateAt().atZone(ZoneOffset.UTC).toInstant() : null)
                .updatedAt(statement.getUpdateAt() != null ?
                        statement.getUpdateAt().atZone(ZoneOffset.UTC).toInstant() : null)
                .personInfo(buildPersonInfo(statement))
                .evidenceLinks(buildEvidenceLinks(statement))
                .build();
    }

    /**
     * Extracts contact information based on person type
     *
     * @param statement The statement entity
     * @return Contact information string
     */
    private String extractContactInformation(Statement statement) {
        if (statement.getSuspect() != null) {
            return statement.getSuspect().getPhoneNumber();
        } else if (statement.getVictim() != null) {
            return statement.getVictim().getContact();
        } else if (statement.getWitness() != null) {
            return statement.getWitness().getContact();
        }
        return null;
    }

    /**
     * Builds PersonInfo from statement entity
     *
     * @param statement The statement entity
     * @return PersonInfo object
     */
    private StatementResponse.PersonInfo buildPersonInfo(Statement statement) {
        if (statement.getSuspect() != null) {
            Suspect suspect = statement.getSuspect();
            return StatementResponse.PersonInfo.builder()
                    .personId(suspect.getSuspectId())
                    .fullName(suspect.getFullname())
                    .type("SUSPECT")
                    .contact(suspect.getPhoneNumber())
                    .build();
        } else if (statement.getVictim() != null) {
            Victim victim = statement.getVictim();
            return StatementResponse.PersonInfo.builder()
                    .personId(victim.getVictimId())
                    .fullName(victim.getFullname())
                    .type("VICTIM")
                    .contact(victim.getContact())
                    .build();
        } else if (statement.getWitness() != null) {
            Witness witness = statement.getWitness();
            return StatementResponse.PersonInfo.builder()
                    .personId(witness.getWitnessId())
                    .fullName(witness.getFullName())
                    .type("WITNESS")
                    .contact(witness.getContact())
                    .build();
        }

        log.warn("No person information found for statement: {}", statement.getStatementId());
        return null;
    }

    /**
     * Builds evidence links from statement entity
     *
     * @param statement The statement entity
     * @return List of EvidenceLink objects
     */
    private List<StatementResponse.EvidenceLink> buildEvidenceLinks(Statement statement) {
        List<StatementResponse.EvidenceLink> evidenceLinks = new ArrayList<>();

        if (statement.getStatementEvidences() != null) {
            for (StatementEvidence se : statement.getStatementEvidences()) {
                if (!se.isDeleted() && se.getEvidence() != null && !se.getEvidence().isDeleted()) {
                    Evidence evidence = se.getEvidence();

                    StatementResponse.EvidenceLink evidenceLink = StatementResponse.EvidenceLink.builder()
                            .evidenceId(evidence.getEvidenceId())
                            .fileName(extractFileName(evidence.getAttachFile()))
                            .fileUrl(evidence.getAttachFile())
                            .evidenceType(evidence.getEvidenceType() != null ?
                                    evidence.getEvidenceType().getLabel() : null)
                            .description(evidence.getDescription())
                            .build();

                    evidenceLinks.add(evidenceLink);
                }
            }
        }

        log.debug("Built {} evidence links for statement: {}", evidenceLinks.size(), statement.getStatementId());
        return evidenceLinks;
    }

    /**
     * Extracts file name from file URL/path
     *
     * @param fileUrl The complete file URL/path
     * @return File name only
     */
    private String extractFileName(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return null;
        }

        // Extract filename from URL (e.g., "http://localhost/uploads/file.png" → "file.png")
        int lastSlashIndex = fileUrl.lastIndexOf('/');
        if (lastSlashIndex >= 0 && lastSlashIndex < fileUrl.length() - 1) {
            return fileUrl.substring(lastSlashIndex + 1);
        }

        return fileUrl; // Return full string if no slash found
    }
}
