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
@Table(name = "material")
public class Material {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "record_id")
    private String recordId;

    @Column(name = "node_id")
    private Long nodeId;

    @Column(name = "supplier")
    private String supplier;

    @Column(name = "material_classification_code")
    private String materialClassificationCode;

    @Column(name = "supplier_material_no")
    private String supplierMaterialNo;

    @Column(name = "chemical_symbol")
    private String chemicalSymbol;

    @Column(name = "standard_material_no")
    private String standardMaterialNo;

    @Column(name = "preliminary", nullable = false, columnDefinition = "TINYINT", length = 1)
    private Boolean preliminary;

    @Column(name = "version")
    private BigDecimal version;
}
