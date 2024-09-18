package com.dxc.macs.bom.model;

import java.math.BigDecimal;

public class NewBomModel {
    private int id;
    private String productNumber;
    private String description;
    private String market;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
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

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getBusinessUnitId() {
        return businessUnitId;
    }

    public void setBusinessUnitId(int businessUnitId) {
        this.businessUnitId = businessUnitId;
    }

    public String getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(String businessUnit) {
        this.businessUnit = businessUnit;
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

    public String getDossierNumber() {
        return dossierNumber;
    }

    public void setDossierNumber(String dossierNumber) {
        this.dossierNumber = dossierNumber;
    }

    public String getThreeRStatus() {
        return threeRStatus;
    }

    public void setThreeRStatus(String threeRStatus) {
        this.threeRStatus = threeRStatus;
    }

    public String getPlant() {
        return plant;
    }

    public void setPlant(String plant) {
        this.plant = plant;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(String updated_by) {
        this.updated_by = updated_by;
    }

    private String releaseNumber;
    private String shortDescription;
    private String longDescription;
    private String status;
    private int businessUnitId;
    private String businessUnit;
    private String taricCode;
    private String taricForComponents;
    private String dossierNumber;
    private String threeRStatus;
    private String plant;
    private String created_by;
    private String updated_by;
    private BigDecimal measuredWeight;
    private String measuredUnit;
    private String regulation;

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

    public String getRegulation() {
        return regulation;
    }

    public void setRegulation(String regulation) {
        this.regulation = regulation;
    }
}
