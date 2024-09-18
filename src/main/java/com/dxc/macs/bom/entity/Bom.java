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
@Table(name = "bom")
public class Bom {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "part_number")
    private String partNumber;
    @Column(name = "description")
    private String description;
    @Column(name = "quantity")
    private BigDecimal quantity;
    @Column(name = "weight")
    private BigDecimal weight;
    @Column(name = "unit_of_measure")
    private String unitOfMeasure;
    @Column(name = "morb")
    private String morb;
    @Column(name = "supplier_code")
    private String supplierCode;
    @Column(name = "supplier_description")
    private String supplierDescription;
    @Column(name = "reference_partnumber")
    private String referencePartNumber;
    @Column(name = "bom_model_product_number")
    private String productNumber;
    @Column(name = "bom_model_release_number")
    private String releaseNumber;
    @Column(name = "mds_type")
    private String mdsType;
    @Column(name = "mds_node_id")
    private Long mdsNodeId;
    @Column(name = "preliminary")
    private String preliminary;
    @Column(name = "version")
    private BigDecimal version;
    @Column(name = "reference_flag")
    private String referenceFlag;

}
