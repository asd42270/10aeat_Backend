package com.final_10aeat.domain.office.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@Table(name = "office")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Office {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(name = "office_name")
    private String officeName;

    @Column(unique = true)
    private String address;

    @Setter
    @Column(name = "map_x")
    private Double mapX;

    @Setter
    @Column(name = "map_y")
    private Double mapY;
}
