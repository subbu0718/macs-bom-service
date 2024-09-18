package com.dxc.macs.bom.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigInteger;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "classification_name")
public class ClassificationName {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "record_id")
    private String recordId;
    @Column(name = "material_classification_code")
    private String materialClassificationCode;
    @Column(name = "material_classification_name")
    private String materialClassificationName;
    @Column(name = "iso_language")
    private String isoLanguage;
}
