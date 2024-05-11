package org.otp.otp.model.dto;

import lombok.Getter;

@Getter
public enum UserType {
    GUEST("Гости", "Guest"),
    PAID("Платные", "Paid"),
    FREE("Бесплатные", "Free"),
    EMPLOYEE("Сотрудники", "Employee"),
    TENANT("Арендаторы", "Tenant");

    private final String ru;
    private final String en;

    UserType(String ru, String en) {
        this.ru = ru;
        this.en = en;
    }

    public static UserType fromValue(String ru) {
        System.out.println("value: " + ru);
        for (UserType userType : UserType.values()) {
            if (userType.getRu().equals(ru)) {
                return userType;
            }
        }
        throw new RuntimeException("UserType is not found");
    }

    public static UserType fromEnValue(String en) {
        System.out.println("value: " + en);
        for (UserType userType : UserType.values()) {
            if (userType.getEn().equals(en)) {
                return userType;
            }
        }
        throw new RuntimeException("UserType is not found");
    }

    @Override
    public String toString() {
        return en;
    }
}
