package com.dxc.macs.bom.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "substance")
public class Substance {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "record_id")
    private String recordId;
    @Column(name = "node_id")
    private Long nodeId;
    @Column(name = "cas_code")
    private String casCode;
    @Column(name = "eu_index")
    private String euIndex;
    @Column(name = "einecs")
    private String einecs;
    @Column(name = "gadsl_duty_to_declare", nullable = false, columnDefinition = "TINYINT", length = 1)
    private int gadslDutyToDeclare;
    @Column(name = "gadsl_is_unwanted", nullable = false, columnDefinition = "TINYINT", length = 1)
    private int gadslIsUnwanted;
    @Column(name = "gadsl_is_prohibited", nullable = false, columnDefinition = "TINYINT", length = 1)
    private int gadslIsProhibited;
    @Column(name = "reach", nullable = false, columnDefinition = "TINYINT", length = 1)
    private int reach;
    @Column(name = "deleted", nullable = false, columnDefinition = "TINYINT", length = 1)
    private int deleted;
    @Column(name = "hidden", nullable = false, columnDefinition = "TINYINT", length = 1)
    private int hidden;
    @Column(name = "sunset_date")
    private Date sunsetDate;
    @Column(name = "last_application_date")
    private Date lastApplicationDate;
}