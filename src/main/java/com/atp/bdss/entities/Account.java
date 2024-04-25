package com.atp.bdss.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Account implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", unique = true, nullable = false)
    String id;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "email", unique = true)
    String email;

    @Column(name = "phone_number", unique = true)
    String phone;

    @Column(name = "password")
    String password;

    @Column(name = "created_at", updatable = false)
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @Column(name = "is_deleted")
    Short isDeleted;

    @Column(name = "otp")
    String otp;

    @ManyToOne()
    @JoinColumn(name = "role_id")
    Role role;

    @OneToMany(mappedBy = "userId" , fetch = FetchType.LAZY)
    List<Transaction> transactions;

    public Account(String id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public Account(String id, String name, String email, String phone, List<Transaction> transactions) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.transactions = transactions;
    }
}
