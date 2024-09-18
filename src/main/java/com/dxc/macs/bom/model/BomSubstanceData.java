package com.dxc.macs.bom.model;

import java.math.BigDecimal;
import java.math.BigInteger;

public class BomSubstanceData {
    public String getCasCode() {
        return casCode;
    }

    public void setCasCode(String casCode) {
        this.casCode = casCode;
    }

    public Long getSubstanceNodeId() {
        return substanceNodeId;
    }

    public void setSubstanceNodeId(Long substanceNodeId) {
        this.substanceNodeId = substanceNodeId;
    }

    public String getSubstanceName() {
        return substanceName;
    }

    public void setSubstanceName(String substanceName) {
        this.substanceName = substanceName;
    }

    public BigDecimal getPortion() {
        return portion;
    }

    public void setPortion(BigDecimal portion) {
        this.portion = portion;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    private String casCode;
    private Long substanceNodeId;
    private String substanceName;
    private BigDecimal portion;
    private String category;
    private Long applicationId;
    private String applicationName;
}
