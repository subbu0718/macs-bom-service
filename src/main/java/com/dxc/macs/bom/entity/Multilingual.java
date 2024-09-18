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
@Table(name = "multilingual")
public class Multilingual {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "record_id")
    private String recordId;
    @Column(name = "iso_language")
    private String isoLanguage;
    @Column(name = "name")
    private String name;
    @Column(name = "trade_name")
    private String tradeName;
    @Column(name = "remark")
    private String remark;
    @Column(name = "node_id")
    private Long nodeId;
    @Column(name = "version")
    private BigDecimal version;
}
