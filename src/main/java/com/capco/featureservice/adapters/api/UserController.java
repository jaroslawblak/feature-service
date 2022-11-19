package com.capco.featureservice.adapters.api;

import com.capco.featureservice.adapters.api.dto.FeatureFlagResponse;
import com.capco.featureservice.application.service.FeatureFlagService;
import java.security.Principal;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final FeatureFlagService featureFlagService;

    @GetMapping("/feature-flags")
    public ResponseEntity<Set<FeatureFlagResponse>> getAllFeatureFlags() {
        return ResponseEntity.ok().body(this.featureFlagService.findByPermissions());
    }
}
