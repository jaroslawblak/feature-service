package com.capco.featureservice.adapters.api.dto;

import com.capco.featureservice.config.security.Permission;
import java.util.Set;
import lombok.Value;

@Value
public class FullFeatureFlagResponse {

    FeatureFlagResponse feature;
    Set<Permission> permissions;
}
