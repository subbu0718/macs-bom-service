package com.dxc.macs.bom.model;

import java.math.BigDecimal;

public class ComponentMdsType {

    private Long nodeId;
    private Boolean preliminary;

    private String recipientStatus;

    private String partNumber;

    private String componentType;

    private String trimmedPartNumb;
    private BigDecimal version;

    public String getTrimmedPartNumb() {
        return trimmedPartNumb;
    }

    public void setTrimmedPartNumb(String trimmedPartNumb) {
        this.trimmedPartNumb = trimmedPartNumb;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public Boolean getPreliminary() {
        return preliminary;
    }

    public void setPreliminary(Boolean preliminary) {
        this.preliminary = preliminary;
    }

    public String getRecipientStatus() {
        return recipientStatus;
    }

    public void setRecipientStatus(String recipientStatus) {
        this.recipientStatus = recipientStatus;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public BigDecimal getVersion() {
        return version;
    }

    public void setVersion(BigDecimal version) {
        this.version = version;
    }
}
