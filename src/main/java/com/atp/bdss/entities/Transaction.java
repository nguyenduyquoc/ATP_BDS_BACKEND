package com.atp.bdss.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
    String id;

    @Column(name = "user_id", nullable = false)
    String userId;

    @Column(name = "land_id", nullable = false)
    String landId;

    @Column(name = "status", nullable = false)
    String status;

    @Column(name = "stk")
    String stk;

    @Column(name = "bank_name")
    String bankName;

    @Column(name = "otp")
    String otp;

    @Column(name = "created_at", nullable = false)
    String createdAt;

    @Column(name = "updated_at")
    String updateAt;

    @Column(name = "is_deleted")
    short isDeleted;
}
