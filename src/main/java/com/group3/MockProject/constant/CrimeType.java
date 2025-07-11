package com.group3.MockProject.constant;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * CrimeType
 * <p>
 * Provides business logic for managing employment details.
 * <p>
 * Version 1.0
 * Date: 07/07/2025
 * <p>
 * Copyright
 * <p>
 * Modification Logs:
 * DATE         AUTHOR       DESCRIPTION
 * -------------------------------------
 * 07/07/2025   Hải Đăng      Create
 */
public enum CrimeType {
    CRIMES_AGAINST_PERSONS("Crimes Against Persons"),
    CRIME_AGAINST_PROPERTY("Crime Against Property"),
    WHITE_COLLAR_CRIMES("White-Collar Crimes"),
    CYBER_CRIMES("Cyber Crimes"),
    DRUG_RELATED_CRIMES("Drug-related Crimes"),
    PUBLIC_ORDER_CRIMES("Public Order Crimes");

    private final String label;

    CrimeType(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }
}
