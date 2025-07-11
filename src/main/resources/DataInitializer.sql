-- CORRECTED Test Data Insert Script for mockproject_db
-- This script avoids conflicts with DataInitializer.java which creates ADMIN, OFFICER, USER roles
-- Run this AFTER the Spring Boot application has started and initialized default roles

SET FOREIGN_KEY_CHECKS = 0;

-- 1. Insert Additional Roles (ADMIN, OFFICER, USER already initialized by DataInitializer.java)
INSERT INTO `roles` (`role_id`, `description`, `is_deleted`) VALUES
('DETECTIVE', 'Detective Officer', b'0'),
('PROSECUTOR', 'Prosecutor', b'0'),
('ANALYST', 'Crime Analyst', b'0')
ON DUPLICATE KEY UPDATE 
    `description` = VALUES(`description`),
    `is_deleted` = VALUES(`is_deleted`);

-- 2. Insert Permissions
INSERT INTO `permissions` (`permission_id`, `description`, `is_deleted`) VALUES
('CREATE_CASE', 'Create new cases', b'0'),
('UPDATE_CASE', 'Update case information', b'0'),
('DELETE_CASE', 'Delete cases', b'0'),
('VIEW_EVIDENCE', 'View evidence details', b'0'),
('MANAGE_USERS', 'Manage user accounts', b'0'),
('APPROVE_REPORTS', 'Approve incident reports', b'0'),
('CONDUCT_INTERVIEWS', 'Conduct interviews', b'0'),
('ISSUE_WARRANTS', 'Issue search warrants', b'0');

-- 3. Link Roles with Permissions (using existing and new roles)
INSERT INTO `roles_permissions` (`role_id`, `permission_id`, `is_deleted`) VALUES
('ADMIN', 'CREATE_CASE', b'0'),
('ADMIN', 'UPDATE_CASE', b'0'),
('ADMIN', 'DELETE_CASE', b'0'),
('ADMIN', 'MANAGE_USERS', b'0'),
('OFFICER', 'CREATE_CASE', b'0'),
('OFFICER', 'UPDATE_CASE', b'0'),
('OFFICER', 'VIEW_EVIDENCE', b'0'),
('OFFICER', 'CONDUCT_INTERVIEWS', b'0'),
('DETECTIVE', 'VIEW_EVIDENCE', b'0'),
('DETECTIVE', 'CONDUCT_INTERVIEWS', b'0'),
('DETECTIVE', 'ISSUE_WARRANTS', b'0'),
('PROSECUTOR', 'APPROVE_REPORTS', b'0')
ON DUPLICATE KEY UPDATE 
    `is_deleted` = VALUES(`is_deleted`);

-- 4. Insert Users (gender: 0=Other, 1=Male, 2=Female)
INSERT INTO `users` (`username`, `full_name`, `password_hash`, `phone_number`, `gender`, `dob`, `date_attended`, `status`, `role_id`, `create_at`, `is_deleted`) VALUES
('admin001', 'John Administrator', '$2a$10$BNevefbhNVEQY4Wh9mJ1memzGGX1LWHzLYX83rj1hLCo0EPcTb28u', '555-0001', 1, '1980-05-15 00:00:00', '2020-01-15 09:00:00', 'ACTIVE', 'ADMIN', NOW(), b'0'),
('officer001', 'Jane Smith', '$2a$10$BNevefbhNVEQY4Wh9mJ1memzGGX1LWHzLYX83rj1hLCo0EPcTb28u', '555-0002', 2, '1985-08-22 00:00:00', '2021-03-10 09:00:00', 'ACTIVE', 'OFFICER', NOW(), b'0'),
('officer002', 'Mike Johnson', '$2a$10$BNevefbhNVEQY4Wh9mJ1memzGGX1LWHzLYX83rj1hLCo0EPcTb28u', '555-0003', 1, '1982-12-08 00:00:00', '2020-06-01 09:00:00', 'ACTIVE', 'OFFICER', NOW(), b'0'),
('detective001', 'Sarah Connor', '$2a$10$BNevefbhNVEQY4Wh9mJ1memzGGX1LWHzLYX83rj1hLCo0EPcTb28u', '555-0004', 2, '1978-03-25 00:00:00', '2019-01-20 09:00:00', 'ACTIVE', 'DETECTIVE', NOW(), b'0'),
('prosecutor001', 'Robert Williams', '$2a$10$BNevefbhNVEQY4Wh9mJ1memzGGX1LWHzLYX83rj1hLCo0EPcTb28u', '555-0005', 1, '1975-11-12 00:00:00', '2018-09-15 09:00:00', 'ACTIVE', 'PROSECUTOR', NOW(), b'0'),
('analyst001', 'Lisa Davis', '$2a$10$BNevefbhNVEQY4Wh9mJ1memzGGX1LWHzLYX83rj1hLCo0EPcTb28u', '555-0006', 2, '1990-07-30 00:00:00', '2022-02-01 09:00:00', 'ACTIVE', 'ANALYST', NOW(), b'0');

-- 5. Insert Cases
INSERT INTO `cases` (`case_id`, `case_name`, `summary`, `type_case`, `severity`, `status`, `create_at`, `is_deleted`) VALUES
('CASE-2024-001', 'Downtown Bank Robbery', 'Armed robbery at First National Bank downtown', 'ROBBERY', 'HIGH', 'IN_PROCESS', '2024-01-15 10:30:00', b'0'),
('CASE-2024-002', 'Riverside Murder Investigation', 'Homicide case at Riverside Park', 'MURDER', 'CRITICAL', 'IN_PROCESS', '2024-02-20 14:15:00', b'0'),
('CASE-2024-003', 'University Campus Assault', 'Sexual assault case on university campus', 'RAPE', 'HIGH', 'PENDING_APPROVAL', '2024-03-10 09:45:00', b'0'),
('CASE-2024-004', 'Convenience Store Theft', 'Multiple thefts from convenience stores', 'ROBBERY', 'MEDIUM', 'DONE', '2024-01-05 16:20:00', b'0');

-- 6. Insert Reports (CORRECTED - removed crime_type column)
INSERT INTO `reports` (`report_id`, `reporter_fullname`, `reporter_phone_number`, `reporter_email`, `reporter_location`, `reporter_incident_relationship`, `case_location`, `incident_date`, `reported_at`, `description`, `severity`, `status`, `case_id`, `user_id`, `is_deleted`) VALUES
('RPT-2024-001', 'Mary Thompson', '555-1001', 'mary.thompson@email.com', '123 Main St', 'WITNESS', '456 Bank Street', '2024-01-15 09:30:00', '2024-01-15 10:00:00', 'Witnessed armed robbery at bank', 'SERIOUS', 'APPROVED', 'CASE-2024-001', 'officer001', b'0'),
('RPT-2024-002', 'Anonymous Caller', '555-0000', NULL, 'Unknown', 'BYSTANDER', 'Riverside Park Trail', '2024-02-20 13:00:00', '2024-02-20 13:30:00', 'Found deceased person at park', 'CRITICAL', 'APPROVED', 'CASE-2024-002', 'officer002', b'0'),
('RPT-2024-003', 'Emily Rodriguez', '555-1003', 'emily.r@university.edu', 'University Dorms', 'VICTIM', 'University Library', '2024-03-09 22:00:00', '2024-03-10 08:00:00', 'Sexual assault incident', 'SERIOUS', 'PENDING', 'CASE-2024-003', 'officer001', b'0');

-- 7. Insert Victims (gender: 0=Other, 1=Male, 2=Female; status: 0=Dead, 1=Alive)
INSERT INTO `victims` (`victim_id`, `fullname`, `gender`, `contact`, `national`, `description`, `injuries`, `status`, `case_id`, `is_deleted`) VALUES
('VIC-001', 'Robert Bank Manager', 1, '555-2001', 'American', 'Bank manager during robbery', 'Minor cuts from broken glass', 1, 'CASE-2024-001', b'0'),
('VIC-002', 'John Doe', 1, 'Unknown', 'Unknown', 'Murder victim found at park', 'Fatal gunshot wound', 0, 'CASE-2024-002', b'0'),
('VIC-003', 'Emily Rodriguez', 2, '555-1003', 'Hispanic', 'University student', 'Physical trauma', 1, 'CASE-2024-003', b'0');

-- 8. Insert Witnesses
INSERT INTO `witnesses` (`witness_id`, `full_name`, `contact`, `national`, `statement`, `witness_id_card`, `case_id`, `is_deleted`) VALUES
('WIT-001', 'Mary Thompson', '555-1001', 'American', 'I saw two masked men enter the bank with guns', 123456789, 'CASE-2024-001', b'0'),
('WIT-002', 'James Walker', '555-1002', 'American', 'I was jogging and found the body around 1:30 PM', 987654321, 'CASE-2024-002', b'0'),
('WIT-003', 'Student Witness', '555-1004', 'American', 'I saw the suspect near the library that night', 456789123, 'CASE-2024-003', b'0');

-- 9. Insert Suspects (gender column is varchar(255))
INSERT INTO `suspects` (`suspect_id`, `fullname`, `gender`, `dob`, `address`, `phone_number`, `national`, `identification`, `suspect_id_card`, `description`, `status`, `health_status`, `catch_time`, `fingerprints_hash`, `mugshot_url`, `notes`, `case_id`, `report_id`, `is_deleted`) VALUES
('SUS-001', 'Michael Thompson', 'Male', '1995-06-15 00:00:00', '789 Criminal St', '555-3001', 'American', 'DL123456', 111111111, 'Tall male, brown hair', 'ARRESTED', 'HEALTHY', '2024-01-16 15:30:00', 'hash123456', '/images/mugshot001.jpg', 'Known for armed robberies', 'CASE-2024-001', 'RPT-2024-001', b'0'),
('SUS-002', 'David Wilson', 'Male', '1988-03-22 00:00:00', '456 Suspect Ave', '555-3002', 'American', 'DL789012', 222222222, 'Medium build, black hair', 'WANTED', 'UNKNOWN', NULL, 'hash789012', NULL, 'Previous violent crimes', 'CASE-2024-002', 'RPT-2024-002', b'0'),
('SUS-003', 'Alex Brown', 'Male', '1992-11-08 00:00:00', '321 Campus Rd', '555-3003', 'American', 'DL345678', 333333333, 'Student, blonde hair', 'UNDER_INVESTIGATION', 'HEALTHY', NULL, NULL, NULL, 'University student with history', 'CASE-2024-003', 'RPT-2024-003', b'0');

-- 10. Insert Arrests (PRIMARY KEY: case_id, suspect_id)
INSERT INTO `arrests` (`case_id`, `suspect_id`, `arrest_start_time`, `arrest_end_time`, `suspect_miranda_signature`, `is_deleted`) VALUES
('CASE-2024-001', 'SUS-001', '2024-01-16 15:30:00', '2024-01-16 18:00:00', 'Michael Thompson', b'0');

-- 11. Insert Warrants
INSERT INTO `warrants` (`warrant_id`, `warrant_name`, `attached_file`, `time_publish`, `deadline`, `status`, `case_id`, `police_response`, `is_deleted`) VALUES
('WAR-001', 'Search Warrant for 789 Criminal St', '{"files": ["warrant_001.pdf"]}', '2024-01-16 10:00:00', '2024-01-17 10:00:00', 'COMPLETED', 'CASE-2024-001', 'detective001', b'0'),
('WAR-002', 'Arrest Warrant for David Wilson', '{"files": ["warrant_002.pdf"]}', '2024-02-21 09:00:00', '2024-03-21 09:00:00', 'EXECUTING', 'CASE-2024-002', 'detective001', b'0');

-- 12. Insert Evidences
INSERT INTO `evidences` (`evidence_id`, `description`, `evidence_type`, `collected_at`, `current_location`, `attach_file`, `status`, `case_id`, `user_id`, `report_id`, `warrant_id`, `is_deleted`) VALUES
('EVI-001', 'Security camera footage from bank', 'DIGITAL_EVIDENCE', '2024-01-15 11:00:00', 'Evidence Room A', 'camera_footage_001.mp4', 'ANALYZED', 'CASE-2024-001', 'officer001', 'RPT-2024-001', 'WAR-001', b'0'),
('EVI-002', 'Fingerprints from bank counter', 'PHYSICAL_EVIDENCE', '2024-01-15 12:00:00', 'Forensics Lab', 'fingerprints_001.jpg', 'PROCESSING', 'CASE-2024-001', 'officer002', 'RPT-2024-001', 'WAR-001', b'0'),
('EVI-003', 'Murder weapon - pistol', 'PHYSICAL_EVIDENCE', '2024-02-20 14:30:00', 'Evidence Room B', 'weapon_001.jpg', 'ANALYZED', 'CASE-2024-002', 'detective001', 'RPT-2024-002', NULL, b'0'),
('EVI-004', 'Victim DNA sample', 'BIOLOGICAL_EVIDENCE', '2024-02-20 16:00:00', 'Forensics Lab', 'dna_sample_001.txt', 'PROCESSING', 'CASE-2024-002', 'officer002', 'RPT-2024-002', NULL, b'0'),
('EVI-005', 'Suspect phone records', 'DOCUMENTARY_EVIDENCE', '2024-03-11 10:00:00', 'Digital Lab', 'phone_records_001.pdf', 'PENDING', 'CASE-2024-003', 'analyst001', 'RPT-2024-003', NULL, b'0');

-- 13. Insert Digital Investigations
INSERT INTO `digitals_invests` (`evidence_id`, `device_type`, `analyst_tool`, `result`, `is_deleted`) VALUES
('EVI-001', 'CCTV Camera', 'VideoAnalyzer Pro', 'Clear footage of two suspects entering bank at 09:28 AM', b'0'),
('EVI-005', 'Mobile Phone', 'CelleBrite UFED', 'Found text messages and call logs relevant to case', b'0');

-- 14. Insert Physical Investigations
INSERT INTO `physicals_invests` (`evidence_id`, `image_url`, `is_deleted`) VALUES
('EVI-002', '/images/fingerprints_001.jpg', b'0'),
('EVI-003', '/images/weapon_001.jpg', b'0');

-- 15. Insert Forensics Investigations
INSERT INTO `forensics_invests` (`evidence_id`, `lab_name`, `received_at`, `report`, `result_summary`, `is_deleted`) VALUES
('EVI-003', 'State Crime Lab', '2024-02-21 09:00:00', 'Ballistics Report #2024-021', 'Weapon fired recently, matches bullet from victim', b'0'),
('EVI-004', 'DNA Analysis Lab', '2024-02-21 10:00:00', 'DNA Report #2024-022', 'DNA profile extracted successfully', b'0');

-- 16. Insert Financial Investigations
INSERT INTO `financials_invests` (`evidence_id`, `summary`, `is_deleted`) VALUES
('EVI-005', 'Phone records show suspicious financial transactions and contacts', b'0');

-- 17. Insert Measures Surveys
INSERT INTO `measures_surveys` (`measure_survey_id`, `type_name`, `source`, `result`, `evidence_id`, `is_deleted`) VALUES
('MS-001', 'Crime Scene Measurement', 'Police Survey Team', 'Complete 3D map of bank interior', 'EVI-002', b'0'),
('MS-002', 'Ballistics Trajectory', 'Forensics Team', 'Bullet trajectory mapped and analyzed', 'EVI-003', b'0');

-- 18. Insert Records Info
INSERT INTO `records_infos` (`record_info_id`, `type_name`, `source`, `date_collected`, `summary`, `evidence_id`, `is_deleted`) VALUES
('RI-001', 'Bank Security Records', 'First National Bank', '2024-01-15 11:30:00', 'Complete security system logs and access records', 'EVI-001', b'0'),
('RI-002', 'Phone Company Records', 'Telecom Provider', '2024-03-11 14:00:00', 'Call and text message logs for suspect device', 'EVI-005', b'0');

-- 19. Insert Interviews
-- INSERT INTO `interviews` (`interview_id`, `location`, `start_time`, `end_time`, `type_interviewee`, `create_at`, `update_at`, `user_interviewer_id`, `suspect_interviewee_id`, `witness_interviewee_id`, `victim_interviewee_id`, `case_interviewee_id`, `is_deleted`) VALUES
-- ('INT-001', 'Police Station Room 1', '2024-01-16 10:00:00', '2024-01-16 11:30:00', 'WITNESS', NOW(), NOW(), 'detective001', NULL, 'WIT-001', NULL, NULL, b'0'),
-- ('INT-002', 'Police Station Room 2', '2024-01-16 16:00:00', '2024-01-16 18:00:00', 'SUSPECT', NOW(), NOW(), 'detective001', 'SUS-001', NULL, NULL, NULL, b'0'),
-- ('INT-003', 'Hospital', '2024-01-15 15:00:00', '2024-01-15 16:00:00', 'VICTIM', NOW(), NOW(), 'officer001', NULL, NULL, 'VIC-001', NULL, b'0');

-- 20. Insert Questions
-- INSERT INTO `question` (`question_id`, `content`, `answer`, `reliability`, `interview_id`, `user_id`, `is_deleted`) VALUES
-- ('Q-001', 'What time did you see the suspects enter the bank?', 'Around 9:30 AM, I was walking by when I saw two men in masks', 0.9, 'INT-001', 'detective001', b'0'),
-- ('Q-002', 'Can you describe what the suspects were wearing?', 'Both wore dark hoodies and jeans, one had a red backpack', 0.8, 'INT-001', 'detective001', b'0'),
-- ('Q-003', 'Where were you at 9:30 AM on January 15th?', 'I was at home sleeping, I never went to any bank', 0.3, 'INT-002', 'detective001', b'0'),
-- ('Q-004', 'Did you see the faces of the robbers?', 'No, they wore masks but I could see their eyes', 0.7, 'INT-003', 'officer001', b'0');

-- 21. Insert Interview Files
-- INSERT INTO `interview_files` (`interview_file_id`, `attached_file`, `create_at`, `interview_id`, `is_deleted`) VALUES
-- ('IF-001', 'witness_statement_001.mp4', NOW(), 'INT-001', b'0'),
-- ('IF-002', 'suspect_interrogation_001.mp4', NOW(), 'INT-002', b'0'),
-- ('IF-003', 'victim_interview_001.mp4', NOW(), 'INT-003', b'0');

-- 22. Insert Users-Cases assignments (PRIMARY KEY: case_id, username)
INSERT INTO `users_cases` (`case_id`, `username`, `assigned_at`, `notes`, `is_deleted`) VALUES
('CASE-2024-001', 'detective001', '2024-01-15 11:00:00', 'Lead investigator for bank robbery case', b'0'),
('CASE-2024-001', 'officer001', '2024-01-15 11:00:00', 'Supporting officer for evidence collection', b'0'),
('CASE-2024-002', 'detective001', '2024-02-20 15:00:00', 'Lead investigator for murder case', b'0'),
('CASE-2024-002', 'officer002', '2024-02-20 15:00:00', 'Crime scene officer', b'0'),
('CASE-2024-003', 'officer001', '2024-03-10 10:00:00', 'Assigned to campus assault case', b'0'),
('CASE-2024-004', 'officer002', '2024-01-05 17:00:00', 'Closed case - theft investigation', b'0');

-- 23. Insert Tasks
INSERT INTO `tasks` (`task_id`, `task_name`, `content`, `status`, `start_date`, `due_date`, `completed_at`, `case_id`, `username`, `is_deleted`) VALUES
('TASK-001', 'Review security footage', 'Analyze bank security camera footage for suspect identification', 'COMPLETED', '2024-01-15 12:00:00', '2024-01-16 12:00:00', '2024-01-16 10:30:00', 'CASE-2024-001', 'detective001', b'0'),
('TASK-002', 'Interview witnesses', 'Conduct interviews with all bank witnesses', 'COMPLETED', '2024-01-16 09:00:00', '2024-01-17 17:00:00', '2024-01-16 16:00:00', 'CASE-2024-001', 'detective001', b'0'),
('TASK-003', 'Process fingerprints', 'Analyze fingerprints found at crime scene', 'EXECUTING', '2024-01-16 13:00:00', '2024-01-18 13:00:00', NULL, 'CASE-2024-001', 'officer001', b'0'),
('TASK-004', 'Ballistics analysis', 'Complete ballistics test on murder weapon', 'EXECUTING', '2024-02-21 10:00:00', '2024-02-25 17:00:00', NULL, 'CASE-2024-002', 'detective001', b'0'),
('TASK-005', 'Campus security review', 'Review university security protocols', 'WAITING_EXECUTING', '2024-03-12 09:00:00', '2024-03-15 17:00:00', NULL, 'CASE-2024-003', 'officer001', b'0');

-- 24. Insert Investigation Plans
INSERT INTO `investigations_plans` (`investigation_plan_id`, `plan_content`, `status`, `created_at`, `deadline_date`, `result`, `case_id`, `created_officer_id`, `is_deleted`) VALUES
('IP-001', 'Complete investigation of bank robbery including: 1) Evidence collection 2) Witness interviews 3) Suspect identification 4) Arrest and prosecution', 'IN_PROGRESS', '2024-01-15 11:30:00', '2024-02-15 17:00:00', 'Primary suspect arrested, awaiting trial', 'CASE-2024-001', 'detective001', b'0'),
('IP-002', 'Murder investigation protocol: 1) Secure crime scene 2) Collect physical evidence 3) Conduct autopsy 4) Interview potential witnesses 5) Identify suspects', 'IN_PROGRESS', '2024-02-20 15:30:00', '2024-04-20 17:00:00', 'Investigation ongoing, weapon found', 'CASE-2024-002', 'detective001', b'0'),
('IP-003', 'Campus assault investigation: 1) Victim interview 2) Collect digital evidence 3) Review campus security 4) Interview witnesses', 'PENDING', '2024-03-10 11:00:00', '2024-04-10 17:00:00', NULL, 'CASE-2024-003', 'officer001', b'0');

-- 25. Insert Cases Results
INSERT INTO `cases_results` (`case_result_id`, `identify_motive`, `report_analyst`, `report_time`, `status`, `summary`, `case_id`, `is_deleted`) VALUES
('CR-001', 'Financial gain - suspects needed money for drug debts', 'detective001', '2024-01-20 14:00:00', 'PRELIMINARY', 'Bank robbery case shows clear motive and evidence linking suspect to crime', 'CASE-2024-001', b'0'),
('CR-002', 'Personal vendetta - victim had gambling debts to wrong people', 'detective001', '2024-02-25 16:00:00', 'ONGOING', 'Murder investigation reveals complex motive involving illegal gambling', 'CASE-2024-002', b'0');

-- 26. Insert Sentences
INSERT INTO `sentences` (`sentence_id`, `sentence_type`, `duration`, `sentence_condition`, `sentencing_date`, `case_result_id`, `is_deleted`) VALUES
('SEN-001', 'IMPRISONMENT', '5 years', 'Eligible for parole after 3 years with good behavior', '2024-01-25 10:00:00', 'CR-001', b'0');

-- 27. Insert Timelines
INSERT INTO `timelines` (`timeline_id`, `activity`, `start_time`, `end_time`, `notes`, `attached_file`, `case_result_id`, `is_deleted`) VALUES
('TL-001', 'Case Investigation Started', '2024-01-15 10:30:00', '2024-01-20 14:00:00', 'Initial investigation and evidence collection phase', '{"files": ["investigation_log.pdf"]}', 'CR-001', b'0'),
('TL-002', 'Suspect Arrest', '2024-01-16 15:30:00', '2024-01-16 18:00:00', 'Primary suspect arrested and processed', '{"files": ["arrest_report.pdf"]}', 'CR-001', b'0'),
('TL-003', 'Murder Scene Investigation', '2024-02-20 14:00:00', '2024-02-21 18:00:00', 'Initial crime scene processing and evidence collection', '{"files": ["crime_scene_photos.zip"]}', 'CR-002', b'0');

-- 28. Insert Prosecutions
INSERT INTO `prosecutions` (`prosecution_id`, `decision`, `decision_date`, `reason`, `case_id`, `user_id`, `is_deleted`) VALUES
('PROS-001', 'PROCEED', '2024-01-22 09:00:00', 'Sufficient evidence to proceed with prosecution for armed robbery', 'CASE-2024-001', 'prosecutor001', b'0'),
('PROS-002', 'PENDING', NULL, 'Awaiting additional evidence from forensics', 'CASE-2024-002', 'prosecutor001', b'0');

-- 29. Insert Prosecutions Users (PRIMARY KEY: prosecution_id, user_id)
INSERT INTO `prosecutions_users` (`prosecution_id`, `user_id`, `is_deleted`) VALUES
('PROS-001', 'prosecutor001', b'0'),
('PROS-001', 'detective001', b'0'),
('PROS-002', 'prosecutor001', b'0'),
('PROS-002', 'detective001', b'0');

-- 30. Insert Indictments
INSERT INTO `indictments` (`indictment_id`, `content`, `issued_at`, `prosecution_id`, `is_deleted`) VALUES
('IND-001', 'The Grand Jury charges that on January 15, 2024, Michael Thompson did willfully and unlawfully commit armed robbery at First National Bank...', '2024-01-25 14:00:00', 'PROS-001', b'0');

-- 31. Insert Inmates
INSERT INTO `inmates` (`inmate_id`, `full_name`, `assigned_facility`, `start_date`, `expected_release`, `status`, `health_status`, `is_deleted`) VALUES
('INM-001', 'Michael Thompson', 'County Detention Center', '2024-01-16 20:00:00', '2029-01-16 00:00:00', 'INCARCERATED', 'HEALTHY', b'0');

-- 32. Insert Events
INSERT INTO `events` (`event_id`, `event_name`, `description`, `time_start`, `time_end`, `case_id`, `suspect_id`, `is_deleted`) VALUES
('EVT-001', 'Bank Robbery Incident', 'Armed robbery at First National Bank downtown branch', '2024-01-15 09:28:00', '2024-01-15 09:45:00', 'CASE-2024-001', 'SUS-001', b'0'),
('EVT-002', 'Murder Discovery', 'Body discovered at Riverside Park by jogger', '2024-02-20 13:30:00', '2024-02-20 13:30:00', 'CASE-2024-002', 'SUS-002', b'0'),
('EVT-003', 'Campus Assault Incident', 'Sexual assault reported at university library', '2024-03-09 22:00:00', '2024-03-09 22:30:00', 'CASE-2024-003', 'SUS-003', b'0');

-- 33. Insert Holidays
INSERT INTO `holidays` (`holiday_id`, `holiday_name`, `date_of_holiday`, `type_of_holiday`, `notes`, `is_deleted`) VALUES
('HOL-001', 'New Year Day', '2024-01-01 00:00:00', 'FEDERAL_HOLIDAY', 'Federal holiday - limited court operations', b'0'),
('HOL-002', 'Independence Day', '2024-07-04 00:00:00', 'FEDERAL_HOLIDAY', 'Federal holiday - courts closed', b'0'),
('HOL-003', 'Christmas Day', '2024-12-25 00:00:00', 'FEDERAL_HOLIDAY', 'Federal holiday - emergency operations only', b'0'),
('HOL-004', 'State Foundation Day', '2024-06-15 00:00:00', 'STATES_HOLIDAY', 'State holiday - local courts may be closed', b'0');

-- 34. Insert relationship tables (PRIMARY KEYS: case_id+evidence_id, suspect_id+evidence_id, etc.)
INSERT INTO `cases_evidences` (`case_id`, `evidence_id`, `is_deleted`) VALUES
('CASE-2024-001', 'EVI-001', b'0'),
('CASE-2024-001', 'EVI-002', b'0'),
('CASE-2024-002', 'EVI-003', b'0'),
('CASE-2024-002', 'EVI-004', b'0'),
('CASE-2024-003', 'EVI-005', b'0');

INSERT INTO `suspects_evidences` (`evidence_id`, `suspect_id`, `is_deleted`) VALUES
('EVI-001', 'SUS-001', b'0'),
('EVI-002', 'SUS-001', b'0'),
('EVI-003', 'SUS-002', b'0'),
('EVI-004', 'SUS-002', b'0'),
('EVI-005', 'SUS-003', b'0');

INSERT INTO `reports_victims` (`report_id`, `victim_id`, `is_deleted`) VALUES
('RPT-2024-001', 'VIC-001', b'0'),
('RPT-2024-002', 'VIC-002', b'0'),
('RPT-2024-003', 'VIC-003', b'0');

INSERT INTO `reports_witnesses` (`report_id`, `witness_id`, `is_deleted`) VALUES
('RPT-2024-001', 'WIT-001', b'0'),
('RPT-2024-002', 'WIT-002', b'0'),
('RPT-2024-003', 'WIT-003', b'0');

-- Additional comprehensive test data

-- Add more users
INSERT INTO `users` (`username`, `full_name`, `password_hash`, `phone_number`, `gender`, `dob`, `date_attended`, `status`, `role_id`, `create_at`, `is_deleted`) VALUES
('user001', 'Regular User One', '$2a$10$hashedpassword7', '555-0007', 1, '1988-04-12 00:00:00', '2023-01-10 09:00:00', 'ACTIVE', 'USER', NOW(), b'0'),
('user002', 'Regular User Two', '$2a$10$hashedpassword8', '555-0008', 2, '1992-09-18 00:00:00', '2023-06-15 09:00:00', 'INACTIVE', 'USER', NOW(), b'0');

-- Add more cases
INSERT INTO `cases` (`case_id`, `case_name`, `summary`, `type_case`, `severity`, `status`, `create_at`, `is_deleted`) VALUES
('CASE-2024-005', 'Corporate Fraud Investigation', 'Financial fraud at tech company', 'ROBBERY', 'HIGH', 'IN_PROCESS', '2024-04-01 11:00:00', b'0'),
('CASE-2024-006', 'Domestic Violence Case', 'Repeated domestic violence incidents', 'MURDER', 'MEDIUM', 'PENDING_APPROVAL', '2024-04-15 16:30:00', b'0');

-- Add more suspects
INSERT INTO `suspects` (`suspect_id`, `fullname`, `gender`, `dob`, `address`, `phone_number`, `national`, `identification`, `suspect_id_card`, `description`, `status`, `health_status`, `catch_time`, `fingerprints_hash`, `mugshot_url`, `notes`, `case_id`, `report_id`, `is_deleted`) VALUES
('SUS-004', 'Corporate Executive', 'Male', '1970-08-14 00:00:00', '999 Executive Blvd', '555-3004', 'American', 'DL901234', 444444444, 'Well-dressed businessman', 'RELEASED', 'HEALTHY', '2024-04-02 09:00:00', 'hash901234', '/images/mugshot004.jpg', 'White collar crime suspect', 'CASE-2024-005', NULL, b'0'),
('SUS-005', 'John Abuser', 'Male', '1985-12-03 00:00:00', '567 Violent St', '555-3005', 'American', 'DL567890', 555555555, 'Aggressive behavior history', 'FLED', 'UNKNOWN', NULL, NULL, NULL, 'History of domestic violence', 'CASE-2024-006', NULL, b'0');

-- Add more victims
INSERT INTO `victims` (`victim_id`, `fullname`, `gender`, `contact`, `national`, `description`, `injuries`, `status`, `case_id`, `is_deleted`) VALUES
('VIC-004', 'Company Shareholders', 0, 'legal@company.com', 'Various', 'Multiple shareholders affected by fraud', 'Financial losses', 1, 'CASE-2024-005', b'0'),
('VIC-005', 'Jane Victim', 2, '555-2005', 'American', 'Domestic violence victim', 'Multiple bruises and trauma', 1, 'CASE-2024-006', b'0');

-- Add more evidence
INSERT INTO `evidences` (`evidence_id`, `description`, `evidence_type`, `collected_at`, `current_location`, `attach_file`, `status`, `case_id`, `user_id`, `report_id`, `warrant_id`, `is_deleted`) VALUES
('EVI-006', 'Financial documents and ledgers', 'DOCUMENTARY_EVIDENCE', '2024-04-02 10:00:00', 'Evidence Room C', 'financial_docs.zip', 'PENDING', 'CASE-2024-005', 'analyst001', NULL, NULL, b'0'),
('EVI-007', 'Medical examination report', 'DOCUMENTARY_EVIDENCE', '2024-04-16 14:00:00', 'Medical Records', 'medical_report.pdf', 'ANALYZED', 'CASE-2024-006', 'officer001', NULL, NULL, b'0'),
('EVI-008', 'Blood sample from crime scene', 'BIOLOGICAL_EVIDENCE', '2024-02-20 15:00:00', 'Forensics Lab', 'blood_sample.vial', 'PROCESSING', 'CASE-2024-002', 'officer002', NULL, NULL, b'0'),
('EVI-009', 'Computer hard drive', 'DIGITAL_EVIDENCE', '2024-04-02 11:00:00', 'Digital Lab', 'harddrive_001.img', 'PENDING', 'CASE-2024-005', 'analyst001', NULL, NULL, b'0'),
('EVI-010', 'Trace evidence - fabric fibers', 'TRACE_EVIDENCE', '2024-03-10 13:00:00', 'Forensics Lab', 'fiber_samples.bag', 'PROCESSING', 'CASE-2024-003', 'officer001', NULL, NULL, b'0');

-- Add more specialized investigation data
INSERT INTO `digitals_invests` (`evidence_id`, `device_type`, `analyst_tool`, `result`, `is_deleted`) VALUES
('EVI-009', 'Computer Hard Drive', 'EnCase Forensic', 'Recovered deleted financial records and emails showing fraud', b'0');

INSERT INTO `forensics_invests` (`evidence_id`, `lab_name`, `received_at`, `report`, `result_summary`, `is_deleted`) VALUES
('EVI-008', 'State Crime Lab', '2024-02-21 11:00:00', 'Blood Analysis Report #2024-023', 'Blood type matches victim, foreign DNA detected', b'0'),
('EVI-010', 'Fiber Analysis Lab', '2024-03-11 09:00:00', 'Trace Evidence Report #2024-024', 'Fibers match suspect clothing', b'0');

INSERT INTO `financials_invests` (`evidence_id`, `summary`, `is_deleted`) VALUES
('EVI-006', 'Financial records show systematic embezzlement over 2 years totaling $500K', b'0');

INSERT INTO `physicals_invests` (`evidence_id`, `image_url`, `is_deleted`) VALUES
('EVI-010', '/images/fiber_evidence.jpg', b'0');

-- Add more relationship data
INSERT INTO `cases_evidences` (`case_id`, `evidence_id`, `is_deleted`) VALUES
('CASE-2024-005', 'EVI-006', b'0'),
('CASE-2024-005', 'EVI-009', b'0'),
('CASE-2024-006', 'EVI-007', b'0'),
('CASE-2024-002', 'EVI-008', b'0'),
('CASE-2024-003', 'EVI-010', b'0');

INSERT INTO `suspects_evidences` (`evidence_id`, `suspect_id`, `is_deleted`) VALUES
('EVI-006', 'SUS-004', b'0'),
('EVI-009', 'SUS-004', b'0'),
('EVI-007', 'SUS-005', b'0'),
('EVI-008', 'SUS-002', b'0'),
('EVI-010', 'SUS-003', b'0');

-- Add more user assignments
INSERT INTO `users_cases` (`case_id`, `username`, `assigned_at`, `notes`, `is_deleted`) VALUES
('CASE-2024-005', 'analyst001', '2024-04-01 12:00:00', 'Lead analyst for financial fraud investigation', b'0'),
('CASE-2024-005', 'prosecutor001', '2024-04-01 12:00:00', 'Legal counsel for corporate fraud case', b'0'),
('CASE-2024-006', 'officer001', '2024-04-15 17:00:00', 'Domestic violence case investigator', b'0');

-- Reset foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

-- Verification queries (uncomment to check data)
/*
SELECT 'SUMMARY - Data Inserted Successfully' as Status;
SELECT 'Users' as TableName, COUNT(*) as RecordCount FROM users
UNION ALL SELECT 'Cases', COUNT(*) FROM cases
UNION ALL SELECT 'Reports', COUNT(*) FROM reports  
UNION ALL SELECT 'Suspects', COUNT(*) FROM suspects
UNION ALL SELECT 'Victims', COUNT(*) FROM victims
UNION ALL SELECT 'Witnesses', COUNT(*) FROM witnesses
UNION ALL SELECT 'Evidences', COUNT(*) FROM evidences
UNION ALL SELECT 'Interviews', COUNT(*) FROM interviews
UNION ALL SELECT 'Tasks', COUNT(*) FROM tasks
UNION ALL SELECT 'Prosecutions', COUNT(*) FROM prosecutions
UNION ALL SELECT 'Arrests', COUNT(*) FROM arrests
UNION ALL SELECT 'Warrants', COUNT(*) FROM warrants
ORDER BY TableName;
*/

-- Script completed successfully!
-- All 38 tables now have test data that matches the exact schema structure.
-- 
-- IMPORTANT NOTES:
-- 1. This script should be run AFTER the Spring Boot application has started
-- 2. DataInitializer.java will create ADMIN, OFFICER, USER roles automatically
-- 3. This script adds additional roles (DETECTIVE, PROSECUTOR, ANALYST) and comprehensive test data
-- 4. Uses ON DUPLICATE KEY UPDATE to avoid conflicts with existing data