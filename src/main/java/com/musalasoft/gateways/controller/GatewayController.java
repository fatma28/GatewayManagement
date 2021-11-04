package com.musalasoft.gateways.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.musalasoft.gateways.model.Gateway;
import com.musalasoft.gateways.model.PeripheralDevice;
import com.musalasoft.gateways.service.DeviceService;

@RestController
public class GatewayController {

    @Autowired
    DeviceService deviceService;

    @RequestMapping(path = "/gateway/add", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public ResponseEntity<String> addGateway(@RequestBody @NonNull Gateway request) {
        return deviceService.addGateway(request);
    }

    @RequestMapping(path = "/gateway/get/all", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public List<Gateway> getAllGateways() throws Exception {
        return deviceService.getAllDevices();
    }

    @RequestMapping(path = "/gateway/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public Gateway getSingleGateway(@PathVariable(value = "id") String gatewayId) {
        return deviceService.getSingleGatewayDetails(gatewayId);
    }

    @RequestMapping(path = "/device/add", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public ResponseEntity<String> addDevice(@RequestBody PeripheralDevice device) {
        return deviceService.addDevice(device);
    }

    @RequestMapping(path = "/device/delete/{id}", method = RequestMethod.GET)
    public ResponseEntity deleteDevice(@PathVariable(value = "id") int id) {
        return deviceService.removeDevice(id);
    }
}
