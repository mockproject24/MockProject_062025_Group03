package com.group3.MockProject.entity;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class WitnessInterviewId implements Serializable {
    private String witnessId;
    private String interviewId;
}