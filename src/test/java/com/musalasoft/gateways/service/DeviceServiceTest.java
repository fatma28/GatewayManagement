package com.musalasoft.gateways.service;


import com.musalasoft.gateways.repo.DeviceRepository;
import com.musalasoft.gateways.model.Gateway;
import com.musalasoft.gateways.model.PeripheralDevice;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeviceServiceTest {

    DeviceService deviceService;

    @Mock
    DeviceRepository deviceRepository; //= mock(DeviceRepository.class);

    @BeforeEach
    void init() {
        deviceService = new DeviceService(deviceRepository);
    }

    @Test
    void test_addGateway_validResonse() throws Exception {
        Gateway gatewayRequest = prepareGatewayRequest();

        doNothing().when(deviceRepository).addGateway(any());
        ResponseEntity<String> stringResponseEntity = deviceService.addGateway(gatewayRequest);
        Assertions.assertEquals(gatewayRequest.getName() + " is added successfully!", stringResponseEntity.getBody());
        Assertions.assertEquals(HttpStatus.CREATED, stringResponseEntity.getStatusCode());
    }

    @Test
    void test_addGateway_invalidIPV4_throwException() throws Exception {
        Gateway gatewayRequest = new Gateway();
        gatewayRequest.setIpv4Address("test");
        ResponseEntity<String> stringResponseEntity = deviceService.addGateway(gatewayRequest);
        Assertions.assertEquals("Invalid IPV4!", stringResponseEntity.getBody());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, stringResponseEntity.getStatusCode());
    }


    @Test
    void test_addGateway_validIPV4_throwExpection() throws Exception {
        Gateway gatewayRequest = prepareGatewayRequest();

        doThrow(RuntimeException.class).when(deviceRepository).addGateway(any());
        ResponseEntity<String> stringResponseEntity = deviceService.addGateway(gatewayRequest);
        Assertions.assertEquals("Failed to add the Gateway!", stringResponseEntity.getBody());
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, stringResponseEntity.getStatusCode());
    }

    private Gateway prepareGatewayRequest() {
        Gateway gatewayRequest = new Gateway();
        gatewayRequest.setIpv4Address("0.0.0.0");
        gatewayRequest.setName("gateway");
        gatewayRequest.setSerialNumber("serialNumber");
        return gatewayRequest;
    }

    @Test
    void test_addGateway_withEmptySubDevices_validResonse() throws Exception {
        Gateway gatewayRequest = prepareGatewayWithEmptySubDevicesRequest();
        doNothing().when(deviceRepository).addGateway(any());
        ResponseEntity<String> stringResponseEntity = deviceService.addGateway(gatewayRequest);
        Assertions.assertEquals(gatewayRequest.getName() + " is added successfully!", stringResponseEntity.getBody());
        Assertions.assertEquals(HttpStatus.CREATED, stringResponseEntity.getStatusCode());
    }

    private Gateway prepareGatewayWithEmptySubDevicesRequest() {
        Gateway gatewayRequest = prepareGatewayRequest();
        gatewayRequest.setPeripharelDeviceList(new ArrayList<>());
        return gatewayRequest;
    }

    @Test
    void test_addGateway_withNullSubDevices_validResonse() throws Exception {
        Gateway gatewayRequest = prepareGatewayWithNullSubDevicesRequest();
        doNothing().when(deviceRepository).addGateway(any());
        ResponseEntity<String> stringResponseEntity = deviceService.addGateway(gatewayRequest);
        Assertions.assertEquals(gatewayRequest.getName() + " is added successfully!", stringResponseEntity.getBody());
        Assertions.assertEquals(HttpStatus.CREATED, stringResponseEntity.getStatusCode());
    }

    private Gateway prepareGatewayWithNullSubDevicesRequest() {
        Gateway gatewayRequest = prepareGatewayRequest();
        gatewayRequest.setPeripharelDeviceList(null);
        return gatewayRequest;
    }

    @Test
    void test_addGateway_assignSubDeviceToExistingGateway_validResonse() throws Exception {
        Gateway gatewayRequest = prepareGatewayRequest_assignSubDeviceToExistingGateway();
        doNothing().when(deviceRepository).addGateway(any());
        ResponseEntity<String> stringResponseEntity = deviceService.addGateway(gatewayRequest);
        Assertions.assertEquals("Gateway: " + gatewayRequest.getName() + " and its devices are added successfully!", stringResponseEntity.getBody());
        Assertions.assertEquals(HttpStatus.CREATED, stringResponseEntity.getStatusCode());
    }

    private Gateway prepareGatewayRequest_assignSubDeviceToExistingGateway() {
        Gateway gatewayRequest = prepareGatewayRequest();
        PeripheralDevice peripheralDevice = new PeripheralDevice();
        peripheralDevice.setUID(123);
        peripheralDevice.setDateCreated(new Date());
        peripheralDevice.setGatewayID(gatewayRequest.getSerialNumber());
        peripheralDevice.setStatus("Online");
        peripheralDevice.setVendor("vendorName");
        gatewayRequest.setPeripharelDeviceList(Arrays.asList(peripheralDevice));
        return gatewayRequest;
    }


    @Test
    void test_addGateway_assignSubDeviceToExistingGateway_GatewayExceed10_GatewayAdded() throws Exception {
        Gateway gatewayRequest = prepareGatewayRequest_assignSubDeviceToExistingGateway();
        when(deviceRepository.getDevicesCountPerGateway(gatewayRequest)).thenReturn(Long.valueOf(10));
        doNothing().when(deviceRepository).addGateway(any());
        ResponseEntity<String> stringResponseEntity = deviceService.addGateway(gatewayRequest);
        Assertions.assertEquals(gatewayRequest.getName() + " is added successfully!", stringResponseEntity.getBody());
        Assertions.assertEquals(HttpStatus.CREATED, stringResponseEntity.getStatusCode());
    }


    /**
     * Add Device Test Cases
     */

    @Test
    void test_addDevice_NullGatewayID_ErrorReturned() {
        PeripheralDevice peripheralDeviceRequest = preparePeripheralDeviceRequest_nullGatewayID();
        ResponseEntity<String> stringResponseEntity = deviceService.addDevice(peripheralDeviceRequest);
        Assertions.assertEquals("Failed to add Device!", stringResponseEntity.getBody());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, stringResponseEntity.getStatusCode());
    }


    private PeripheralDevice preparePeripheralDeviceRequest_nullGatewayID() {
        PeripheralDevice peripheralDevice = new PeripheralDevice();
        peripheralDevice.setUID(123);
        peripheralDevice.setDateCreated(new Date());
        peripheralDevice.setGatewayID(null);
        peripheralDevice.setStatus("Online");
        peripheralDevice.setVendor("vendorName");
        return peripheralDevice;
    }

    @Test
    void test_addDevice_EmptyGatewayID_ErrorReturned() {
        PeripheralDevice peripheralDeviceRequest = preparePeripheralDeviceRequest_emptyGatewayID();
        ResponseEntity<String> stringResponseEntity = deviceService.addDevice(peripheralDeviceRequest);
        Assertions.assertEquals("Failed to add Device!", stringResponseEntity.getBody());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, stringResponseEntity.getStatusCode());
    }

    private PeripheralDevice preparePeripheralDeviceRequest_emptyGatewayID() {
        PeripheralDevice peripheralDevice = new PeripheralDevice();
        peripheralDevice.setUID(123);
        peripheralDevice.setDateCreated(new Date());
        peripheralDevice.setGatewayID("");
        peripheralDevice.setStatus("Online");
        return peripheralDevice;
    }

    @Test
    void test_addDevice_nullGatewayByPeripheralDeviceResponse_ErrorReturned() {
        PeripheralDevice peripheralDeviceRequest = preparePeripheralDeviceRequest();
        when(deviceRepository.getGatewayByPeripheralDevice(peripheralDeviceRequest)).thenReturn(null);
        ResponseEntity<String> stringResponseEntity = deviceService.addDevice(peripheralDeviceRequest);
        Assertions.assertEquals("Failed to add Device!", stringResponseEntity.getBody());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, stringResponseEntity.getStatusCode());
    }

    private PeripheralDevice preparePeripheralDeviceRequest() {
        PeripheralDevice peripheralDevice = new PeripheralDevice();
        peripheralDevice.setUID(123);
        peripheralDevice.setDateCreated(new Date());
        peripheralDevice.setGatewayID("123");
        peripheralDevice.setStatus("Online");
        peripheralDevice.setVendor("vendorName");
        return peripheralDevice;
    }

    @Test
    void test_addDevice_assignSubDeviceToNotExistingGateway_ErrorResponse() {
        PeripheralDevice peripheralDevice = preparePeripheralDeviceRequest();
        when(deviceRepository.getGatewayByPeripheralDevice(peripheralDevice)).thenReturn(null);
        ResponseEntity<String> stringResponseEntity = deviceService.addDevice(peripheralDevice);
        Assertions.assertEquals("Failed to add Device!", stringResponseEntity.getBody());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, stringResponseEntity.getStatusCode());
    }

    @Test
    void test_addDevice_assignSubDeviceToExistingGateway_GatewayExceed10_ErrorResponse() {
        PeripheralDevice peripheralDevice = preparePeripheralDeviceRequest();
        Gateway gatewayRequest = prepareGatewayRequest_assignSubDeviceToExistingGateway();
        when(deviceRepository.getGatewayByPeripheralDevice(any())).thenReturn(gatewayRequest);
        when(deviceRepository.getDevicesCountPerGateway(gatewayRequest)).thenReturn(Long.valueOf(10));
        ResponseEntity<String> stringResponseEntity = deviceService.addDevice(peripheralDevice);
        Assertions.assertEquals("Failed to add Device!", stringResponseEntity.getBody());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, stringResponseEntity.getStatusCode());
    }

    @Test
    void test_addDevice_assignSubDeviceToExistingGateway_GatewayLessThan10_ErrorResponse() {
        PeripheralDevice peripheralDevice = preparePeripheralDeviceRequest();
        Gateway gatewayRequest = prepareGatewayRequest_assignSubDeviceToExistingGateway();
        when(deviceRepository.getGatewayByPeripheralDevice(any())).thenReturn(gatewayRequest);
        when(deviceRepository.getDevicesCountPerGateway(gatewayRequest)).thenReturn(Long.valueOf(9));
        ResponseEntity<String> stringResponseEntity = deviceService.addDevice(peripheralDevice);
        Assertions.assertEquals(peripheralDevice.getUID() + " is added to " + peripheralDevice.getGatewayID()
                        + " successfully!", stringResponseEntity.getBody());
        Assertions.assertEquals(HttpStatus.CREATED, stringResponseEntity.getStatusCode());
    }

    private Gateway prepareGatewayRequest_assignSubDeviceToNotExistGateway() {
        Gateway gatewayRequest = prepareGatewayRequest();
        PeripheralDevice peripheralDevice = new PeripheralDevice();
        peripheralDevice.setUID(123);
        peripheralDevice.setDateCreated(new Date());
        peripheralDevice.setGatewayID("notExist");
        peripheralDevice.setStatus("Online");
        gatewayRequest.setPeripharelDeviceList(Arrays.asList(peripheralDevice));
        return gatewayRequest;
    }

    @Test
    public void test_removeDevice_validResponse() {
        PeripheralDevice peripheralDevice = new PeripheralDevice();
        peripheralDevice.setUID(2);
        when(deviceRepository.getPeripheralDeviceById(2)).thenReturn(peripheralDevice);
        Assertions.assertEquals("2 is deleted successfully!", deviceService.removeDevice(2).getBody());
        Assertions.assertEquals(HttpStatus.CREATED, deviceService.removeDevice(2).getStatusCode());
    }

    @Test
    public void test_removeDevice_ErrorResponse() {
        PeripheralDevice peripheralDevice = new PeripheralDevice();
        peripheralDevice.setUID(2);
        when(deviceRepository.getPeripheralDeviceById(2)).thenReturn(null);
        Assertions.assertEquals("Failed to remove Device!", deviceService.removeDevice(2).getBody());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, deviceService.removeDevice(2).getStatusCode());
    }

    @Test
    public void test_getSingleGatewayDetails_emptyResponse() {
        when(deviceRepository.getGatewayAndSubDevices("0")).thenReturn(null);
        Assertions.assertNull(deviceService.getSingleGatewayDetails("0"));
    }


    @Test
    public void test_getSingleGatewayDetails_validResponse() {
        Gateway gateway = prepareGatewayRequest_assignSubDeviceToExistingGateway();
        when(deviceRepository.getGatewayAndSubDevices("serialNumber")).thenReturn(gateway);
        Gateway gatewayResponse = deviceService.getSingleGatewayDetails("serialNumber");
        Assertions.assertNotNull(gatewayResponse);
        Assertions.assertEquals(gateway, gatewayResponse);
        Assertions.assertEquals("gateway", gatewayResponse.getName());
        Assertions.assertEquals("0.0.0.0", gatewayResponse.getIpv4Address());
        Assertions.assertEquals("serialNumber", gatewayResponse.getSerialNumber());
        Assertions.assertEquals(1, gatewayResponse.getPeripharelDeviceList().size());
        Assertions.assertEquals("serialNumber", gatewayResponse.getPeripharelDeviceList().get(0).getGatewayID());
        Assertions.assertEquals("Online", gatewayResponse.getPeripharelDeviceList().get(0).getStatus());
        Assertions.assertEquals("vendorName", gatewayResponse.getPeripharelDeviceList().get(0).getVendor());
        Assertions.assertNotNull(gatewayResponse.getPeripharelDeviceList().get(0).getDateCreated());
    }

    @Test
    public void test_getAllDevices_validResponse() throws Exception {
        when(deviceRepository.retrieveAllDevices()).thenReturn(Arrays.asList(prepareGatewayRequest()));
        Assertions.assertNotNull(deviceService.getAllDevices());
        Assertions.assertEquals(1, deviceService.getAllDevices().size());
    }

    @Test
    public void test_getAllDevices_nullResponse() throws Exception {
        when(deviceRepository.retrieveAllDevices()).thenReturn(null);
        Assertions.assertNull(deviceService.getAllDevices());
    }

}
