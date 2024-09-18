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
@Table(name = "bom_model")
public class BomModel {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "product_number")
    private String productNumber;
    @Column(name = "description")
    private String description;
    @Column(name = "market")
    private String market;
    @Column(name = "release_number")
    private String releaseNumber;
    @Column(name = "short_description")
    private String shortDescription;
    @Column(name = "long_description")
    private String longDescription;
    @Column(name = "status")
    private String status;
    @Column(name = "business_unit_id")
    private int businessUnitId;
    @Column(name = "taric_code")
    private String taricCode;
    @Column(name = "taric_code_components")
    private String taricForComponents;
    @Column(name = "dossier_number")
    private String dossierNumber;
    @Column(name = "threer_status")
    private String threeRStatus;
    @Column(name = "plant")
    private String plant;
    @Column(name= "created_by")
    private String created_by;
    @Column(name= "updated_by")
    private String updated_by;
    @Column(name= "measured_weight")
    private BigDecimal measuredWeight;
    @Column(name= "weighted_unit")
    private String measuredUnit;
    @Column(name= "created_imds_id")
    private Long createdImdsId;
    @Column(name= "sent_flag")
    private String sentFlag;

    @Column(name= "cdx_job_id")
    private Integer cdxJobId;
    @Column(name= "echa_id")
    private Long echaId;
    @Column(name= "scip_number")
    private String scipNumber;
    @Column(name= "echa_url")
    private String echaUrl;
    @Column(name= "scip_validation_message")
    private String scipValidationMessage;
    @Column(name= "regulation")
    private String regulation;
}
