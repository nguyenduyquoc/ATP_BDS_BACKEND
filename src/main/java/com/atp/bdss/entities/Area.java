package com.atp.bdss.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Entity
@Table(name = "area")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Area {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", unique = true, nullable = false)
    String id;

    @Column(name = "name")
    String name;

    @OneToMany(mappedBy = "area" , fetch = FetchType.LAZY)
    List<Land> lands;

    @ManyToOne
    @JoinColumn(name = "project_id")
    Project project;


    public Area(String id, String name, List<Land> lands) {
        this.id = id;
        this.name = name;
        this.lands = lands;
    }

    public Area(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Area(String id, String name, Project project) {
        this.id = id;
        this.name = name;
        this.project = project;
    }

}

