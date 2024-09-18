package com.dxc.macs.bom.entity;

import lombok.*;

import javax.persistence.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "master_part_supplier_data")
public class Masterpartsupplierdata {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name= "part_number_code")
    private String partnumbercode;

    @Column(name= "supplier_code")
    private String supplier_code;
}
