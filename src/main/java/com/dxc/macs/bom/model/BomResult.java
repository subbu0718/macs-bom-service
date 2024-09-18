package com.dxc.macs.bom.model;

import java.math.BigDecimal;

public class BomResult {
    private int productId;
    private String productNumber;
    private String description;
    private String releaseNumber;
    private String createDate;

    private String createdBy;

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    private String shortDescription;
    private String market;
    private String businessUnit;
    private String status;
    private int totalPartNumbers;
    private int buyPartNumbers;
    private int mdsAccepted;
    private int mdsRejected;
    private int mdsSuspended;
    private int mdsPreliminary;
    private String dossier;
    private String threeR;
    private String line;
    private String taricCode;
    private String taricForComponents;
    private int businessUnitId;
    private int missingMds;
    private BigDecimal measuredWeight;
    private String measuredUnit;
    private int selfCertificateCount;
    private BigDecimal totMeasuredWeight;
    private BigDecimal totCalculatedWeight;
    private Integer cdxJobId;
    private Long echaId;
    private String scipNumber;
    private  String echaUrl;
    private String scipValidationMessage;
    private String regulation;


    public Integer getCdxJobId() {
        return cdxJobId;
    }

    public void setCdxJobId(Integer cdxJobId) {
        this.cdxJobId = cdxJobId;
    }

    public Long getEchaId() {
        return echaId;
    }

    public void setEchaId(Long echaId) {
        this.echaId = echaId;
    }

    public String getScipNumber() {
        return scipNumber;
    }

    public void setScipNumber(String scipNumber) {
        this.scipNumber = scipNumber;
    }

    public String getEchaUrl() {
        return echaUrl;
    }

    public void setEchaUrl(String echaUrl) {
        this.echaUrl = echaUrl;
    }

    public String getScipValidationMessage() {
        return scipValidationMessage;
    }

    public void setScipValidationMessage(String scipValidationMessage) {
        this.scipValidationMessage = scipValidationMessage;
    }

    public BigDecimal getMeasuredWeight() {
        return measuredWeight;
    }

    public void setMeasuredWeight(BigDecimal measuredWeight) {
        this.measuredWeight = measuredWeight;
    }

    public String getMeasuredUnit() {
        return measuredUnit;
    }

    public void setMeasuredUnit(String measuredUnit) {
        this.measuredUnit = measuredUnit;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public int getMissingMds() {
        return missingMds;
    }

    public void setMissingMds(int missingMds) {
        this.missingMds = missingMds;
    }

    public String getTaricCode() {
        return taricCode;
    }

    public void setTaricCode(String taricCode) {
        this.taricCode = taricCode;
    }

    public String getTaricForComponents() {
        return taricForComponents;
    }

    public void setTaricForComponents(String taricForComponents) {
        this.taricForComponents = taricForComponents;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
    public String getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReleaseNumber() {
        return releaseNumber;
    }

    public void setReleaseNumber(String releaseNumber) {
        this.releaseNumber = releaseNumber;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(String businessUnit) {
        this.businessUnit = businessUnit;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalPartNumbers() {
        return totalPartNumbers;
    }

    public void setTotalPartNumbers(int totalPartNumbers) {
        this.totalPartNumbers = totalPartNumbers;
    }

    public int getBuyPartNumbers() {
        return buyPartNumbers;
    }

    public void setBuyPartNumbers(int buyPartNumbers) {
        this.buyPartNumbers = buyPartNumbers;
    }

    public int getMdsAccepted() {
        return mdsAccepted;
    }

    public void setMdsAccepted(int mdsAccepted) {
        this.mdsAccepted = mdsAccepted;
    }

    public int getMdsRejected() {
        return mdsRejected;
    }

    public void setMdsRejected(int mdsRejected) {
        this.mdsRejected = mdsRejected;
    }

    public int getMdsSuspended() {
        return mdsSuspended;
    }

    public void setMdsSuspended(int mdsSuspended) {
        this.mdsSuspended = mdsSuspended;
    }

    public int getMdsPreliminary() {
        return mdsPreliminary;
    }

    public void setMdsPreliminary(int mdsPreliminary) {
        this.mdsPreliminary = mdsPreliminary;
    }

    public String getDossier() {
        return dossier;
    }

    public void setDossier(String dossier) {
        this.dossier = dossier;
    }

    public String getThreeR() {
        return threeR;
    }

    public void setThreeR(String threeR) {
        this.threeR = threeR;
    }

    public int getBusinessUnitId() {
        return businessUnitId;
    }

    public void setBusinessUnitId(int businessUnitId) {
        this.businessUnitId = businessUnitId;
    }

    public int getSelfCertificateCount() {
        return selfCertificateCount;
    }

    public void setSelfCertificateCount(int selfCertificateCount) {
        this.selfCertificateCount = selfCertificateCount;
    }

    public BigDecimal getTotMeasuredWeight() {
        return totMeasuredWeight;
    }

    public void setTotMeasuredWeight(BigDecimal totMeasuredWeight) {
        this.totMeasuredWeight = totMeasuredWeight;
    }

    public BigDecimal getTotCalculatedWeight() {
        return totCalculatedWeight;
    }

    public void setTotCalculatedWeight(BigDecimal totCalculatedWeight) {
        this.totCalculatedWeight = totCalculatedWeight;
    }

    public String getRegulation() {
        return regulation;
    }

    public void setRegulation(String regulation) {
        this.regulation = regulation;
    }
}
