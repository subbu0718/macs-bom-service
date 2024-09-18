package com.dxc.macs.bom.model;

import java.math.BigDecimal;
import java.math.BigInteger;

public class BomDetailInput {
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

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public Long getComponentNode() {
        return componentNode;
    }

    public void setComponentNode(Long componentNode) {
        this.componentNode = componentNode;
    }

    public Long getMaterialNode() {
        return materialNode;
    }

    public void setMaterialNode(Long materialNode) {
        this.materialNode = materialNode;
    }

    public BigInteger getSubstanceNode() {
        return substanceNode;
    }

    public void setSubstanceNode(BigInteger substanceNode) {
        this.substanceNode = substanceNode;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public BigDecimal getVersion() {
        return version;
    }

    public void setVersion(BigDecimal version) {
        this.version = version;
    }

    private String productNumber;
    private String releaseNumber;
    private String partNumber;
    private Long componentNode;
    private Long materialNode;
    private BigInteger substanceNode;
    private String lang;
    private BigDecimal version;
}
