package com.dxc.macs.bom.model;

import java.math.BigDecimal;
import java.util.Date;

public class BomSearchCriteria {

    private String businessUnit;
    private String product;
    private String description;
    private String status;
    private String mdsStatus;
    private String threeR;
    private String releaseNumber;
    private  String partNumber;
    private Date fromCreateDate;
    private Date toCreateDate;

    public String getReleaseNumber() {
        return releaseNumber;
    }

    public void setReleaseNumber(String releaseNumber) {
        this.releaseNumber = releaseNumber;
    }

    public String getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(String businessUnit) {
        this.businessUnit = businessUnit;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMdsStatus() {
        return mdsStatus;
    }

    public void setMdsStatus(String mdsStatus) {
        this.mdsStatus = mdsStatus;
    }

    public String getThreeR() {
        return threeR;
    }

    public void setThreeR(String threeR) {
        this.threeR = threeR;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public Date getFromCreateDate() {
        return fromCreateDate;
    }

    public void setFromCreateDate(Date fromCreateDate) {
        this.fromCreateDate = fromCreateDate;
    }

    public Date getToCreateDate() {
        return toCreateDate;
    }

    public void setToCreateDate(Date toCreateDate) {
        this.toCreateDate = toCreateDate;
    }
}
