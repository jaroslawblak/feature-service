package com.capco.featureservice.domain.model;

import com.capco.featureservice.config.security.Permission;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "feature_flag")
@Getter
@NoArgsConstructor
public class FeatureFlagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "feature_flag_seq")
    @SequenceGenerator(name="feature_flag_seq", sequenceName="feature_flag_seq")
    private Long id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private boolean globalState;

    @ManyToMany
    @JoinTable(
            name = "FeatureFlag_Permission",
            joinColumns = { @JoinColumn(name = "feature_flag_id") },
            inverseJoinColumns = { @JoinColumn(name = "permission_id") }
    )
    private Set<PermissionEntity> activePermission = new HashSet<>();

    public FeatureFlagEntity(String name, Set<PermissionEntity> activePermission) {
        this.name = name;
        this.activePermission = activePermission;
    }

    public void changePermissions(Set<PermissionEntity> newPermissions) {
        this.activePermission.forEach(permissionEntity -> permissionEntity.getFeatures().remove(this));
        this.activePermission = newPermissions;
    }

    public void activate() {
        this.globalState = true;
    }

    public void deactivate() {
        this.globalState = false;
    }
}
