package com.musalasoft.gateways.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ValidationUtilTest {

    @Test
    void test_validateValue_passNullValue_invalidResponse() {
        Assertions.assertFalse(ValidationUtil.validateValue(null));
    }

    @Test
    void test_validateValue_passEmptyValue_invalidResponse() {
        Assertions.assertFalse(ValidationUtil.validateValue(""));
    }

    @Test
    void test_validateValue_validResponse() {
        Assertions.assertTrue(ValidationUtil.validateValue("test"));
    }

    @Test
    void test_validateIpv4_invalidResponse() {
        Assertions.assertFalse(ValidationUtil.validateIpv4("test"));
    }

    @Test
    void test_validateIpv4_emptyValue_invalidResponse() {
        Assertions.assertFalse(ValidationUtil.validateIpv4(""));
    }

    @Test
    void test_validateIpv4_wrongValue_invalidResponse() {
        Assertions.assertFalse(ValidationUtil.validateIpv4("260.10.10.5"));
    }

    @Test
    void test_validateIpv4_validResponse() {
        Assertions.assertTrue(ValidationUtil.validateIpv4("0.0.0.0"));
    }

}
