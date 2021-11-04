package com.musalasoft.gateways.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.musalasoft.gateways.repo.DeviceRepository;
import com.musalasoft.gateways.model.Gateway;
import com.musalasoft.gateways.model.PeripheralDevice;
import com.musalasoft.gateways.util.ValidationUtil;

@Service
public class DeviceService {

    DeviceRepository deviceRepository;

    @Autowired
    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public ResponseEntity<String> addGateway(Gateway gatewayRequest) {

        if (ValidationUtil.validateIpv4(gatewayRequest.getIpv4Address())) {
            try {
                deviceRepository.addGateway(gatewayRequest);
            } catch (Exception ex) {
                return new ResponseEntity<>("Failed to add the Gateway!", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return Optional.ofNullable(gatewayRequest.getPeripharelDeviceList())
                    .filter(peripharelDeviceList -> !peripharelDeviceList.isEmpty())
                    .filter(peripharelDeviceList -> deviceRepository.getDevicesCountPerGateway(gatewayRequest) < 10)
                    .map(peripharelDeviceList -> saveSubDevices(gatewayRequest, peripharelDeviceList))
                    .orElse(new ResponseEntity<>(gatewayRequest.getName() + " is added successfully!", HttpStatus.CREATED));
        } else {
            return new ResponseEntity<>("Invalid IPV4!", HttpStatus.BAD_REQUEST);
        }
    }

    private ResponseEntity<String> saveSubDevices(Gateway gatewayRequest, List<PeripheralDevice> peripheralDeviceList) {
        peripheralDeviceList
                .stream()
                .forEach(peripheralDevice -> {
                    peripheralDevice.setGatewayID(gatewayRequest.getSerialNumber());
                    deviceRepository.addDevice(peripheralDevice);
                });
        return new ResponseEntity<>("Gateway: " + gatewayRequest.getName() + " and its devices are added successfully!", HttpStatus.CREATED);
    }

    public ResponseEntity<String> addDevice(PeripheralDevice peripheralDeviceRequest) {
        return Optional.ofNullable(peripheralDeviceRequest)
                .filter(request -> ValidationUtil.validateValue(request.getGatewayID()))
                .map(request -> deviceRepository.getGatewayByPeripheralDevice(request))
                .filter(Objects::nonNull)
                .filter(gateway -> deviceRepository.getDevicesCountPerGateway(gateway) < 10)
                .map(gateway -> {
                    deviceRepository.addDevice(peripheralDeviceRequest);
                    return new ResponseEntity<>(peripheralDeviceRequest.getUID() + " is added to " + peripheralDeviceRequest.getGatewayID() + " successfully!", HttpStatus.CREATED);
                })
                .orElse(new ResponseEntity<>("Failed to add Device!", HttpStatus.BAD_REQUEST));
    }

    public Gateway getSingleGatewayDetails(String id) {
        return deviceRepository.getGatewayAndSubDevices(id);
    }

    public List<Gateway> getAllDevices() throws RuntimeException {
        return deviceRepository.retrieveAllDevices();
    }

    public ResponseEntity<String> removeDevice(int id) {
        return Optional.ofNullable(id)
                .map(deviceRepository::getPeripheralDeviceById)
                .map(peripheralDevice -> {
                    deviceRepository.remove(peripheralDevice);
                    return new ResponseEntity<>(peripheralDevice.getUID() + " is deleted successfully!", HttpStatus.CREATED);
                })
                .orElse(new ResponseEntity<>("Failed to remove Device!", HttpStatus.BAD_REQUEST));
    }

}
