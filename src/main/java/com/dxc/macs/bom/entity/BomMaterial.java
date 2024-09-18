package com.dxc.macs.bom.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigInteger;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "bom_material")
public class BomMaterial {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "bom_model_product_number")
    private String productNumber;
    @Column(name = "bom_model_release_number")
    private String releaseNumber;
    @Column(name = "bom_part_number")
    private String partNumber;
    @Column(name = "ra_node_id")
    private Long raNodeId;
    @Column(name = "ra_flag")
    private String raFlag;
}
