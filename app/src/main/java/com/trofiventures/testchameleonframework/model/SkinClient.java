package com.trofiventures.testchameleonframework.model;

import java.io.Serializable;

public class SkinClient implements Serializable {

    private String beaconId;
    private String bundleId;

    public String getBeaconId() {
        return beaconId;
    }

    public void setBeaconId(String beaconId) {
        this.beaconId = beaconId;
    }

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }
}
