package com.group3.MockProject.mapper;

import com.group3.MockProject.dto.request.CreateInterviewRequest;
import com.group3.MockProject.dto.request.QuestionRequest;
import com.group3.MockProject.dto.response.InterviewResponse;
import com.group3.MockProject.entity.*;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

/**
 * InterviewMapper
 * Mapper component for converting between Interview DTOs and Entities.
 *
 * <p>
 * Responsibilities:
 * - Maps request DTOs to entity objects
 * - Converts trust level strings to numeric values
 * - Builds response DTOs from entities
 * - Handles interviewee name extraction by type
 * </p>
 *
 * <p>
 * Trust Level Conversion:
 * - "a" (high trust) → 1.0
 * - "b" (medium trust) → 0.7
 * - "c" (low trust) → 0.4
 * </p>
 *
 * <p>
 * Version 1.0
 * Date: 7/10/2025
 * </p>
 *
 * Modification Logs:
 * DATE          AUTHOR       DESCRIPTION
 * -------------------------------------
 * 10/7/2025     FongFox      Create complete mapper
 * 11/7/2025     FongFox      Fix and organize code
 * 12/7/2025     FongFox      Fix and organize code (2)
 */
@Component
public class InterviewMapper {
    /**
     * Creates Interview entity from request data
     * Maps timestamp conversion and sets appropriate interviewee reference
     */
    public Interview createInterviewEntity(
            CreateInterviewRequest request, Case caseEntity, User interviewer, Object interviewee
    ) {
        Interview interview = new Interview();
        interview.setStartTime(request.getStartTime().atOffset(ZoneOffset.UTC).toLocalDateTime());
        interview.setEndTime(request.getEndTime().atOffset(ZoneOffset.UTC).toLocalDateTime());
        interview.setLocation(request.getLocation());
        interview.setTypeInterviewee(request.getIntervieweeType());
        interview.setUserInterviewer(interviewer);
        interview.setCaseInterview(caseEntity);

        // Set appropriate interviewee reference based on type
        if (interviewee instanceof Suspect) {
            interview.setSuspectInterviewee((Suspect) interviewee);
        } else if (interviewee instanceof Victim) {
            interview.setVictimInterviewee((Victim) interviewee);
        } else if (interviewee instanceof Witness) {
            interview.setWitnessInterviewee((Witness) interviewee);
        }

        return interview;
    }

    /**
     * Creates list of Question entities from request data
     * Converts trust levels and links questions to interview and user
     */
    public List<Question> createQuestions(
            List<QuestionRequest> questionRequests, Interview interview, User interviewer
    ) {
        List<Question> questions = new ArrayList<>();

        for (QuestionRequest qr : questionRequests) {
            Question question = new Question();
            question.setContent(qr.getQuestion());
            question.setAnswer(qr.getAnswer());
            question.setReliability(convertLevelOfTrust(qr.getLevelOfTrust()));
            question.setInterview(interview);
            question.setUser(interviewer);
            questions.add(question);
        }

        return questions;
    }

    /**
     * Converts trust level from string to float value
     * "a" -> 1.0 (high trust), "b" -> 0.7 (medium trust), "c" -> 0.4 (low trust)
     */
    public Float convertLevelOfTrust(String levelOfTrust) {
        switch (levelOfTrust.toLowerCase()) {
            case "a": return 1.0f;
            case "b": return 0.7f;
            case "c": return 0.4f;
            default: return 0.0f; // Default value for invalid input
        }
    }

    /**
     * Builds InterviewResponse DTO from interview entity and related data
     * Includes interviewer/interviewee names, question count, and file list
     */
    public InterviewResponse buildInterviewResponse(
            Interview interview, User interviewer, Object interviewee, int totalQuestions, List<String> attachedFiles
    ) {
        return InterviewResponse.builder()
                .startTime(interview.getStartTime())
                .endTime(interview.getEndTime())
                .location(interview.getLocation())
                .interviewerName(interviewer.getFullName())
                .intervieweeType(interview.getTypeInterviewee())
                .intervieweeName(getIntervieweeName(interviewee))
                .totalQuestions(totalQuestions)
                .attachedFiles(attachedFiles)
                .createdAt(interview.getCreateAt())
                .build();
    }

    /**
     * Extracts interviewee name based on entity type
     * Handles different name field patterns across Suspect, Victim, and Witness entities
     */
    public String getIntervieweeName(Object interviewee) {
        if (interviewee instanceof Suspect) {
            return ((Suspect) interviewee).getFullname();
        } else if (interviewee instanceof Victim) {
            return ((Victim) interviewee).getFullname();
        } else if (interviewee instanceof Witness) {
            return ((Witness) interviewee).getFullName();
        }
        return "Unknown";
    }
}