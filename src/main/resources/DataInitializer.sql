-- =====================================================================
-- CRIMINAL DB - CORRECTED INSERT SCRIPT
-- =====================================================================
-- This script fixes the column count and foreign key issues
-- =====================================================================

SET FOREIGN_KEY_CHECKS = 0;
SET AUTOCOMMIT = 0;
START TRANSACTION;

-- =====================================================================
-- 1. ROLES (Additional roles - ADMIN, OFFICER, USER created by DataInitializer)
-- =====================================================================
INSERT INTO `roles` (`role_id`, `description`, `is_deleted`) VALUES
('DETECTIVE', 'Detective Officer - Lead investigations and case analysis', b'0'),
('PROSECUTOR', 'Prosecutor - Handle legal proceedings and case prosecution', b'0'),
('ANALYST', 'Crime Analyst - Data analysis and evidence processing', b'0'),
('FORENSIC_EXPERT', 'Forensic Expert - Specialized evidence analysis', b'0'),
('SUPERVISOR', 'Supervisor - Oversight and case management', b'0')
ON DUPLICATE KEY UPDATE 
    `description` = VALUES(`description`);

-- =====================================================================
-- 2. PERMISSIONS
-- =====================================================================
INSERT INTO `permissions` (`permission_id`, `description`, `is_deleted`) VALUES
('CREATE_CASE', 'Create new criminal cases', b'0'),
('UPDATE_CASE', 'Update case information and status', b'0'),
('DELETE_CASE', 'Delete cases (soft delete)', b'0'),
('VIEW_CASE_DETAILS', 'View detailed case information', b'0'),
('VIEW_EVIDENCE', 'View evidence details and files', b'0'),
('ANALYZE_EVIDENCE', 'Perform evidence analysis', b'0'),
('MANAGE_USERS', 'Create and manage user accounts', b'0'),
('CONDUCT_INTERVIEWS', 'Conduct and record interviews', b'0'),
('ISSUE_WARRANTS', 'Issue search and arrest warrants', b'0'),
('MAKE_ARRESTS', 'Process suspect arrests', b'0'),
('APPROVE_REPORTS', 'Approve and validate incident reports', b'0'),
('INITIATE_PROSECUTION', 'Start prosecution proceedings', b'0');

-- =====================================================================
-- 3. ROLE PERMISSIONS
-- =====================================================================
INSERT INTO `roles_permissions` (`role_id`, `permission_id`, `is_deleted`) VALUES
('ADMIN', 'MANAGE_USERS', b'0'),
('ADMIN', 'CREATE_CASE', b'0'),
('ADMIN', 'UPDATE_CASE', b'0'),
('ADMIN', 'DELETE_CASE', b'0'),
('OFFICER', 'CREATE_CASE', b'0'),
('OFFICER', 'UPDATE_CASE', b'0'),
('OFFICER', 'VIEW_CASE_DETAILS', b'0'),
('OFFICER', 'VIEW_EVIDENCE', b'0'),
('OFFICER', 'CONDUCT_INTERVIEWS', b'0'),
('OFFICER', 'MAKE_ARRESTS', b'0'),
('DETECTIVE', 'VIEW_CASE_DETAILS', b'0'),
('DETECTIVE', 'UPDATE_CASE', b'0'),
('DETECTIVE', 'VIEW_EVIDENCE', b'0'),
('DETECTIVE', 'ANALYZE_EVIDENCE', b'0'),
('DETECTIVE', 'CONDUCT_INTERVIEWS', b'0'),
('DETECTIVE', 'ISSUE_WARRANTS', b'0'),
('DETECTIVE', 'MAKE_ARRESTS', b'0'),
('PROSECUTOR', 'VIEW_CASE_DETAILS', b'0'),
('PROSECUTOR', 'VIEW_EVIDENCE', b'0'),
('PROSECUTOR', 'APPROVE_REPORTS', b'0'),
('PROSECUTOR', 'INITIATE_PROSECUTION', b'0'),
('ANALYST', 'VIEW_CASE_DETAILS', b'0'),
('ANALYST', 'VIEW_EVIDENCE', b'0'),
('ANALYST', 'ANALYZE_EVIDENCE', b'0'),
('FORENSIC_EXPERT', 'VIEW_EVIDENCE', b'0'),
('FORENSIC_EXPERT', 'ANALYZE_EVIDENCE', b'0')
ON DUPLICATE KEY UPDATE `is_deleted` = VALUES(`is_deleted`);

-- =====================================================================
-- 4. USERS (CORRECTED - Added missing columns)
-- =====================================================================
SET @test_password = '$2a$10$BNevefbhNVEQY4Wh9mJ1memzGGX1LWHzLYX83rj1hLCo0EPcTb28u';

INSERT INTO `users` (`username`, `avatar_url`, `create_at`, `date_attended`, `dob`, `full_name`, `gender`, `is_deleted`, `password_hash`, `phone_number`, `refresh_token`, `status`, `role_id`) VALUES
('admin001', NULL, NOW(), '2020-01-15 09:00:00', '1975-05-15', 'System Administrator', 1, b'0', @test_password, '+1-555-0001', NULL, 'ACTIVE', 'ADMIN'),
('officer001', NULL, NOW(), '2021-03-10 09:00:00', '1985-08-22', 'Jane Smith', 2, b'0', @test_password, '+1-555-0002', NULL, 'ACTIVE', 'OFFICER'),
('officer002', NULL, NOW(), '2020-06-01 09:00:00', '1982-12-08', 'Mike Johnson', 1, b'0', @test_password, '+1-555-0003', NULL, 'ACTIVE', 'OFFICER'),
('detective001', NULL, NOW(), '2019-01-20 09:00:00', '1978-03-25', 'Sarah Connor', 2, b'0', @test_password, '+1-555-0004', NULL, 'ACTIVE', 'DETECTIVE'),
('detective002', NULL, NOW(), '2019-06-15 09:00:00', '1980-11-05', 'James Bond', 1, b'0', @test_password, '+1-555-0012', NULL, 'ACTIVE', 'DETECTIVE'),
('prosecutor001', NULL, NOW(), '2018-09-15 09:00:00', '1975-11-12', 'Robert Williams', 1, b'0', @test_password, '+1-555-0005', NULL, 'ACTIVE', 'PROSECUTOR'),
('prosecutor002', NULL, NOW(), '2020-03-01 09:00:00', '1982-07-18', 'Maria Gonzalez', 2, b'0', @test_password, '+1-555-0013', NULL, 'ACTIVE', 'PROSECUTOR'),
('analyst001', NULL, NOW(), '2022-02-01 09:00:00', '1990-07-30', 'Lisa Davis', 2, b'0', @test_password, '+1-555-0006', NULL, 'ACTIVE', 'ANALYST'),
('forensic001', NULL, NOW(), '2017-09-10 09:00:00', '1973-04-22', 'Dr. Henry Lab', 1, b'0', @test_password, '+1-555-0014', NULL, 'ACTIVE', 'FORENSIC_EXPERT'),
('user001', NULL, NOW(), '2023-01-10 09:00:00', '1988-04-12', 'Regular User One', 1, b'0', @test_password, '+1-555-0007', NULL, 'ACTIVE', 'USER');

-- =====================================================================
-- 5. CASES
-- =====================================================================
INSERT INTO `cases` (`case_id`, `case_name`, `create_at`, `is_deleted`, `severity`, `status`, `summary`, `type_case`) VALUES
('CASE-2024-001', 'Downtown Bank Robbery', '2024-01-15 10:30:00', b'0', 'HIGH', 'IN_PROCESS', 'Armed robbery at First National Bank downtown branch involving two masked suspects', 'ROBBERY'),
('CASE-2024-002', 'Riverside Park Murder', '2024-02-20 14:15:00', b'0', 'CRITICAL', 'IN_PROCESS', 'Homicide investigation - victim found with gunshot wounds at Riverside Park', 'MURDER'),
('CASE-2024-003', 'University Campus Assault', '2024-03-10 09:45:00', b'0', 'HIGH', 'PENDING_APPROVAL', 'Sexual assault case reported on university campus near library', 'RAPE'),
('CASE-2024-004', 'Convenience Store Theft Ring', '2024-01-05 16:20:00', b'0', 'MEDIUM', 'DONE', 'Multiple coordinated thefts from convenience stores across the city', 'ROBBERY');

-- =====================================================================
-- 6. REPORTS
-- =====================================================================
INSERT INTO `reports` (`report_id`, `case_location`, `description`, `incident_date`, `is_deleted`, `reported_at`, `reporter_email`, `reporter_fullname`, `reporter_incident_relationship`, `reporter_location`, `reporter_phone_number`, `severity`, `status`, `case_id`, `username`) VALUES
('RPT-2024-001', '456 Bank Street, Downtown', 'Witnessed two masked individuals enter bank with weapons, heard gunshots and screaming', '2024-01-15 09:30:00', b'0', '2024-01-15 10:00:00', 'mary.thompson@email.com', 'Mary Thompson', 'WITNESS', '123 Main Street', '+1-555-1001', 'SERIOUS', 'APPROVED', 'CASE-2024-001', 'officer001'),
('RPT-2024-002', 'Riverside Park Main Trail', 'Found deceased male victim with apparent gunshot wounds during morning jog', '2024-02-20 13:00:00', b'0', '2024-02-20 13:30:00', NULL, 'Anonymous Caller', 'BYSTANDER', 'Unknown', '+1-555-0000', 'CRITICAL', 'APPROVED', 'CASE-2024-002', 'officer002'),
('RPT-2024-003', 'University Library 3rd Floor', 'Sexual assault incident in library study area, suspect fled scene', '2024-03-09 22:00:00', b'0', '2024-03-10 08:00:00', 'emily.r@university.edu', 'Emily Rodriguez', 'VICTIM', 'University Student Dormitory', '+1-555-1003', 'SERIOUS', 'PENDING', 'CASE-2024-003', 'officer001');

-- =====================================================================
-- 7. VICTIMS
-- =====================================================================
INSERT INTO `victims` (`victim_id`, `contact`, `description`, `fullname`, `gender`, `injuries`, `is_deleted`, `national`, `status`, `case_id`) VALUES
('VIC-001', '+1-555-2001', 'Bank manager present during robbery, cooperated with suspects under duress', 'Robert Bank Manager', 1, 'Minor lacerations from broken glass, psychological trauma', b'0', 'American', 1, 'CASE-2024-001'),
('VIC-002', 'Unknown', 'Murder victim discovered at Riverside Park, identity confirmed through fingerprints', 'John Mitchell Doe', 1, 'Fatal gunshot wound to chest, defensive wounds on hands', b'0', 'Unknown', 0, 'CASE-2024-002'),
('VIC-003', '+1-555-1003', 'University student, victim of campus assault', 'Emily Rodriguez', 2, 'Physical trauma consistent with assault, psychological counseling recommended', b'0', 'Hispanic-American', 1, 'CASE-2024-003');

-- =====================================================================
-- 8. WITNESSES
-- =====================================================================
INSERT INTO `witnesses` (`witness_id`, `contact`, `full_name`, `is_deleted`, `national`, `statement`, `witness_id_card`, `case_id`) VALUES
('WIT-001', '+1-555-1001', 'Mary Thompson', b'0', 'American', 'Observed two masked suspects enter bank at approximately 9:30 AM, heard gunshots and screaming, saw suspects flee in dark sedan', 123456789, 'CASE-2024-001'),
('WIT-002', '+1-555-1002', 'James Walker', b'0', 'American', 'Regular jogger discovered victim at Riverside Park around 1:30 PM, immediately called 911, did not observe any suspects', 987654321, 'CASE-2024-002'),
('WIT-003', '+1-555-1004', 'Library Student', b'0', 'American', 'Observed suspicious individual near library third floor study area around incident time, can provide physical description', 456789123, 'CASE-2024-003');

-- =====================================================================
-- 9. SUSPECTS
-- =====================================================================
INSERT INTO `suspects` (`suspect_id`, `address`, `catch_time`, `description`, `dob`, `fingerprints_hash`, `fullname`, `gender`, `health_status`, `identification`, `is_deleted`, `mugshot_url`, `national`, `notes`, `phone_number`, `status`, `suspect_id_card`, `case_id`, `report_id`) VALUES
('SUS-001', '789 Criminal Street', '2024-01-16 15:30:00', 'Tall male, brown hair, athletic build, distinctive tattoo on left forearm', '1995-06-15', 'fp_hash_123456', 'Michael Thompson', 'Male', 'HEALTHY', 'DL123456789', b'0', '/images/mugshot001.jpg', 'American', 'Previous arrests for armed robbery, known associate of criminal networks', '+1-555-3001', 'ARRESTED', 111111111, 'CASE-2024-001', 'RPT-2024-001'),
('SUS-002', '456 Suspect Avenue', NULL, 'Medium build, black hair, facial scar above right eyebrow', '1988-03-22', 'fp_hash_789012', 'David Wilson', 'Male', 'UNKNOWN', 'DL789012345', b'0', NULL, 'American', 'History of violent crimes, considered armed and dangerous', '+1-555-3002', 'WANTED', 222222222, 'CASE-2024-002', 'RPT-2024-002'),
('SUS-003', '321 Campus Road', NULL, 'University student, blonde hair, clean appearance, no visible distinguishing marks', '1992-11-08', NULL, 'Alex Brown', 'Male', 'HEALTHY', 'DL345678901', b'0', NULL, 'American', 'University student with clean record, under investigation for campus incident', '+1-555-3003', 'UNDER_INVESTIGATION', 333333333, 'CASE-2024-003', 'RPT-2024-003');

-- =====================================================================
-- 10. EVIDENCES
-- =====================================================================
INSERT INTO `evidences` (`evidence_id`, `attach_file`, `collected_at`, `current_location`, `description`, `evidence_type`, `is_deleted`, `status`, `case_id`, `report_id`, `username`, `warrant_id`) VALUES
('EVI-001', 'bank_footage_complete.mp4', '2024-01-15 11:00:00', 'Digital Evidence Lab - Server A', 'Bank security camera footage showing suspects entering and exiting', 'DIGITAL_EVIDENCE', b'0', 'ANALYZED', 'CASE-2024-001', 'RPT-2024-001', 'officer001', NULL),
('EVI-002', 'fingerprint_set_001.jpg', '2024-01-15 12:00:00', 'Forensics Lab - Station 3', 'Fingerprints lifted from bank counter and door handles', 'PHYSICAL_EVIDENCE', b'0', 'ANALYZED', 'CASE-2024-001', 'RPT-2024-001', 'officer002', NULL),
('EVI-003', 'murder_weapon_002.jpg', '2024-02-20 14:30:00', 'Evidence Room B - Locker 20', 'Murder weapon - 9mm pistol found at scene', 'PHYSICAL_EVIDENCE', b'0', 'ANALYZED', 'CASE-2024-002', 'RPT-2024-002', 'detective001', NULL),
('EVI-004', 'victim_dna_sample.vial', '2024-02-20 16:00:00', 'Forensics Lab - Freezer Unit 1', 'Victim DNA sample for identification', 'BIOLOGICAL_EVIDENCE', b'0', 'ANALYZED', 'CASE-2024-002', 'RPT-2024-002', 'forensic001', NULL),
('EVI-005', 'campus_security_footage.mp4', '2024-03-10 12:00:00', 'Digital Evidence Lab - Server B', 'Campus security footage from library area', 'DIGITAL_EVIDENCE', b'0', 'PROCESSING', 'CASE-2024-003', 'RPT-2024-003', 'analyst001', NULL);

-- =====================================================================
-- 11. ARRESTS
-- =====================================================================
INSERT INTO `arrests` (`case_id`, `suspect_id`, `arrest_start_time`, `arrest_end_time`, `suspect_miranda_signature`, `is_deleted`) VALUES
('CASE-2024-001', 'SUS-001', '2024-01-16 15:30:00', '2024-01-16 18:00:00', 'Michael Thompson', b'0');

-- =====================================================================
-- 12. INTERVIEWS
-- =====================================================================
-- =====================================================================
-- 12. INTERVIEWS
-- =====================================================================
INSERT INTO `interviews` (`interview_id`, `create_at`, `end_time`, `is_deleted`, `location`, `start_time`, `type_interviewee`, `update_at`, `case_interviewee_id`, `suspect_interviewee_id`, `user_interviewer_id`, `victim_interviewee_id`, `witness_interviewee_id`) VALUES
('INT-001', NOW(), '2024-01-16 11:30:00', b'0', 'Police Station - Interview Room 1', '2024-01-16 10:00:00', 'WITNESS', NOW(), NULL, NULL, 'detective001', NULL, 'WIT-001'),
('INT-002', NOW(), '2024-01-16 18:00:00', b'0', 'Police Station - Interrogation Room A', '2024-01-16 16:00:00', 'SUSPECT', NOW(), NULL, 'SUS-001', 'detective001', NULL, NULL),
('INT-003', NOW(), '2024-01-15 16:00:00', b'0', 'Hospital - Private Room', '2024-01-15 15:00:00', 'VICTIM', NOW(), NULL, NULL, 'officer001', 'VIC-001', NULL);

-- =====================================================================
-- 13. QUESTIONS
-- =====================================================================
INSERT INTO `question` (`question_id`, `answer`, `content`, `is_deleted`, `reliability`, `interview_id`, `username`) VALUES
('Q-001', 'Around 9:30 AM, I was walking by when I saw two men in masks', 'What time did you see the suspects enter the bank?', b'0', 0.9, 'INT-001', 'detective001'),
('Q-002', 'Both wore dark hoodies and jeans, one had a red backpack', 'Can you describe what the suspects were wearing?', b'0', 0.8, 'INT-001', 'detective001'),
('Q-003', 'I was at home sleeping, I never went to any bank', 'Where were you at 9:30 AM on January 15th?', b'0', 0.3, 'INT-002', 'detective001'),
('Q-004', 'No, they wore masks but I could see their eyes', 'Did you see the faces of the robbers?', b'0', 0.7, 'INT-003', 'officer001');

-- =====================================================================
-- 14. INTERVIEW FILES
-- =====================================================================
INSERT INTO `interview_files` (`interview_file_id`, `attached_file`, `create_at`, `is_deleted`, `interview_id`) VALUES
('IF-001', 'witness_statement_001.mp4', NOW(), b'0', 'INT-001'),
('IF-002', 'suspect_interrogation_001.mp4', NOW(), b'0', 'INT-002'),
('IF-003', 'victim_interview_001.mp4', NOW(), b'0', 'INT-003');

-- =====================================================================
-- 15. USER CASE ASSIGNMENTS
-- =====================================================================
INSERT INTO `users_cases` (`case_id`, `username`, `assigned_at`, `is_deleted`, `notes`) VALUES
('CASE-2024-001', 'detective001', '2024-01-15 11:00:00', b'0', 'Lead investigator for bank robbery case'),
('CASE-2024-001', 'officer001', '2024-01-15 11:00:00', b'0', 'Supporting officer for evidence collection'),
('CASE-2024-002', 'detective001', '2024-02-20 15:00:00', b'0', 'Lead investigator for murder case'),
('CASE-2024-002', 'officer002', '2024-02-20 15:00:00', b'0', 'Crime scene officer'),
('CASE-2024-003', 'officer001', '2024-03-10 10:00:00', b'0', 'Assigned to campus assault case');

-- =====================================================================
-- 16. TASKS
-- =====================================================================
INSERT INTO `tasks` (`task_id`, `completed_at`, `content`, `due_date`, `is_deleted`, `start_date`, `status`, `task_name`, `case_id`, `username`) VALUES
('TASK-001', '2024-01-16 10:30:00', 'Analyze bank security camera footage for suspect identification and timeline establishment', '2024-01-16 12:00:00', b'0', '2024-01-15 12:00:00', 'COMPLETED', 'Security Footage Analysis', 'CASE-2024-001', 'detective001'),
('TASK-002', '2024-01-16 16:00:00', 'Schedule and conduct interviews with all bank witnesses including staff and customers', '2024-01-17 17:00:00', b'0', '2024-01-16 09:00:00', 'COMPLETED', 'Witness Interview Coordination', 'CASE-2024-001', 'detective001'),
('TASK-003', NULL, 'Process all fingerprint evidence collected from bank crime scene', '2024-01-18 13:00:00', b'0', '2024-01-16 13:00:00', 'EXECUTING', 'Forensic Evidence Processing', 'CASE-2024-001', 'officer001'),
('TASK-004', NULL, 'Complete ballistics testing on murder weapon', '2024-02-25 17:00:00', b'0', '2024-02-21 10:00:00', 'EXECUTING', 'Ballistics Analysis', 'CASE-2024-002', 'detective001');

-- =====================================================================
-- 17. INVESTIGATION PLANS
-- =====================================================================
INSERT INTO `investigations_plans` (`investigation_plan_id`, `created_at`, `deadline_date`, `is_deleted`, `plan_content`, `result`, `status`, `case_id`, `created_officer_id`) VALUES
('IP-001', '2024-01-15 11:30:00', '2024-02-15 17:00:00', b'0', 'Complete investigation of bank robbery including: 1) Evidence collection 2) Witness interviews 3) Suspect identification 4) Arrest and prosecution', 'Primary suspect arrested, awaiting trial', 'COMPLETED', 'CASE-2024-001', 'detective001'),
('IP-002', '2024-02-20 15:30:00', '2024-04-20 17:00:00', b'0', 'Murder investigation protocol: 1) Secure crime scene 2) Collect physical evidence 3) Conduct autopsy 4) Interview potential witnesses 5) Identify suspects', 'Investigation ongoing, weapon found', 'IN_PROGRESS', 'CASE-2024-002', 'detective001'),
('IP-003', '2024-03-10 11:00:00', '2024-04-10 17:00:00', b'0', 'Campus assault investigation: 1) Victim interview 2) Collect digital evidence 3) Review campus security 4) Interview witnesses', NULL, 'PENDING', 'CASE-2024-003', 'officer001');

-- =====================================================================
-- 18. CASES RESULTS
-- =====================================================================
INSERT INTO `cases_results` (`case_result_id`, `identify_motive`, `is_deleted`, `report_analyst`, `report_time`, `status`, `summary`, `case_id`) VALUES
('CR-001', 'Financial gain - suspects needed money for drug debts', b'0', 'detective001', '2024-01-20 14:00:00', 'PRELIMINARY', 'Bank robbery case shows clear motive and evidence linking suspect to crime', 'CASE-2024-001'),
('CR-002', 'Personal vendetta - victim had gambling debts to wrong people', b'0', 'detective001', '2024-02-25 16:00:00', 'ONGOING', 'Murder investigation reveals complex motive involving illegal gambling', 'CASE-2024-002');

-- =====================================================================
-- 19. SENTENCES
-- =====================================================================
INSERT INTO `sentences` (`sentence_id`, `duration`, `is_deleted`, `sentence_condition`, `sentence_type`, `sentencing_date`, `case_result_id`) VALUES
('SEN-001', '5 years', b'0', 'Eligible for parole after 3 years with good behavior', 'IMPRISONMENT', '2024-01-25 10:00:00', 'CR-001');

-- =====================================================================
-- 20. TIMELINES
-- =====================================================================
INSERT INTO `timelines` (`timeline_id`, `activity`, `attached_file`, `end_time`, `is_deleted`, `notes`, `start_time`, `case_result_id`) VALUES
('TL-001', 'Case Investigation Started', '{"files": ["investigation_log.pdf"]}', '2024-01-20 14:00:00', b'0', 'Initial investigation and evidence collection phase', '2024-01-15 10:30:00', 'CR-001'),
('TL-002', 'Suspect Arrest', '{"files": ["arrest_report.pdf"]}', '2024-01-16 18:00:00', b'0', 'Primary suspect arrested and processed', '2024-01-16 15:30:00', 'CR-001'),
('TL-003', 'Murder Scene Investigation', '{"files": ["crime_scene_photos.zip"]}', '2024-02-21 18:00:00', b'0', 'Initial crime scene processing and evidence collection', '2024-02-20 14:00:00', 'CR-002');

-- =====================================================================
-- 21. PROSECUTIONS
-- =====================================================================
INSERT INTO `prosecutions` (`prosecution_id`, `decision`, `decision_date`, `is_deleted`, `reason`, `case_id`, `username`) VALUES
('PROS-001', 'PROCEED', '2024-01-22 09:00:00', b'0', 'Sufficient evidence to proceed with prosecution for armed robbery', 'CASE-2024-001', 'prosecutor001'),
('PROS-002', 'PENDING', NULL, b'0', 'Awaiting additional evidence from forensics', 'CASE-2024-002', 'prosecutor001');

-- =====================================================================
-- 22. PROSECUTIONS USERS
-- =====================================================================
INSERT INTO `prosecutions_users` (`prosecution_id`, `username`, `is_deleted`) VALUES
('PROS-001', 'prosecutor001', b'0'),
('PROS-001', 'detective001', b'0'),
('PROS-002', 'prosecutor001', b'0'),
('PROS-002', 'detective001', b'0');

-- =====================================================================
-- 23. INDICTMENTS
-- =====================================================================
INSERT INTO `indictments` (`indictment_id`, `content`, `is_deleted`, `issued_at`, `prosecution_id`) VALUES
('IND-001', 'The Grand Jury charges that on January 15, 2024, Michael Thompson did willfully and unlawfully commit armed robbery at First National Bank...', b'0', '2024-01-25 14:00:00', 'PROS-001');

-- =====================================================================
-- 24. INMATES
-- =====================================================================
INSERT INTO `inmates` (`inmate_id`, `assigned_facility`, `expected_release`, `full_name`, `health_status`, `is_deleted`, `start_date`, `status`) VALUES
('INM-001', 'County Detention Center', '2029-01-16 00:00:00', 'Michael Thompson', 'HEALTHY', b'0', '2024-01-16 20:00:00', 'INCARCERATED');

-- =====================================================================
-- 25. EVENTS
-- =====================================================================
INSERT INTO `events` (`event_id`, `case_id`, `description`, `event_name`, `is_deleted`, `suspect_id`, `time_end`, `time_start`) VALUES
('EVT-001', 'CASE-2024-001', 'Armed robbery at First National Bank downtown branch', 'Bank Robbery Incident', b'0', 'SUS-001', '2024-01-15 09:45:00', '2024-01-15 09:28:00'),
('EVT-002', 'CASE-2024-002', 'Body discovered at Riverside Park by jogger', 'Murder Discovery', b'0', 'SUS-002', '2024-02-20 13:30:00', '2024-02-20 13:30:00'),
('EVT-003', 'CASE-2024-003', 'Sexual assault reported at university library', 'Campus Assault Incident', b'0', 'SUS-003', '2024-03-09 22:30:00', '2024-03-09 22:00:00');

-- =====================================================================
-- 26. HOLIDAYS
-- =====================================================================
INSERT INTO `holidays` (`holiday_id`, `date_of_holiday`, `holiday_name`, `is_deleted`, `notes`, `type_of_holiday`) VALUES
('HOL-001', '2024-01-01 00:00:00', 'New Year Day', b'0', 'Federal holiday - limited court operations', 'FEDERAL_HOLIDAY'),
('HOL-002', '2024-07-04 00:00:00', 'Independence Day', b'0', 'Federal holiday - courts closed', 'FEDERAL_HOLIDAY'),
('HOL-003', '2024-12-25 00:00:00', 'Christmas Day', b'0', 'Federal holiday - emergency operations only', 'FEDERAL_HOLIDAY'),
('HOL-004', '2024-06-15 00:00:00', 'State Foundation Day', b'0', 'State holiday - local courts may be closed', 'STATES_HOLIDAY');

-- =====================================================================
-- 27. DIGITAL INVESTIGATIONS
-- =====================================================================
INSERT INTO `digitals_invests` (`evidence_id`, `analyst_tool`, `device_type`, `is_deleted`, `result`) VALUES
('EVI-001', 'VideoAnalyzer Pro', 'CCTV Camera', b'0', 'Clear footage of two suspects entering bank at 09:28 AM'),
('EVI-005', 'VideoForensics Suite', 'IP Security Camera', b'0', 'Multiple camera angles captured incident timeline');

-- =====================================================================
-- 28. PHYSICAL INVESTIGATIONS
-- =====================================================================
INSERT INTO `physicals_invests` (`evidence_id`, `image_url`, `is_deleted`) VALUES
('EVI-002', '/images/fingerprints_001.jpg', b'0'),
('EVI-003', '/images/weapon_001.jpg', b'0');

-- =====================================================================
-- 29. FORENSICS INVESTIGATIONS
-- =====================================================================
INSERT INTO `forensics_invests` (`evidence_id`, `is_deleted`, `lab_name`, `received_at`, `report`, `result_summary`) VALUES
('EVI-003', b'0', 'State Crime Lab', '2024-02-21 09:00:00', 'Ballistics Report #2024-021', 'Weapon fired recently, matches bullet from victim'),
('EVI-004', b'0', 'DNA Analysis Lab', '2024-02-21 10:00:00', 'DNA Report #2024-022', 'DNA profile extracted successfully');

-- =====================================================================
-- 30. RELATIONSHIP TABLES
-- =====================================================================

-- Case-Evidence relationships
INSERT INTO `cases_evidences` (`case_id`, `evidence_id`, `is_deleted`) VALUES
('CASE-2024-001', 'EVI-001', b'0'),
('CASE-2024-001', 'EVI-002', b'0'),
('CASE-2024-002', 'EVI-003', b'0'),
('CASE-2024-002', 'EVI-004', b'0'),
('CASE-2024-003', 'EVI-005', b'0');

-- Suspect-Evidence relationships
INSERT INTO `suspects_evidences` (`evidence_id`, `suspect_id`, `is_deleted`) VALUES
('EVI-001', 'SUS-001', b'0'),
('EVI-002', 'SUS-001', b'0'),
('EVI-003', 'SUS-002', b'0'),
('EVI-004', 'SUS-002', b'0'),
('EVI-005', 'SUS-003', b'0');

-- Report-Victim relationships
INSERT INTO `reports_victims` (`report_id`, `victim_id`, `is_deleted`) VALUES
('RPT-2024-001', 'VIC-001', b'0'),
('RPT-2024-002', 'VIC-002', b'0'),
('RPT-2024-003', 'VIC-003', b'0');

-- Report-Witness relationships
INSERT INTO `reports_witnesses` (`report_id`, `witness_id`, `is_deleted`) VALUES
('RPT-2024-001', 'WIT-001', b'0'),
('RPT-2024-002', 'WIT-002', b'0'),
('RPT-2024-003', 'WIT-003', b'0');

-- =====================================================================
-- COMMIT TRANSACTION
-- =====================================================================
SET FOREIGN_KEY_CHECKS = 1;
COMMIT;

-- =====================================================================
-- VERIFICATION QUERIES
-- =====================================================================
SELECT 'Criminal Investigation Database Test Data Successfully Inserted!' as CompletionStatus;

-- Uncomment to verify data insertion
/*
SELECT 
    'Users' as TableName, COUNT(*) as RecordCount FROM users WHERE is_deleted = b'0'
UNION ALL SELECT 'Cases', COUNT(*) FROM cases WHERE is_deleted = b'0'
UNION ALL SELECT 'Reports', COUNT(*) FROM reports WHERE is_deleted = b'0'
UNION ALL SELECT 'Suspects', COUNT(*) FROM suspects WHERE is_deleted = b'0'
UNION ALL SELECT 'Victims', COUNT(*) FROM victims WHERE is_deleted = b'0'
UNION ALL SELECT 'Witnesses', COUNT(*) FROM witnesses WHERE is_deleted = b'0'
UNION ALL SELECT 'Evidences', COUNT(*) FROM evidences WHERE is_deleted = b'0'
UNION ALL SELECT 'Interviews', COUNT(*) FROM interviews WHERE is_deleted = b'0'
UNION ALL SELECT 'Tasks', COUNT(*) FROM tasks WHERE is_deleted = b'0'
ORDER BY TableName;
*/

-- =====================================================================
-- DEPLOYMENT NOTES:
-- =====================================================================
/*
1. Run this script AFTER Spring Boot application startup
2. Ensure DataInitializer.java has created ADMIN, OFFICER, USER roles
3. All column counts and foreign keys have been corrected
4. Test data includes realistic scenarios for application testing
5. All enum values match the application schema definitions
*/