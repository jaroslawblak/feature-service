package com.capco.featureservice.application.service;

import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.capco.featureservice.adapters.api.dto.FeatureFlagRequest;
import com.capco.featureservice.adapters.api.dto.FullFeatureFlagResponse;
import com.capco.featureservice.config.security.Permission;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class FeatureFlagServiceTest {

    @Autowired
    private FeatureFlagService featureFlagService;

    @ParameterizedTest
    @ValueSource(booleans =  {true, false})
    void shouldCreateNewAsDisableAndWithRoles(boolean state){
        //when
        Long id = featureFlagService.save(new FeatureFlagRequest("test", Set.of(Permission.values()), state));

        //then
        FullFeatureFlagResponse feature = featureFlagService.find(id);
        assertFalse(feature.getFeature().isState());
        assertThat(feature.getPermissions()).isNotEmpty();
        assertThat(feature.getPermissions()).hasSize(Permission.values().length);
    }

    @Test
    void shouldSetAllPermissionsAsDefaults(){
        //when
        Long id = featureFlagService.save(new FeatureFlagRequest("test", emptySet(), false));

        //then
        FullFeatureFlagResponse feature = featureFlagService.find(id);
        assertFalse(feature.getFeature().isState());
        assertThat(feature.getPermissions()).isNotEmpty();
        assertThat(feature.getPermissions()).hasSize(Permission.values().length);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void shouldNotCreateWithInvalidName(String name){

        assertThrows(IllegalArgumentException.class,
                () -> featureFlagService.save(new FeatureFlagRequest(name, emptySet(), false)));

    }

    @Test
    void shouldUpdateFeatureAndRemainOldName(){
        Long id = featureFlagService.save(new FeatureFlagRequest("test", Set.of(Permission.values()), false));

        FeatureFlagRequest update = new FeatureFlagRequest("new test", Set.of(Permission.BASIC), true);
        Long IdOfUpdated = featureFlagService.update(id, update);

        FullFeatureFlagResponse feature = featureFlagService.find(IdOfUpdated);
        assertTrue(feature.getFeature().isState());
        assertThat(feature.getPermissions()).isNotEmpty();
        assertThat(feature.getPermissions()).hasSize(1);
        assertThat(feature.getPermissions()).containsExactly(Permission.BASIC);

        assertThat(feature.getFeature().getName()).isEqualTo("test");
    }

}
