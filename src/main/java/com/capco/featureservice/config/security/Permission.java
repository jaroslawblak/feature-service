package com.capco.featureservice.config.security;

import java.util.Arrays;

public enum Permission {
    BASIC("BASIC"),
    FULL("FULL");

    private final String name;

    Permission(String name) {
        this.name = name;
    }

    public static Permission fromName(String name) {
        return Arrays.stream(Permission.values())
                .filter(permission -> permission.name().equals(name))
                .findFirst()
                .orElseThrow();
    }
}
