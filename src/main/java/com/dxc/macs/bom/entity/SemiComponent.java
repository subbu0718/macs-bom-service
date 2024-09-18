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
@Table(name = "semi_component")
public class SemiComponent {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "record_id")
    private String recordId;

    @Column(name = "node_id")
    private Long nodeId;

    @Column(name = "article_name")
    private String articleName;

    @Column(name = "item_no")
    private String itemNo;

    @Column(name = "specific_weight")
    private BigDecimal specificWeight;

    @Column(name = "specific_weight_type")
    private int specificWeightType;

    @Column(name = "preliminary", nullable = false, columnDefinition = "TINYINT", length = 1)
    private Boolean preliminary;

    @Column(name = "version")
    private BigDecimal version;
}
