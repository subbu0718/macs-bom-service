package com.dxc.macs.bom.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "bom_3r_material")
public class Bom3RMaterial {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "bom_model_id")
    private int bomModelId;

    @Column(name = "part_number")
    private String partNumber;

    @Column(name = "node_id")
    private Long nodeId;

    @Column(name = "weight")
    private BigDecimal weight;

    @Column(name = "unit_of_measure")
    private String unitOfMeasure;

    @Column(name = "no_of_components_attached")
    private BigDecimal noOfComponentsAttached;
}
