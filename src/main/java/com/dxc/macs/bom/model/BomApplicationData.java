package com.dxc.macs.bom.model;

import java.math.BigInteger;

public class BomApplicationData {
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

    private Long applicationId;
    private String applicationName;
}
