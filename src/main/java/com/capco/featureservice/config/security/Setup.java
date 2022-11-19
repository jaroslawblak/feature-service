package com.capco.featureservice.config.security;

import com.capco.featureservice.domain.model.PermissionEntity;
import com.capco.featureservice.domain.ports.PermissionRepository;
import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Setup implements ApplicationRunner {

    private final PermissionRepository permissionRepository;

    @Override
    public void run(ApplicationArguments args) {
        permissionRepository.saveAll(
                Arrays.stream(Permission.values())
                        .map(PermissionEntity::new)
                        .collect(Collectors.toSet()));

    }
}
