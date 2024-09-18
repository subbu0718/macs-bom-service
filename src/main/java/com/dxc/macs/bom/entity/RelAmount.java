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
@Table(name = "rel_amount")
public class RelAmount {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "record_id")
    private String recordId;
    @Column(name = "node_id")
    private Long nodeId;
    @Column(name = "no_of_components_attached")
    private BigDecimal noOfComponentsAttached;
    @Column(name = "ra_sequence")
    private int sequence;
    @Column(name = "immediate_parent_node_id")
    private Long immediateParentNodeId;
    @Column(name = "version")
    private BigDecimal version;
}