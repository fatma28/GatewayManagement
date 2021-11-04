package com.musalasoft.gateways.repo;

import com.musalasoft.gateways.model.Gateway;
import com.musalasoft.gateways.model.PeripheralDevice;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.util.Arrays;
import java.util.Date;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeviceRepositoryImplTest {

    DeviceRepositoryImpl deviceRepositoryImpl = new DeviceRepositoryImpl();

    @MockBean
    EntityManager entityManager;

    @BeforeEach
    void setUp() {
        entityManager = Mockito.mock(EntityManager.class);
        ReflectionTestUtils.setField(deviceRepositoryImpl, "entityManager", entityManager);
    }

    @Test
    public void test_getGatewayByPeripheralDevice_NullResponse() {
        PeripheralDevice peripheralDevice = new PeripheralDevice();
        Assertions.assertNull(deviceRepositoryImpl.getGatewayByPeripheralDevice(peripheralDevice));
    }

    @Test
    public void test_getGatewayByPeripheralDevice_throwException() {
        PeripheralDevice peripheralDevice = new PeripheralDevice();
        when(entityManager.find(Gateway.class, peripheralDevice.getGatewayID())).thenThrow(RuntimeException.class);
        Assertions.assertThrows(RuntimeException.class, () -> deviceRepositoryImpl.getGatewayByPeripheralDevice(peripheralDevice));
    }


    private Gateway prepareGatewayRequest() {
        Gateway gatewayRequest = new Gateway();
        gatewayRequest.setIpv4Address("0.0.0.0");
        gatewayRequest.setName("gateway");
        gatewayRequest.setSerialNumber("serialNumber");
        return gatewayRequest;
    }

    private PeripheralDevice preparePeripheralDeviceRequest() {
        PeripheralDevice peripheralDevice = new PeripheralDevice();
        peripheralDevice.setUID(123);
        peripheralDevice.setDateCreated(new Date());
        peripheralDevice.setGatewayID("123");
        peripheralDevice.setStatus("Online");
        return peripheralDevice;
    }

    @Test
    public void test_addDevice_throwException() {
        PeripheralDevice peripheralDevice = preparePeripheralDeviceRequest();
        when(entityManager.merge(peripheralDevice)).thenThrow(RuntimeException.class);
        Assertions.assertThrows(RuntimeException.class, () -> deviceRepositoryImpl.addDevice(peripheralDevice));
    }

    @Test
    public void test_addGateway_throwException() {
        Gateway gateway = prepareGatewayRequest();
        when(entityManager.merge(gateway)).thenThrow(RuntimeException.class);
        Assertions.assertThrows(RuntimeException.class, () -> deviceRepositoryImpl.addGateway(gateway));
    }

    @Test
    public void test_getGatewayAndSubDevices_throwException() {
        when(entityManager.createQuery("")).thenThrow(RuntimeException.class);
        Assertions.assertThrows(RuntimeException.class, () -> deviceRepositoryImpl.getGatewayAndSubDevices("1"));
    }

    @Test
    public void test_getGatewayAndSubDevices_validResponse() {
        Gateway gateway = prepareGatewayRequest();
        TypedQuery query = Mockito.mock(TypedQuery.class);
        when(entityManager.createQuery("SELECT g FROM Gateway g, PeripheralDevice d WHERE g.serialNumber = :id")).thenReturn(query);
        when(query.getSingleResult()).thenReturn(gateway);
        Assertions.assertNotNull(deviceRepositoryImpl.getGatewayAndSubDevices("1"));
        Assertions.assertEquals(gateway.getName(), deviceRepositoryImpl.getGatewayAndSubDevices("1").getName());
        Assertions.assertEquals(gateway.getSerialNumber(), deviceRepositoryImpl.getGatewayAndSubDevices("1").getSerialNumber());
        Assertions.assertEquals(gateway.getIpv4Address(), deviceRepositoryImpl.getGatewayAndSubDevices("1").getIpv4Address());
    }

    @Test
    public void test_getPeripheralDeviceById_throwException() {
        when(entityManager.find(PeripheralDevice.class, 1)).thenThrow(RuntimeException.class);
        Assertions.assertThrows(RuntimeException.class, () -> deviceRepositoryImpl.getPeripheralDeviceById(1));
    }

    @Test
    public void test_getDevicesCountPerGateway_throwException() {
        Gateway gateway = prepareGatewayRequest();
        when(entityManager.createQuery("")).thenThrow(RuntimeException.class);
        Assertions.assertThrows(RuntimeException.class, () -> deviceRepositoryImpl.getDevicesCountPerGateway(gateway));
    }

    @Test
    public void test_getDevicesCountPerGateway_validResponse() {
        Gateway gateway = prepareGatewayRequest();
        TypedQuery query = Mockito.mock(TypedQuery.class);
        when(entityManager.createQuery("SELECT COUNT(d) FROM PeripheralDevice d WHERE d.gatewayID = :id")).thenReturn(query);
        when(query.getSingleResult()).thenReturn(Long.valueOf(1));
        Assertions.assertNotNull(deviceRepositoryImpl.getDevicesCountPerGateway(gateway));
        Assertions.assertEquals(Long.valueOf(1), deviceRepositoryImpl.getDevicesCountPerGateway(gateway));
    }

    @Test
    public void test_retrieveAllDevices_throwException() throws Exception {
        TypedQuery query = Mockito.mock(TypedQuery.class);
        when(entityManager.createQuery("SELECT g from Gateway g")).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList(prepareGatewayRequest()));
        Assertions.assertNotNull(deviceRepositoryImpl.retrieveAllDevices());
    }

    @Test
    public void test_remove_throwException() {
        PeripheralDevice peripheralDevice = new PeripheralDevice();
        doThrow(RuntimeException.class).when(entityManager).remove(peripheralDevice);
        Assertions.assertThrows(RuntimeException.class, () -> deviceRepositoryImpl.remove(peripheralDevice));
    }
}
