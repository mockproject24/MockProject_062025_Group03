package com.group3.MockProject.service.impl;

import com.group3.MockProject.dto.request.CreateInterviewDto;
import com.group3.MockProject.dto.request.QuestionDto;
import com.group3.MockProject.entity.*;
import com.group3.MockProject.repository.InterviewRepository;
import com.group3.MockProject.repository.UserRepository;
import com.group3.MockProject.repository.VictimRepository;
import com.group3.MockProject.repository.WitnessRepository;
// TODO: Uncomment when VictimInterview entity is properly implemented
// import com.group3.MockProject.repository.VictimInterviewRepository;
// TODO: Uncomment when WitnessInterview entity is properly implemented
// import com.group3.MockProject.repository.WitnessInterviewRepository;
import com.group3.MockProject.service.InterviewService;
import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * InterviewServiceImpl
 * <p>
 * Provides business logic for managing employment details.
 * <p>
 * Version 1.0
 * Date: 7/4/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 7/4/2025      User      Create
 */
@Service
public class InterviewServiceImpl implements InterviewService {
    private final InterviewRepository interviewRepository;
    private final UserRepository userRepository;
    private final VictimRepository victimRepository;
    private final WitnessRepository witnessRepository;
    // TODO: Uncomment when VictimInterview entity is properly implemented
    // private final VictimInterviewRepository victimInterviewRepository;
    // TODO: Uncomment when WitnessInterview entity is properly implemented
    // private final WitnessInterviewRepository witnessInterviewRepository;
    private final Path uploadRoot = Paths.get("uploads");

    public InterviewServiceImpl(
            InterviewRepository interviewRepository,
            UserRepository userRepository,
            VictimRepository victimRepository,
            WitnessRepository witnessRepository) {
            // TODO: Add VictimInterviewRepository and WitnessInterviewRepository parameters when entities are available
        this.interviewRepository = interviewRepository;
        this.userRepository = userRepository;
        this.victimRepository = victimRepository;
        this.witnessRepository = witnessRepository;
        // TODO: Uncomment when VictimInterview entity is properly implemented
        // this.victimInterviewRepository = victimInterviewRepository;
        // TODO: Uncomment when WitnessInterview entity is properly implemented
        // this.witnessInterviewRepository = witnessInterviewRepository;
    }

    @Override
    public void createInterview(
            String caseId,
            String suspectId,
            CreateInterviewDto createInterviewDto,
            List<MultipartFile> files) throws BadRequestException {
        if (createInterviewDto.getEndTime().isBefore(createInterviewDto.getStartTime())) {
            throw new BadRequestException("endTime must be after startTime");
        }

        if (createInterviewDto.getQuesAndAns() == null || createInterviewDto.getQuesAndAns().isEmpty()) {
            throw new BadRequestException("Must have at least one question");
        }

        Interview interview = new Interview();
        interview.setInterviewId(UUID.randomUUID().toString());
        interview.setStartTime(LocalDateTime.from(createInterviewDto.getStartTime()));
        interview.setEndTime(LocalDateTime.from(createInterviewDto.getEndTime()));
        interview.setLocation(createInterviewDto.getLocation());

        // Map & set questions
        List<Question> questionList = new ArrayList<>();
        for (QuestionDto questionDto : createInterviewDto.getQuesAndAns()) {
            Question question = getQuestion(questionDto);
            questionList.add(question);
        }
        interview.setQuestions(questionList);
        for (Question question : questionList) {
            question.setInterview(interview);
        }

        // Find interview in DB by interview full name (throw exception if not found)
        User interviewer = userRepository
                .findByFullName(createInterviewDto.getInterviewer())
                .orElseThrow(() -> new EntityNotFoundException("Interviewer not found with " + createInterviewDto.getInterviewer()));
        interview.setInterviewer(interviewer);
        interview.setTypeInterviewee(createInterviewDto.getIntervieweeType());

        //Todo Case 1: Interview witness
        if (createInterviewDto.getIntervieweeType().equalsIgnoreCase("witness")) {
//            Witness witness = witnessRepository
//                    .findWitnessByFullname(createInterviewDto.getInterviewee())
//                    .orElseThrow(() -> new EntityNotFoundException("Interviewee not found!"));
//
//            if (witnessInterviewRepository.existsByInterviewAndWitness(interview, witness)) {
//                throw new DataIntegrityViolationException("Relationship between interview and witness already existed!");
//            }
//
//            WitnessInterview witnessInterview = new WitnessInterview();
//            witnessInterview.setInterview(interview);
//            witnessInterview.setWitness(witness);
//
//            if(interview.getWitnessesInterviews() == null) {
//                interview.setWitnessesInterviews(new ArrayList<>());
//            }
//            interview.getWitnessesInterviews().add(witnessInterview);
            // TODO: Implement witness interview when WitnessInterview entity is available
        }

        //Todo case 2: Interview victim
        if (createInterviewDto.getIntervieweeType().equalsIgnoreCase("victim")) {
//            Victim victim = victimRepository
//                    .findVictimByFullname(createInterviewDto.getInterviewee())
//                    .orElseThrow(() -> new EntityNotFoundException("Interviewee not found!"));
//
//            if (victimInterviewRepository.existsByInterviewAndVictim(interview, victim)) {
//                throw new DataIntegrityViolationException("Relationship between interview and witness already existed!");
//            }
//
//            VictimInterview victimInterview= new VictimInterview();
//            victimInterview.setInterview(interview);
//            victimInterview.setVictim(victim);
//
//            if(interview.getVictimsInterviews() == null) {
//                interview.setVictimsInterviews(new ArrayList<>());
//            }
//            interview.getVictimsInterviews().add(victimInterview);
            // TODO: Implement victim interview when VictimInterview entity is available
        }

        //Todo case 3: Interview suspect (optional)
        //Todo handle save file

        interviewRepository.save(interview);
    }

    //Mapper to entity (for case 1)
//    private Interview toEntity(
//            CreateInterviewDto createInterviewDto,
//            List<String> filePaths,
//            User interviewer,
//            Witness witness) {
//        return new Interview();
//    }

    private Question getQuestion(QuestionDto questionDto) {
        Question question = new Question();
        question.setContent(questionDto.getQuestion());
        question.setAnswer(questionDto.getAnswer());

        // Consider reliability question and convert into float value
        if (questionDto.getLevelOfTrust().equalsIgnoreCase("a")) {
            question.setReliability(1.0f);
        } else if (questionDto.getLevelOfTrust().equalsIgnoreCase("b")) {
            question.setReliability(0.7f);
        } else {
            question.setReliability(0.4f);
        }

        return question;
    }
}
