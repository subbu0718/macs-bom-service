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
@Table(name = "rel_percentage")
public class RelPercentage {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "record_id")
    private String recordId;
    @Column(name = "immediate_parent_node_id")
    private Long immediateParentNodeId;
    @Column(name = "node_id")
    private Long nodeId;
    @Column(name = "ingredient_value")
    private BigDecimal ingredientValue;
    @Column(name = "ingredient_min_percentage")
    private BigDecimal ingredientMinPercentage;
    @Column(name = "ingredient_max_percentage")
    private BigDecimal ingredientMaxPercentage;
    @Column(name = "ingredient_type")
    private int ingredientType;
    @Column(name = "confidential", nullable = false, columnDefinition = "TINYINT", length = 1)
    private Boolean confidential;
    @Column(name = "rp_sequence")
    private int sequence;
    @Column(name = "application_id")
    private Long applicationId;
    @Column(name = "node_id_of_parent_component")
    private Long nodeIdOfParentComponent;
    @Column(name = "process_chemical")
    private int processChemical;
    @Column(name = "substance_of_high_concern")
    private int substanceOfHighConcern;
    @Column(name = "filler")
    private int filler;
    @Column(name = "version")
    private BigDecimal version;
}