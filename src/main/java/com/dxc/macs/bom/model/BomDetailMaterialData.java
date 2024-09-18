package com.dxc.macs.bom.model;

import java.math.BigDecimal;
import java.math.BigInteger;

public class BomDetailMaterialData {

    private BigInteger materialNodeId;
    private String materialName;
    private BigDecimal materialWeight;
    private String materialClassificationCode;
    private String materialClassificationName;
    private Long semiComponentNodeId;
    private String articleName;
    private BigDecimal semiWeight;
    private Long materialImmediateParentId;
    private BigDecimal semiWeightedMean;
    private BigDecimal materialWeightedMean;

    public BigInteger getMaterialNodeId() {
        return materialNodeId;
    }

    public void setMaterialNodeId(BigInteger materialNodeId) {
        this.materialNodeId = materialNodeId;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public BigDecimal getMaterialWeight() {
        return materialWeight;
    }

    public void setMaterialWeight(BigDecimal materialWeight) {
        this.materialWeight = materialWeight;
    }

    public String getMaterialClassificationCode() {
        return materialClassificationCode;
    }

    public void setMaterialClassificationCode(String materialClassificationCode) {
        this.materialClassificationCode = materialClassificationCode;
    }

    public String getMaterialClassificationName() {
        return materialClassificationName;
    }

    public void setMaterialClassificationName(String materialClassificationName) {
        this.materialClassificationName = materialClassificationName;
    }

    public Long getSemiComponentNodeId() {
        return semiComponentNodeId;
    }

    public void setSemiComponentNodeId(Long semiComponentNodeId) {
        this.semiComponentNodeId = semiComponentNodeId;
    }

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public BigDecimal getSemiWeight() {
        return semiWeight;
    }

    public void setSemiWeight(BigDecimal semiWeight) {
        this.semiWeight = semiWeight;
    }

    public Long getMaterialImmediateParentId() {
        return materialImmediateParentId;
    }

    public void setMaterialImmediateParentId(Long materialImmediateParentId) {
        this.materialImmediateParentId = materialImmediateParentId;
    }

    public BigDecimal getSemiWeightedMean() {
        return semiWeightedMean;
    }

    public void setSemiWeightedMean(BigDecimal semiWeightedMean) {
        this.semiWeightedMean = semiWeightedMean;
    }

    public BigDecimal getMaterialWeightedMean() {
        return materialWeightedMean;
    }

    public void setMaterialWeightedMean(BigDecimal materialWeightedMean) {
        this.materialWeightedMean = materialWeightedMean;
    }

}
