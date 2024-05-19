package org.otp.otp.model.dto;

import lombok.Getter;

@Getter
public enum UserType {
    GUEST("Гости", "Guest", "3"),
    PAID("Платные", "Paid", "4"),
    FREE("Бесплатные", "Free", "5"),
    EMPLOYEE("Сотрудники", "Employee", "1"),
    TENANT("Арендаторы", "Tenant", "2");

    private final String ru;
    private final String en;
    private final String code;

    UserType(String ru, String en, String code) {
        this.ru = ru;
        this.en = en;
        this.code = code;
    }

    public static UserType fromValue(String ru) {
        for (UserType userType : UserType.values()) {
            if (userType.getRu().equals(ru)) {
                return userType;
            }
        }
        throw new RuntimeException("UserType is not found");
    }

    public static UserType fromEnValue(String en) {
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
