package com.group3.MockProject.service;

import com.group3.MockProject.dto.ResponseDto;
import com.group3.MockProject.dto.request.SaveInitialResponseDto;
import com.group3.MockProject.entity.*;
import com.group3.MockProject.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * CaseInitialService
 * <p>
 * Provides business logic for save case's initial response.
 * <p>
 * Version 1.0
 * Date: 04-Jul-25
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 04-Jul-25      Hoang Tran      Create
 */
@Service
@RequiredArgsConstructor
public class CaseInitialService {

    private final CaseInitialRepository caseInitialRepository;
    private final SceneResponseRepository sceneResponseRepository;
    private final ScenePreservationRepository scenePreservationRepository;
    private final MedicalSupportRepository medicalSupportRepository;
    private final UsersCasesRepository usersCasesRepository;
    private final UserRepository userRepository;

    /**
     * Save initial response information for a case
     *
     * @param caseId  the case ID
     * @param request the initial response data
     * @return ResponseDto with success or error message
     */
    @Transactional
    public ResponseDto<Void> saveInitialResponse(String caseId, SaveInitialResponseDto request) {
        try {
            // Check if case exists
            Optional<Case> caseOptional = caseInitialRepository.findByCaseIdAndIsDeletedFalse(caseId);
            if (caseOptional.isEmpty()) {
                return ResponseDto.error("Case not found", 404);
            }
            Case caseEntity = caseOptional.get();

            // Create SceneResponse
            SceneResponse sceneResponse = new SceneResponse();
            sceneResponse.setCaseEntity(caseEntity);
            sceneResponse.setDispatchTime(request.getDispatchTime());
            sceneResponse.setArrivalTime(request.getArrivalTime());
            sceneResponse.setPreliminaryAssessment(request.getPreliminaryAssessment());
            sceneResponse.setDeleted(false);

            SceneResponse savedSceneResponse = sceneResponseRepository.save(sceneResponse);

            // Save officers (UsersCases)
            if (request.getOfficers() != null) {
                for (SaveInitialResponseDto.OfficerDto officerDto : request.getOfficers()) {
                    Optional<User> userOptional = userRepository.findByUsernameAndIsDeletedFalse(officerDto.getOfficerId());
                    if (userOptional.isPresent()) {
                        UsersCasesId usersCasesId = new UsersCasesId();
                        usersCasesId.setUserId(officerDto.getOfficerId());
                        usersCasesId.setCaseId(caseId);

                        UsersCases usersCases = new UsersCases();
                        usersCases.setId(usersCasesId);
                        usersCases.setUser(userOptional.get());
                        usersCases.setCaseEntity(caseEntity);
                        usersCases.setResponsible(officerDto.getResponsible());
                        usersCases.setDeleted(false);

                        usersCasesRepository.save(usersCases);
                    }
                }
            }

            // Save preservation measures
            if (request.getPreservationMeasures() != null) {
                for (SaveInitialResponseDto.PreservationMeasureDto measureDto : request.getPreservationMeasures()) {
                    ScenePreservation preservation = new ScenePreservation();
                    preservation.setSceneResponse(savedSceneResponse);
                    preservation.setNotes(measureDto.getNotes());
                    preservation.setDeleted(false);

                    scenePreservationRepository.save(preservation);
                }
            }

            // Save medical support
            if (request.getMedicalSupport() != null) {
                for (SaveInitialResponseDto.MedicalSupportDto supportDto : request.getMedicalSupport()) {
                    MedicalSupport medicalSupport = new MedicalSupport();
                    medicalSupport.setSceneResponse(savedSceneResponse);
                    medicalSupport.setUnitId(supportDto.getUnitId());
                    medicalSupport.setTypeOfSupport(supportDto.getTypeOfSupport());
                    medicalSupport.setDeleted(false);

                    medicalSupportRepository.save(medicalSupport);
                }
            }

            return ResponseDto.success("Initial response data saved successfully", null);

        } catch (Exception e) {
            return ResponseDto.error("Error saving initial response: " + e.getMessage());
        }
    }
}
