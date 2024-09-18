package com.dxc.macs.bom.entity;

import javax.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "rel_weight")
public class RelWeight {
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
    @Column(name = "weight")
    private BigDecimal weight;
    @Column(name = "weight_unit")
    private String weightUnit;
    @Column(name = "rw_sequence")
    private int sequence;
    @Column(name = "applied_to_article_as")
    private String appliedToArticle;
    @Column(name = "version")
    private BigDecimal version;
}
