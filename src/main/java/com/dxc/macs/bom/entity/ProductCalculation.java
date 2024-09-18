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
@Table(name = "product_calculation")
public class ProductCalculation {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "bom_model_id")
    private int bomModelId;

    @Column(name = "recyclability")
    private BigDecimal recyclability;

    @Column(name = "recoverability")
    private BigDecimal recoverability;

    @Column(name = "status")
    private String status;

    @Column(name = "total_weight")
    private BigDecimal totalWeight;

    @Column(name = "mp")
    private BigDecimal mp;

    @Column(name = "md")
    private BigDecimal md;

    @Column(name = "mm")
    private BigDecimal mm;

    @Column(name = "mtr")
    private BigDecimal mtr;

    @Column(name = "mte")
    private BigDecimal mte;

    @Column(name = "mv")
    private BigDecimal mv;
}
