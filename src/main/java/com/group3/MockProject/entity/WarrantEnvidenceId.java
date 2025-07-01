package com.group3.MockProject.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarrantEnvidenceId implements Serializable {
    String warrantId;
    String evidenceId;
}
