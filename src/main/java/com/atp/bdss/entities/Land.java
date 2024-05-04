package com.atp.bdss.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Entity
@Table(name = "land")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Land {
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
    short status;

    @Column(name = "price")
    String price;

    @Column(name = "deposit")
    String deposit;

    @Column(name = "acreage")
    long acreage;

    @Column(name = "type_of_apartment")
    String typeOfApartment;

    @Column(name = "direction")
    String direction;

    @ManyToOne
    @JoinColumn(name = "area_id")
    Area area;

    @OneToMany(mappedBy = "land" , fetch = FetchType.LAZY)
    List<Image> images;

    public Land(String id, String name, String description, String thumbnail, String address, short status, String price, String deposit, long acreage, String typeOfApartment, String direction, Area area) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
        this.address = address;
        this.status = status;
        this.price = price;
        this.deposit = deposit;
        this.acreage = acreage;
        this.typeOfApartment = typeOfApartment;
        this.direction = direction;
        this.area = area;
    }

    public Land(String typeOfApartment) {
        this.typeOfApartment = typeOfApartment;
    }

    public Land(String id, String direction) {
        this.id = id;
        this.direction = direction;
    }
}
