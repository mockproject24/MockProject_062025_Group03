package com.group3.MockProject.entity;

import jakarta.persistence.*;
import lombok.*;
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
    @JoinColumn(name = "user_id")
    @MapsId("userId")
    User user;

    @Column(name = "is_deleted")
    @ColumnDefault("false")
    boolean isDeleted = false;
}
