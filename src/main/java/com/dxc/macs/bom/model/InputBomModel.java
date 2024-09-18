package com.dxc.macs.bom.model;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class InputBomModel {

    public InputBomModel(){
        super();
    }

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

    public MultipartFile getModelFile() {
        return modelFile;
    }

    public void setModelFile(MultipartFile modelFile) {
        this.modelFile = modelFile;
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

    public int getBusinessUnitId() {
        return businessUnitId;
    }

    public void setBusinessUnitId(int businessUnitId) {
        this.businessUnitId = businessUnitId;
    }

    public String getBusinessUnitName() {
        return businessUnitName;
    }

    public void setBusinessUnitName(String businessUnitName) {
        this.businessUnitName = businessUnitName;
    }

    public BigDecimal getMeasuredWeight() {
        return measuredWeight;
    }

    public void setMeasuredWeight(BigDecimal measuredWeight) {
        this.measuredWeight = measuredWeight;
    }

    public String getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(String businessUnit) {
        this.businessUnit = businessUnit;
    }

    private int id;
    private String productNumber;
    private String description;
    private String market;
    private String releaseNumber;
    private String shortDescription;
    private String longDescription;
    private String status;
    private MultipartFile modelFile;
    private String taricCode;
    private String taricForComponents;
    private String dossierNumber;
    private String threeRStatus;
    private int businessUnitId;
    private String businessUnitName;
    private BigDecimal measuredWeight;
    private String measuredUnit;
    private String businessUnit;
    private String accessState;
    private String plant;
    private Long createdImdsId;
    private String sentFlag;
    private String regulation;

    public Long getCreatedImdsId() {
        return createdImdsId;
    }

    public void setCreatedImdsId(Long createdImdsId) {
        this.createdImdsId = createdImdsId;
    }

    public String getSentFlag() {
        return sentFlag;
    }

    public void setSentFlag(String sentFlag) {
        this.sentFlag = sentFlag;
    }

}
