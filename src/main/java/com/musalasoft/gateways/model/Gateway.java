package com.musalasoft.gateways.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
public class Gateway {

    @Id
    @JsonProperty(value = "serial_number")
    private String serialNumber;
    private String name;
    @NotBlank(message = "ipv4Address value cannot be null")
    private String ipv4Address;

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "serial_number")
    private List<PeripheralDevice> peripharelDeviceList;

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIpv4Address() {
        return ipv4Address;
    }

    public void setIpv4Address(String ipv4Address) {
        this.ipv4Address = ipv4Address;
    }

    public List<PeripheralDevice> getPeripharelDeviceList() {
        return peripharelDeviceList;
    }

    public void setPeripharelDeviceList(List<PeripheralDevice> peripharelDeviceList) {
        this.peripharelDeviceList = peripharelDeviceList;
    }

}
