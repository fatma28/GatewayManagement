package com.musalasoft.gateways.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.InetAddressValidator;

public class ValidationUtil {

    private ValidationUtil() {}

    private static final InetAddressValidator validator = InetAddressValidator.getInstance();

    public static boolean validateValue(String value) {
        return StringUtils.isNotBlank(value);
    }

    public static boolean validateIpv4(String value) {
        return validator.isValid(value);
    }
}
