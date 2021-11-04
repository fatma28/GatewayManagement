package com.musalasoft.gateways.controller;

import com.google.gson.Gson;
import com.musalasoft.gateways.model.Gateway;
import com.musalasoft.gateways.model.PeripheralDevice;
import com.musalasoft.gateways.repo.DeviceRepository;
import com.musalasoft.gateways.service.DeviceService;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {GatewayController.class})
@WebMvcTest
public class GatewayControllerTest {

    @MockBean
    DeviceRepository deviceRepository;

    @MockBean
    private DeviceService deviceService;

    @Autowired
    private MockMvc mockMvc;
    Gson gson = new Gson();

    @Test
    public void test_addGateway_validResponse() throws Exception {
        Gateway gateway = prepareGatewayRequest();

        when(
                deviceService.addGateway(any())).thenReturn(new ResponseEntity<>(" is added successfully!", HttpStatus.CREATED));
        mockMvc.perform(
                        post("/gateway/add").contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(gson.toJson(gateway)))
                .andExpect(status().isCreated())
                .andExpect(result -> (gateway.getName() + " is added successfully!").equals(result.getResponse().getContentAsString()));
    }

    @Test
    public void test_addGateway_noBody_ErrorReturned() throws Exception {
        mockMvc.perform(post("/gateway/add").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void test_getAllGateways_validResponse() throws Exception {
        when(deviceService.getAllDevices()).thenReturn(Arrays.asList(prepareGatewayRequest()));

        mockMvc.perform(get("/gateway/get/all"))
                .andExpect(result -> StringUtils.isNotBlank(result.getResponse().getContentAsString()));

    }

    @Test
    public void test_getSingleGateway_validResponse() throws Exception {
        when(deviceService.getSingleGatewayDetails("a")).thenReturn(prepareGatewayRequest());
        mockMvc.perform(get("/gateway/get/1")
                )
                .andExpect(result -> StringUtils.isNotBlank(result.getResponse().getContentAsString()));
    }

    @Test
    public void test_addDevice_validResponse() throws Exception {
        PeripheralDevice peripheralDeviceRequest = preparePeripheralDeviceRequest();
        when(deviceService.addDevice(peripheralDeviceRequest))
                .thenReturn(new ResponseEntity<>(peripheralDeviceRequest.getUID() + " is added to " + peripheralDeviceRequest.getGatewayID()
                + " successfully with Inactive status!", HttpStatus.CREATED));

        mockMvc.perform(post("/device/add")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(gson.toJson(peripheralDeviceRequest))
                )
                .andExpect(result -> StringUtils.isNotBlank(result.getResponse().getContentAsString()));
    }

    @Test
    public void test_deleteDevice_validResponse() throws Exception {
        when(deviceService.removeDevice(1)).thenReturn(new ResponseEntity<>(" is deleted successfully!", HttpStatus.CREATED));

        mockMvc.perform(get("/device/delete/1"))
                .andExpect(status().isCreated())
                .andExpect(result -> StringUtils.isNotBlank(result.getResponse().getContentAsString()));
    }

    private PeripheralDevice preparePeripheralDeviceRequest() {
        PeripheralDevice peripheralDevice = new PeripheralDevice();
        peripheralDevice.setUID(123);
        peripheralDevice.setDateCreated(new Date());
        peripheralDevice.setGatewayID("123");
        peripheralDevice.setStatus("Online");
        return peripheralDevice;
    }
    private Gateway prepareGatewayRequest() {
        Gateway gatewayRequest = new Gateway();
        gatewayRequest.setIpv4Address("0.0.0.0");
        gatewayRequest.setName("gateway");
        gatewayRequest.setSerialNumber("serialNumber");
        return gatewayRequest;
    }
}
