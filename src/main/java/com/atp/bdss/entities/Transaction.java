package com.atp.bdss.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Table(name = "transaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Transaction {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", unique = true, nullable = false)
    String id;

    @Column(name = "user_id", nullable = false)
    String userId;

    @Column(name = "land_id", nullable = false)
    String landId;

    @Column(name = "status", nullable = false)
    short status;

    @Column(name = "code", nullable = false, unique = true)
    String code;


    @Column(name = "created_at", nullable = false)
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updateAt;

    @Column(name = "is_deleted")
    short isDeleted;

    public Transaction(String id, String userId, String landId, short status, LocalDateTime createdAt, LocalDateTime updateAt) {
        this.id = id;
        this.userId = userId;
        this.landId = landId;
        this.status = status;
        this.createdAt = createdAt;
        this.updateAt = updateAt;
    }
}
