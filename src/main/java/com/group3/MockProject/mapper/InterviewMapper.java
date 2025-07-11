package com.group3.MockProject.mapper;

import com.group3.MockProject.dto.request.CreateInterviewDto;
import com.group3.MockProject.dto.request.QuestionDto;
import com.group3.MockProject.dto.response.InterviewResponseDto;
import com.group3.MockProject.entity.Interview;
import com.group3.MockProject.entity.InterviewFile;
import com.group3.MockProject.entity.Question;
import com.group3.MockProject.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * InterviewMapper
 * <p>
 * Converts between DTOs and Entities with updated entity structure
 * <p>
 * Version 1.0
 * Date: 7/10/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 7/10/2025      FongFox      Create complete mapper
 */
@Component
public class InterviewMapper {
    /**
     * Convert CreateInterviewDto to Interview entity
     * @param dto CreateInterviewDto from client
     * @param interviewer User who conducts the interview
     * @return Interview entity (without files and questions)
     */
    public Interview convertToInterviewEntity(CreateInterviewDto dto, User interviewer) {
        Interview interview = new Interview();

        // Set basic information
        // FIXED: Convert Instant to LocalDateTime properly
        interview.setStartTime(dto.getStartTime().atZone(ZoneOffset.UTC).toLocalDateTime());
        interview.setEndTime(dto.getEndTime().atZone(ZoneOffset.UTC).toLocalDateTime());
        interview.setLocation(dto.getLocation());
        interview.setTypeInterviewee(dto.getIntervieweeType());
        interview.setUserInterviewer(interviewer);
        interview.setDeleted(false);
        // createAt and updateAt will be set automatically by @CreationTimestamp and @UpdateTimestamp

        return interview;
    }

    /**
     * Convert uploaded files to InterviewFile entities
     * @param filePaths List of uploaded file paths
     * @param interview Interview entity
     * @return List of InterviewFile entities
     */
    public List<InterviewFile> convertToInterviewFileEntities(List<String> filePaths, Interview interview) {
        List<InterviewFile> interviewFiles = new ArrayList<>();

        if (filePaths != null && !filePaths.isEmpty()) {
            for (String filePath : filePaths) {
                InterviewFile interviewFile = new InterviewFile();
                interviewFile.setAttachedFile(filePath);
                interviewFile.setInterview(interview);
                interviewFile.setDeleted(false);
                // createAt will be set automatically by @CreationTimestamp

                interviewFiles.add(interviewFile);
            }
        }

        return interviewFiles;
    }

    /**
     * Convert list of QuestionDto to list of Question entities
     * @param questionDtos List of QuestionDto from client
     * @param interview Interview entity that was created
     * @param user User who created the questions
     * @return List of Question entities
     */
    public List<Question> convertToQuestionEntities(List<QuestionDto> questionDtos, Interview interview, User user) {
        List<Question> questions = new ArrayList<>();

        if (questionDtos != null && !questionDtos.isEmpty()) {
            // Convert each question
            for (QuestionDto questionDto : questionDtos) {
                Question question = convertToQuestionEntity(questionDto, interview, user);
                questions.add(question);
            }
        }

        return questions;
    }

    /**
     * Convert QuestionDto to Question entity
     * @param dto QuestionDto from client
     * @param interview Interview entity
     * @param user User who created the question
     * @return Question entity
     */
    private Question convertToQuestionEntity(QuestionDto dto, Interview interview, User user) {
        Question question = new Question();

//        question.setQuestionId(UUID.randomUUID().toString());
        question.setContent(dto.getQuestion());
        question.setAnswer(dto.getAnswer());
        question.setReliability(convertLevelOfTrustToFloat(dto.getLevelOfTrust()));
        question.setInterview(interview);
        question.setUser(user);
        question.setDeleted(false);
        // createAt and updateAt will be set automatically by timestamps

        return question;
    }

    /**
     * Convert Interview entity to Response DTO
     * @param interview Interview entity that was saved
     * @return InterviewResponseDto
     */
    public InterviewResponseDto convertToResponseDto(Interview interview) {
        // Get interviewee name based on type
        String intervieweeName = getIntervieweeName(interview);

        // Get attached files list from InterviewFile entities
        List<String> attachedFiles = getAttachedFilesFromEntities(interview);

        // Build response DTO
        return InterviewResponseDto.builder()
                .startTime(interview.getStartTime())
                .endTime(interview.getEndTime())
                .location(interview.getLocation())
                .interviewerName(interview.getUserInterviewer().getFullName())
                .intervieweeType(interview.getTypeInterviewee())
                .intervieweeName(intervieweeName)
                .totalQuestions(interview.getQuestions() != null ? interview.getQuestions().size() : 0)
                .attachedFiles(attachedFiles)
                .createdAt(interview.getCreateAt()) // Use createAt from entity
                .build();
    }

    /**
     * Convert level of trust string to float value
     * @param levelOfTrust "a", "b", "c" (case insensitive)
     * @return Float value (1.0, 0.7, 0.4)
     */
    private Float convertLevelOfTrustToFloat(String levelOfTrust) {
        if (levelOfTrust == null || levelOfTrust.trim().isEmpty()) {
            return 0.4f; // Default value for invalid input
        }

        String level = levelOfTrust.toLowerCase().trim();
        switch (level) {
            case "a":
                return 1.0f; // High trust
            case "b":
                return 0.7f; // Medium trust
            case "c":
                return 0.4f; // Low trust
            default:
                return 0.4f; // Default for invalid values
        }
    }

    /**
     * Get interviewee name based on type
     * @param interview Interview entity
     * @return Interviewee full name
     */
    private String getIntervieweeName(Interview interview) {
        String typeInterviewee = interview.getTypeInterviewee();

        if (typeInterviewee == null || typeInterviewee.trim().isEmpty()) {
            return "Unknown";
        }

        String type = typeInterviewee.toUpperCase();
        switch (type) {
            case "SUSPECT":
                if (interview.getSuspectInterviewee() != null) {
                    return interview.getSuspectInterviewee().getFullname();
                }
                break;
            case "VICTIM":
                if (interview.getVictimInterviewee() != null) {
                    return interview.getVictimInterviewee().getFullname();
                }
                break;
            case "WITNESS":
                if (interview.getWitnessInterviewee() != null) {
                    return interview.getWitnessInterviewee().getFullName();
                }
                break;
            default:
                return "Unknown";
        }

        return "Unknown";
    }

    /**
     * Get attached files list from InterviewFile entities
     * @param interview Interview entity
     * @return List of file names
     */
    private List<String> getAttachedFilesFromEntities(Interview interview) {
        List<String> fileNames = new ArrayList<>();

        if (interview.getInterviewFileList() != null && !interview.getInterviewFileList().isEmpty()) {
            for (InterviewFile interviewFile : interview.getInterviewFileList()) {
                if (!interviewFile.isDeleted() && interviewFile.getAttachedFile() != null) {
                    fileNames.add(interviewFile.getAttachedFile());
                }
            }
        }

        return fileNames;
    }
}