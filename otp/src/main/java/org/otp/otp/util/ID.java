package org.otp.otp.util;

import java.util.UUID;

public class ID {
    public static String value() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "");
    }
}
