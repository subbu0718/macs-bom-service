package com.dxc.macs.bom.entity;

import javax.persistence.*;
import lombok.*;

import java.math.BigDecimal;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "component")
public class Component {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "record_id")
    private String recordId;

    @Column(name = "node_id")
    private Long nodeId;

    @Column(name = "description")
    private String description;

    @Column(name = "tolerance")
    private BigDecimal tolerance;

    @Column(name = "measured_weight")
    private BigDecimal measuredWeight;

    @Column(name = "calculated_weight")
    private BigDecimal calculatedWeight;

    @Column(name = "weighted_unit")
    private String weightedUnit;

    @Column(name = "part_item_no")
    private String partItemNo;

    @Column(name = "polymeric_parts_marking")
    private int polymericPartsMarking;

    @Column(name = "preliminary", nullable = false, columnDefinition = "TINYINT", length = 1)
    private Boolean preliminary;

    @Column(name = "multi_source", nullable = false, columnDefinition = "TINYINT", length = 1)
    private Boolean multiSource;

    @Column(name = "production_eu")
    private int productionEU;

    @Column(name = "scip_no")
    private String scipNo;

    @Column(name = "scip_submission_no")
    private String scipSubmissionNo;

    @Column(name = "automatic_scip", nullable = false, columnDefinition = "TINYINT", length = 1)
    private Boolean automaticScip;

    @Column(name = "version")
    private BigDecimal version;
}
