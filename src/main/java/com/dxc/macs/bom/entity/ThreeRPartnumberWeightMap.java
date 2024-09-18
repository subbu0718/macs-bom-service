package com.dxc.macs.bom.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "3r_partnumber_weight_map")
public class ThreeRPartnumberWeightMap {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "part_number")
    private String partNumber;

    @Column(name = "weight")
    private BigDecimal weight;

    @Column(name = "unit_of_measure")
    private String unitOfMeasure;

    @Column(name = "reference_part_number")
    private String referencePartNumber;

    @Column(name = "pretreatment", nullable = true, columnDefinition = "TINYINT", length = 1)
    private Boolean pretreatment;

    @Column(name = "dism", nullable = true, columnDefinition = "TINYINT", length = 1)
    private Boolean dism;

    @Column(name="pre_treatment_iso_type")
    private Integer pre_treatment_iso_type;

    @Column(name = "proven_dismounting_id")
    private Integer provenDismountingId;

    @Column(name = "is_excluded", nullable = true, columnDefinition = "TINYINT", length = 1)
    private Boolean isExcluded;
}
