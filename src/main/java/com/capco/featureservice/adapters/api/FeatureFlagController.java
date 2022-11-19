package com.capco.featureservice.adapters.api;

import com.capco.featureservice.adapters.api.dto.FeatureFlagId;
import com.capco.featureservice.adapters.api.dto.FeatureFlagRequest;
import com.capco.featureservice.adapters.api.dto.FullFeatureFlagResponse;
import com.capco.featureservice.application.service.FeatureFlagService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/feature-flags")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class FeatureFlagController {

    private final FeatureFlagService featureFlagService;

    @PostMapping
    public ResponseEntity<FeatureFlagId> create(@Valid @RequestBody FeatureFlagRequest request) {
        Long id = this.featureFlagService.save(request);
        return ResponseEntity.ok().body(new FeatureFlagId(id));
    }

    @GetMapping("/{id}")
    ResponseEntity<FullFeatureFlagResponse> get(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(this.featureFlagService.find(id));
    }

    @GetMapping
    public ResponseEntity<List<FullFeatureFlagResponse>> getAll() {
        return ResponseEntity.ok().body(this.featureFlagService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<FeatureFlagId> update(@PathVariable("id") Long id,
                                                @Valid @RequestBody FeatureFlagRequest request) {
        Long entityId = this.featureFlagService.update(id, request);
        return ResponseEntity.ok().body(new FeatureFlagId(entityId));
    }
}
