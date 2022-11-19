package com.capco.featureservice.domain.ports;

import com.capco.featureservice.config.security.Permission;
import com.capco.featureservice.domain.model.PermissionEntity;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {

    Set<PermissionEntity> findAllByNameIn(Set<Permission> permissions);
}
