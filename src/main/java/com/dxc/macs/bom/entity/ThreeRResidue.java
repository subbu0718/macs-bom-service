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
@Table(name = "3r_residue")
public class ThreeRResidue {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "bom_model_id")
    private int bomModelId;

    @Column(name = "proventech_id")
    private int proventechId;

    @Column(name = "weight")
    private BigDecimal weight;

    @Column(name = "unit_of_measure")
    private String unitOfMeasure;
}
