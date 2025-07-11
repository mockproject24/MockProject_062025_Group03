package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

import java.io.Serializable;

@Entity
@Table(name = "prosecutions_users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProsecutionsUser implements Serializable {

    @EmbeddedId
    ProsecutionsUserId id;

    @ManyToOne
    @JoinColumn(name = "prosecution_id")
    @MapsId("prosecutionId")
    Prosecution prosecution;

    @ManyToOne
    @JoinColumn(name = "username")
    @MapsId("username")
    User user;

    @Column(name = "is_deleted")
    @ColumnDefault("false")
    boolean isDeleted = false;
}
