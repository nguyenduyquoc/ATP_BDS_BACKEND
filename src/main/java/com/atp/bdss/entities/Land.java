package com.atp.bdss.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
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
    BigDecimal price;

    @Column(name = "deposit")
    BigDecimal deposit;

    @Column(name = "acreage")
    long acreage;

    @Column(name = "type_of_apartment")
    Short typeOfApartment;

    @Column(name = "direction")
    Short direction;

    @ManyToOne
    @JoinColumn(name = "area_id")
    Area area;

    @OneToMany(mappedBy = "land" , fetch = FetchType.LAZY)
    List<Image> images;

    public Land(String id, String name, String description, String thumbnail, String address, short status, BigDecimal price, BigDecimal deposit, long acreage, short typeOfApartment, short direction) {
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
    }
}
