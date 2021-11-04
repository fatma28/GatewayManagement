package com.musalasoft.gateways.repo;

import com.musalasoft.gateways.model.Gateway;
import com.musalasoft.gateways.model.PeripheralDevice;

import java.util.List;

public interface DeviceRepository {

    Gateway getGatewayByPeripheralDevice(PeripheralDevice device) throws RuntimeException;

    void addDevice(PeripheralDevice device);

    void addGateway(Gateway gateway) throws RuntimeException;

    void remove(PeripheralDevice peripheralDevice) throws RuntimeException;

    PeripheralDevice getPeripheralDeviceById(int id);

    Gateway getGatewayAndSubDevices(String id);

    List<Gateway> retrieveAllDevices() throws RuntimeException;

    Long getDevicesCountPerGateway(Gateway gateway);

}
