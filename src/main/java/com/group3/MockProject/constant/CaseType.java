package com.group3.MockProject.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CaseType {
    ROBBERY("Robbery Case"),
    MURDER("Murder Case"),
    ASSAULT("Assault Case"),
    FRAUD("Fraud Case"),
    DRUG("Drug-Related Case"),
    KIDNAPPING("Kidnapping Case"),
    VANDALISM("Vandalism Case"),
    THEFT("Theft Case"),
    CYBERCRIME("Cybercrime Case"),
    PUBLIC_DISTURBANCE("Public Disturbance Case"),
    EMBEZZLEMENT("Embezzlement Case");

    private final String label;
}
