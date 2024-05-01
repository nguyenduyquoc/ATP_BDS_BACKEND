package com.atp.bdss.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "project")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Project {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", unique = true, nullable = false)
    String id;

    @Column(name = "name")
    String name;

    @Column(name = "description")
    String description;

    @Column(name = "thumbnail")
    String thumbnail;

    @Column(name = "address")
    String address;

    @Column(name = "status")
    Short status;

    @Column(name = "start_date")
    LocalDate startDate;

    @Column(name = "end_date")
    LocalDate endDate;

    @Column(name = "qr_img")
    String qrImg;

    @Column(name = "bank_number")
    String bankNumber;

    @Column(name = "bank_name")
    String bankName;

    @Column(name = "host_bank")
    String hostBank;

    @Column(name = "investor")
    String investor;

    @Column(name = "investor_phone")
    String investorPhone;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @Column(name = "is_deleted")
    Short isDeleted;

    @ManyToOne()
    @JoinColumn(name = "type_id")
    ProjectType projectType;

    @OneToMany(mappedBy = "project" , fetch = FetchType.LAZY)
    List<Area> areas;

    @ManyToOne()
    @JoinColumn(name = "district_id")
    District district;

    public Project(String id, String name, String description, String thumbnail, String address, Short status,
                   LocalDate startDate, LocalDate endDate, String qrImg, String bankNumber, String bankName,
                   String hostBank, String investor, String investorPhone, ProjectType projectType, District district) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
        this.address = address;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.qrImg = qrImg;
        this.bankNumber = bankNumber;
        this.bankName = bankName;
        this.hostBank = hostBank;
        this.investor = investor;
        this.investorPhone = investorPhone;
        this.projectType = projectType;
        this.district = district;
    }
}

