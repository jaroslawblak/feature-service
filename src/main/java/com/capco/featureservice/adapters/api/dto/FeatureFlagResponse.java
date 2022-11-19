package com.capco.featureservice.adapters.api.dto;

import lombok.Value;

@Value
public class FeatureFlagResponse {

    Long id;
    String name;
    boolean state;
}
