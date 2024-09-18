package com.dxc.macs.bom.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "recipient_spec")
public class RecipientSpec {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "record_id")
    private String recordIdentifier;
    @Column(name = "top_level_node_id")
    private Long topLevelNodeId;
    @Column(name = "org_unit_id")
    private Long orgUnitId;
    @Column(name = "product_name")
    private String productName;
    @Column(name = "report_no")
    private String reportNumber;
    @Column(name = "part_item_no")
    private String partNumber;
    @Column(name = "purchase_order_no")
    private String purchaseOrderNumber;
    @Column(name = "bill_of_delivery_no")
    private String billOfDeliveryNo;
    @Column(name = "information_for_mds_supplier")
    private String infoForMdsSupplier;
    @Column(name = "last_change_date")
    private Date lastChangeDate;
    @Column(name = "supplier_code")
    private String supplierCode;
    @Column(name = "drawing_no")
    private String drawingNumber;
    @Column(name = "drawing_dated")
    private String drawingDate;
    @Column(name = "drawing_change_level")
    private String drawingChangeLevel;
    @Column(name = "report_date")
    private Date reportDate;
    @Column(name = "recipient_status_change_date")
    private Date recipientStatusChangeDate;
    @Column(name = "recipient_status_change_time")
    private Time recipientStatusChangeTime;
    @Column(name = "recipient_status")
    private String recipientStatus;
    @Column(name = "transmission_date")
    private Date transmissionDate;
    @Column(name = "forwarding_allowed")
    private int forwardAllowed;
    @Column(name = "spare_part")
    private int sparePart;
    @Column(name = "version")
    private BigDecimal version;
}
