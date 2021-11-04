package com.musalasoft.gateways.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "DEVICE")
public class PeripheralDevice {

    @Id
    @JsonProperty("uid")
    private int UID;
    private String vendor;
    @JsonProperty("date_created")
    private Date dateCreated;
    @NotNull
    @Pattern(regexp = "Online|Offline", message = "Status value should be Online or Offline")
    private String status;
    @JsonProperty("gateway_id")
    private String gatewayID;

    public int getUID() {
        return UID;
    }

    public void setUID(int uID) {
        UID = uID;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGatewayID() {
        return gatewayID;
    }

    public void setGatewayID(String gatewayID) {
        this.gatewayID = gatewayID;
    }

}
