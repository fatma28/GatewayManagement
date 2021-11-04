package com.musalasoft.gateways.repo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.musalasoft.gateways.model.Gateway;
import com.musalasoft.gateways.model.PeripheralDevice;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class DeviceRepositoryImpl implements DeviceRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public Gateway getGatewayByPeripheralDevice(PeripheralDevice device) throws RuntimeException {
        return Optional.ofNullable(device)
                .map(peripheralDevice -> entityManager.find(Gateway.class, peripheralDevice.getGatewayID()))
                .orElse(null);
    }

    @Transactional
    @Override
    public void addDevice(PeripheralDevice device) {
        entityManager.merge(device);
    }

    @Transactional
    @Override
    public void addGateway(Gateway gateway) throws RuntimeException {
        entityManager.merge(gateway);
    }

    @Transactional
    @Override
    public List<Gateway> retrieveAllDevices() throws RuntimeException {
        Query query = entityManager.createQuery("SELECT g from Gateway g");
        return query.getResultList();
    }


    @Transactional
    @Override
    public void remove(PeripheralDevice peripheralDevice) throws RuntimeException {
        entityManager.remove(peripheralDevice);
    }


    @Transactional
    @Override
    public PeripheralDevice getPeripheralDeviceById(int id) {
        return entityManager.find(PeripheralDevice.class, id);
    }

    @Transactional
    @Override
    public Gateway getGatewayAndSubDevices(String id) {
        Query query = entityManager.createQuery("SELECT g FROM Gateway g, PeripheralDevice d WHERE g.serialNumber = :id");
        query.setParameter("id", id);
        return (Gateway) query.getSingleResult();
    }

    @Transactional
    @Override
    public Long getDevicesCountPerGateway(Gateway gateway) {
        Query query = entityManager.createQuery("SELECT COUNT(d) FROM PeripheralDevice d WHERE d.gatewayID = :id");
        query.setParameter("id", gateway.getSerialNumber());
        return (Long) query.getSingleResult();
    }

}
