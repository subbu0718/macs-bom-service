package com.dxc.macs.bom.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "3r_product")
public class ThreeRProduct {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "bom_model_id")
    private int bomModelId;

    @Column(name = "part_number")
    private String partNumber;

    @Column(name = "is_excluded", nullable = true, columnDefinition = "TINYINT", length = 1)
    private Boolean isExcluded;

    @Column(name = "weight")
    private BigDecimal weight;

    @Column(name = "unit_of_measure")
    private String unitOfMeasure;

    @Column(name = "quantity")
    private BigDecimal quantity;

    @Column(name = "reference_part_number")
    private String referencePartNumber;

    @Column(name = "pretreatment", nullable = true, columnDefinition = "TINYINT", length = 1)
    private Boolean pretreatment;

    @Column(name = "dism", nullable = true, columnDefinition = "TINYINT", length = 1)
    private Boolean dism;

    @Column(name = "status")
    private String status;

    @Column(name = "proven_dismounting_id")
    private Integer provenDismountingId;

    @Column(name = "top_level_node_id")
    private Long topLevelNodeId;

    @Column(name = "version")
    private BigDecimal version;
}
