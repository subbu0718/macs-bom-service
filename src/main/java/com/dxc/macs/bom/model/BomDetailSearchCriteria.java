package com.dxc.macs.bom.model;

import java.math.BigDecimal;

public class BomDetailSearchCriteria {

    private String productNumber;
    private BigDecimal releaseNumber;
    private String type;
    private String partNumber;
    private String mdsType;
    private String mdsStatus;
    private String refPartNumber;
    private Boolean preliminary;
    private  String bomDescription;
    private Boolean currentVersion;
    private Boolean isCertified;
    private Boolean referencePN;
    private Boolean referencedPN;


    public String getBomDescription() {
        return bomDescription;
    }

    public void setBomDescription(String bomDescription) {
        this.bomDescription = bomDescription;
    }

    public Boolean getPreliminary() {
        return preliminary;
    }

    public void setPreliminary(Boolean preliminary) {
        this.preliminary = preliminary;
    }
    public String getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    public BigDecimal getReleaseNumber() {
        return releaseNumber;
    }

    public void setReleaseNumber(BigDecimal releaseNumber) {
        this.releaseNumber = releaseNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public String getMdsType() {
        return mdsType;
    }

    public void setMdsType(String mdsType) {
        this.mdsType = mdsType;
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

    public Boolean getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(Boolean currentVersion) {
        this.currentVersion = currentVersion;
    }

    public Boolean getIsCertified() {
        return isCertified;
    }

    public void setIsCertified(Boolean certified) {
        isCertified = certified;
    }

    public Boolean getReferencePN() {
        return referencePN;
    }

    public void setReferencePN(Boolean referencePN) {
        this.referencePN = referencePN;
    }

    public Boolean getReferencedPN() {
        return referencedPN;
    }

    public void setReferencedPN(Boolean referencedPN) {
        this.referencedPN = referencedPN;
    }
}
