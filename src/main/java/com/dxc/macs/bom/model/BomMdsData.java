package com.dxc.macs.bom.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class BomMdsData {
    public BigInteger getMdsId() {
        return mdsId;
    }

    public void setMdsId(BigInteger mdsId) {
        this.mdsId = mdsId;
    }

    public BigDecimal getMdsVersion() {
        return mdsVersion;
    }

    public void setMdsVersion(BigDecimal mdsVersion) {
        this.mdsVersion = mdsVersion;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public Date getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(Date approvedDate) {
        this.approvedDate = approvedDate;
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

    public BigDecimal getPartNumberWeight() {
        return partNumberWeight;
    }

    public void setPartNumberWeight(BigDecimal partNumberWeight) {
        this.partNumberWeight = partNumberWeight;
    }

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public BigDecimal getMeasuredWeight() {
        return measuredWeight;
    }

    public void setMeasuredWeight(BigDecimal measuredWeight) {
        this.measuredWeight = measuredWeight;
    }

    public BigDecimal getCalculatedWeight() {
        return calculatedWeight;
    }

    public void setCalculatedWeight(BigDecimal calculatedWeight) {
        this.calculatedWeight = calculatedWeight;
    }

    public String getDeviation() {
        return deviation;
    }

    public void setDeviation(String deviation) {
        this.deviation = deviation;
    }

    public String getWeightedUnit() {
        return weightedUnit;
    }

    public void setWeightedUnit(String weightedUnit) {
        this.weightedUnit = weightedUnit;
    }

    public String getCompDescription() {
        return compDescription;
    }

    public void setCompDescription(String compDescription) {
        this.compDescription = compDescription;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public String getPartMdsStatus() {
        return partMdsStatus;
    }

    public void setPartMdsStatus(String partMdsStatus) {
        this.partMdsStatus = partMdsStatus;
    }

    public BigInteger getNodeId() {
        return nodeId;
    }

    public void setNodeId(BigInteger nodeId) {
        this.nodeId = nodeId;
    }

    public String getPreliminary() {
        return preliminary;
    }

    public void setPreliminary(String preliminary) {
        this.preliminary = preliminary;
    }

    public String getThreeR() {
        return threeR;
    }

    public void setThreeR(String threeR) {
        this.threeR = threeR;
    }

    public String getRefpartNumber() {
        return refpartNumber;
    }

    public void setRefpartNumber(String refpartNumber) {
        this.refpartNumber = refpartNumber;
    }

    private BigInteger mdsId;
    private BigInteger nodeId;
    private BigDecimal mdsVersion;
    private String createDate;
    private Date approvedDate;
    private String supplierCode;
    private String supplierDescription;
    private BigDecimal partNumberWeight;
    private String unitOfMeasure;
    private BigDecimal measuredWeight;
    private BigDecimal calculatedWeight;
    private String deviation;
    private String weightedUnit;
    private String compDescription;
    private String rejectReason;
    private String partMdsStatus;
    private String preliminary;
    private String threeR;
    private String refpartNumber;
}
