package com.group3.MockProject.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
public class Case{
    @Id
    private String caseId;

    @NotNull
    private String caseName;

    @NotNull
    private String typeCase;

    @NotNull
    private String severity;

    @NotNull
    private String status;

    @NotNull
    @Column(columnDefinition = "MEDIUMTEXT")
    private String summary;

    @NotNull
    private LocalDateTime create_at;
    @NotNull
    @ColumnDefault("false")
    private Boolean is_deleted;

    @OneToMany
    private Set<Warrant> warrants;
}
