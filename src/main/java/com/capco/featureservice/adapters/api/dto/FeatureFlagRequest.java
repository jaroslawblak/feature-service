package com.capco.featureservice.adapters.api.dto;

import com.capco.featureservice.config.security.Permission;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;

@Getter
public class FeatureFlagRequest {
    private final String name;
    private final Set<Permission> permissions;
    private final boolean state;

    public FeatureFlagRequest(@JsonProperty("name") String name,
                              @JsonProperty("permissions") Set<Permission> permissions,
                              @JsonProperty("state") boolean state) {
        this.name = name;
        this.permissions = permissions != null ? permissions : new HashSet<>();
        this.state = state;
    }
}
