package com.capco.featureservice.domain.ports;

import com.capco.featureservice.config.security.Permission;
import com.capco.featureservice.domain.model.FeatureFlagEntity;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FeatureFlagRepository extends JpaRepository<FeatureFlagEntity, Long> {

    List<FeatureFlagEntity> findAllByActivePermission_nameInOrGlobalStateIsTrue(Set<Permission> names);

    Optional<FeatureFlagEntity> findByName(String name);
}
