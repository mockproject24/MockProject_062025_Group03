package com.group3.MockProject.entity;

import com.group3.MockProject.constant.GenderType;
import com.group3.MockProject.constant.UserStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;

@Entity
@Table(name = "victims")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Victim {
    @Id
    @Column(name = "victim_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    String victimId;

    @Column(name = "fullname")
    String fullname;

    @Column(name = "victim_id_card", nullable = false, unique = true)
    Long victimIdCard; // add unique column for victim

    @Column(name = "contact")
    String contact;

    @Column(name="national")
    String national;

    @Column(name="gender")
    GenderType gender;

    @Column(name="description", columnDefinition = "MEDIUMTEXT")
    String description;

    @Column(name = "injuries",columnDefinition = "MEDIUMTEXT")
    String injuries;

    @Column(name = "status")
    UserStatus status;

    @Column(name = "is_deleted")
    @ColumnDefault("false")
    boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "case_id")
    Case caseEntity;

    @OneToMany(mappedBy = "victimInterviewee")
    List<Interview> victimInterviews;

    @OneToMany(mappedBy = "victim")
    List<ReportsVictims> reportsVictims;

    @OneToMany(mappedBy = "victim")
    List<Statement> statements;
}
