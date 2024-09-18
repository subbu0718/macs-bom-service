package com.dxc.macs.bom.entity;

import lombok.*;

import javax.persistence.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "synonym")
public class Synonym {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "record_id")
    private String recordId;
    @Column(name = "substance_node_id")
    private Long substanceNodeId;
    @Column(name = "synonym_id")
    private int synonymId;
    @Column(name = "iso_language")
    private String isoLanguage;
    @Column(name = "synonym_name")
    private String synonymName;
}