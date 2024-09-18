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
@Table(name = "material_classification")
public class MaterialClassification {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "record_id")
    private String recordId;
    @Column(name = "material_classification_code")
    private String materialClassificationCode;
    @Column(name = "leaf", nullable = false, columnDefinition = "TINYINT", length = 1)
    private Boolean leaf;
}
