package com.atp.bdss.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "district")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class District {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "name", nullable = false)
    String name;

    @ManyToOne
    @JoinColumn(name = "province_id")
    Province province;

    @OneToMany(mappedBy = "district" , fetch = FetchType.LAZY)
    List<Project> projects;

    public District(int id, String name, Province province) {
        this.id = id;
        this.name = name;
        this.province = province;
    }
}
