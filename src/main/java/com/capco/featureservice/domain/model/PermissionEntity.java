package com.capco.featureservice.domain.model;

import com.capco.featureservice.config.security.Permission;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "permission")
@Getter
@NoArgsConstructor
public class PermissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "permission_seq")
    @SequenceGenerator(name="permission_seq", sequenceName="permission_seq")
    private Long id;


    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Permission name;

    @ManyToMany(mappedBy = "activePermission")
    private Set<FeatureFlagEntity> features;

    public PermissionEntity(Permission name) {
        this.name = name;
    }
}
