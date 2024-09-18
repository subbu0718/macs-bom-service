package com.dxc.macs.bom.model;

import java.math.BigDecimal;
import java.util.Date;

public class BomPartNumber {

    private int id;
    private String partNumber;
    private String description;
    private BigDecimal quantity;
    private String mdsStatus;
    private String morb;
    private BigDecimal weight;
    private String unitOfMeasure;
    private String supplierCode;
    private String supplierDescription;
    private String productNumber;
    private String releaseNumber;
    private String mdsType;
    private Long mdsNodeId;
    private String preliminary;
    private String refPartNumber;
    private BigDecimal version;

    private String macsPartNumber;
    private String certAttached;
    private String referenceFlag;
    private Boolean supplierEmailStatus;
    private String notifiedStatus;
    private Date notifiedDate;
    private Integer notifiedCount;

    public BigDecimal getVersion() {
        return version;
    }

    public void setVersion(BigDecimal version) {
        this.version = version;
    }

    public String getPreliminary() {
        return preliminary;
    }

    public void setPreliminary(String preliminary) {
        this.preliminary = preliminary;
    }

    public Long getMdsNodeId() {
        return mdsNodeId;
    }

    public void setMdsNodeId(Long mdsNodeId) {
        this.mdsNodeId = mdsNodeId;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getSupplierDescription() {
        return supplierDescription;
    }

    public void setSupplierDescription(String supplierDescription) {
        this.supplierDescription = supplierDescription;
    }

    public String getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    public String getReleaseNumber() {
        return releaseNumber;
    }

    public void setReleaseNumber(String releaseNumber) {
        this.releaseNumber = releaseNumber;
    }

    public String getMdsType() {
        return mdsType;
    }

    public void setMdsType(String mdsType) {
        this.mdsType = mdsType;
    }

    public String getMorb() {
        return morb;
    }

    public void setMorb(String morb) {
        this.morb = morb;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getMdsStatus() {
        return mdsStatus;
    }

    public void setMdsStatus(String mdsStatus) {
        this.mdsStatus = mdsStatus;
    }

    public String getRefPartNumber() {
        return refPartNumber;
    }

    public void setRefPartNumber(String refPartNumber) {
        this.refPartNumber = refPartNumber;
    }

    public String getMacsPartNumber() {
        return macsPartNumber;
    }

    public void setMacsPartNumber(String macsPartNumber) {
        this.macsPartNumber = macsPartNumber;
    }

    public String getCertAttached() {
        return certAttached;
    }

    public void setCertAttached(String certAttached) {
        this.certAttached = certAttached;
    }

    public String getReferenceFlag() {
        return referenceFlag;
    }

    public void setReferenceFlag(String referenceFlag) {
        this.referenceFlag = referenceFlag;
    }

    public Boolean getSupplierEmailStatus() {
        return supplierEmailStatus;
    }

    public void setSupplierEmailStatus(Boolean supplierEmailStatus) {
        this.supplierEmailStatus = supplierEmailStatus;
    }

    public String getNotifiedStatus() {
        return notifiedStatus;
    }

    public void setNotifiedStatus(String notifiedStatus) {
        this.notifiedStatus = notifiedStatus;
    }

    public Date getNotifiedDate() {
        return notifiedDate;
    }

    public void setNotifiedDate(Date notifiedDate) {
        this.notifiedDate = notifiedDate;
    }

    public Integer getNotifiedCount() {
        return notifiedCount;
    }

    public void setNotifiedCount(Integer notifiedCount) {
        this.notifiedCount = notifiedCount;
    }
}
