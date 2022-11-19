package com.capco.featureservice.application.service;

import com.capco.featureservice.adapters.api.dto.FeatureFlagRequest;
import com.capco.featureservice.adapters.api.dto.FeatureFlagResponse;
import com.capco.featureservice.adapters.api.dto.FullFeatureFlagResponse;
import com.capco.featureservice.config.security.Permission;
import com.capco.featureservice.domain.model.FeatureFlagEntity;
import com.capco.featureservice.domain.model.PermissionEntity;
import com.capco.featureservice.domain.ports.FeatureFlagRepository;
import com.capco.featureservice.domain.ports.PermissionRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeatureFlagService {

    private final FeatureFlagRepository repository;
    private final PermissionRepository permissionRepository;
    private final SessionService sessionService;

    public List<FullFeatureFlagResponse> findAll() {
        return repository.findAll().stream()
                .map(entity ->
                    new FullFeatureFlagResponse(
                            new FeatureFlagResponse(entity.getId(), entity.getName(), entity.isGlobalState()),
                            entity.getActivePermission().stream().map(PermissionEntity::getName).collect(Collectors.toSet())))
                .collect(Collectors.toList());
    }

    public FullFeatureFlagResponse find(Long id){
        FeatureFlagEntity entity = this.repository.findById(id).orElseThrow();
        return new FullFeatureFlagResponse(
                new FeatureFlagResponse(entity.getId(), entity.getName(), entity.isGlobalState()),
                entity.getActivePermission().stream().map(PermissionEntity::getName).collect(Collectors.toSet()));
    }
    @Transactional
    public Long save(FeatureFlagRequest request) {
        validateFeatureName(request.getName());
        Set<Permission> permissions = request.getPermissions().isEmpty()
                ? Set.of(Permission.values())
                : request.getPermissions();

        Set<PermissionEntity> permissionEntities = permissionRepository.findAllByNameIn(permissions);
        return repository.save(new FeatureFlagEntity(request.getName(), permissionEntities)).getId();
    }

    public Set<FeatureFlagResponse> findByPermissions() {
        Set<Permission> userPermissions = this.sessionService.getPermissions();
        List<FeatureFlagEntity> userActiveFeatures =
                this.repository.findAllByActivePermission_nameInOrGlobalStateIsTrue(userPermissions);

        return userActiveFeatures.stream()
                .map(entity ->
                    new FeatureFlagResponse(entity.getId(), entity.getName(), entity.isGlobalState()))
                .collect(Collectors.toSet());
    }

    @Transactional
    public Long update(Long id, FeatureFlagRequest request) {
        Optional<FeatureFlagEntity> entity = this.repository.findById(id);
        return entity.isPresent()
            ? updateFeature(entity.get(), request)
            : this.save(request);
    }

    private void validateFeatureName(String name) {
        Optional<FeatureFlagEntity> feature = this.repository.findByName(name);
        if (name == null || name.isEmpty() || feature.isPresent()) {
            throw new IllegalArgumentException(String.format(
                    "Could not create feature with provided name %s", name));
        }
    }

    private Long updateFeature(FeatureFlagEntity entity, FeatureFlagRequest request) {
        Set<PermissionEntity> permissions = this.permissionRepository.findAllByNameIn(request.getPermissions());
        entity.changePermissions(permissions);
        if (request.isState()) {
            entity.activate();
        } else {
            entity.deactivate();
        }
        return entity.getId();
    }
}
