package com.dxc.macs.bom.entity;

import lombok.*;

import javax.persistence.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "substance_application")
public class SubstanceApplication {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "record_id")
    private String recordId;
    @Column(name = "substance_application_id")
    private Long substanceApplicationId;
    @Column(name = "substance_application_text")
    private String substanceApplicationText;
    @Column(name = "deleted")
    private int deleted;
}