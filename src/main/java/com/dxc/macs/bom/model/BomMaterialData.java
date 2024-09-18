package com.dxc.macs.bom.model;

import java.math.BigInteger;

public class BomMaterialData {

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

    public String getClassificationCode() {
        return classificationCode;
    }

    public void setClassificationCode(String classificationCode) {
        this.classificationCode = classificationCode;
    }

    public String getClassificationName() {
        return classificationName;
    }

    public void setClassificationName(String classificationName) {
        this.classificationName = classificationName;
    }

    private BigInteger materialNodeId;
    private String materialName;
    private String classificationCode;
    private String classificationName;
}
