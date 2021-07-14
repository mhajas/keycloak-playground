package org.keycloak.playground.nodowntimeupgrade.infinispan;

import org.keycloak.playground.nodowntimeupgrade.base.model.ModelVersion;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V1;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V3;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V4;
import org.keycloak.playground.nodowntimeupgrade.base.storage.Storage;
import org.keycloak.playground.nodowntimeupgrade.base.storage.VersionedStorage;
import org.keycloak.playground.nodowntimeupgrade.infinispan.migration.MultiVersionMarshaller;
import org.keycloak.playground.nodowntimeupgrade.infinispan.v1.Getters;
import org.keycloak.playground.nodowntimeupgrade.infinispan.v1.InfinispanObjectEntity;
import org.keycloak.playground.nodowntimeupgrade.infinispan.v1.Setters;

public class InfinispanVersionedStorage implements VersionedStorage {

    public static final String CACHE_NAME = "myObjectsCache";

    @Override
    public Storage<ObjectModel_V1> getStorageV1() {

        return new InfinispanStorage<>(
                InfinispanObjectEntity.class,
                new MultiVersionMarshaller<>(InfinispanObjectEntity.class,
                        InfinispanObjectEntity::new,
                        ModelVersion.VERSION_1,
                        Setters.setters,
                        Getters.getters),
                InfinispanObjectEntity::fromModel,
                InfinispanObjectEntity::toModel
                );
    }

    @Override
    public Storage<ObjectModel_V3> getStorageV3() {
        return new InfinispanStorage<>(
                org.keycloak.playground.nodowntimeupgrade.infinispan.v3.InfinispanObjectEntity.class,
                new MultiVersionMarshaller<>(
                        org.keycloak.playground.nodowntimeupgrade.infinispan.v3.InfinispanObjectEntity.class,
                        org.keycloak.playground.nodowntimeupgrade.infinispan.v3.InfinispanObjectEntity::new,
                        ModelVersion.VERSION_3,
                        org.keycloak.playground.nodowntimeupgrade.infinispan.v3.Setters.setters,
                        org.keycloak.playground.nodowntimeupgrade.infinispan.v3.Getters.getters
                ),
                org.keycloak.playground.nodowntimeupgrade.infinispan.v3.InfinispanObjectEntity::fromModel,
                org.keycloak.playground.nodowntimeupgrade.infinispan.v3.InfinispanObjectEntity::toModel
        );
    }

    @Override
    public Storage<ObjectModel_V4> getStorageV4() {
        return new InfinispanStorage<>(
                org.keycloak.playground.nodowntimeupgrade.infinispan.v4.InfinispanObjectEntity.class,
                new MultiVersionMarshaller<>(
                        org.keycloak.playground.nodowntimeupgrade.infinispan.v4.InfinispanObjectEntity.class,
                        org.keycloak.playground.nodowntimeupgrade.infinispan.v4.InfinispanObjectEntity::new,
                        ModelVersion.VERSION_4,
                        org.keycloak.playground.nodowntimeupgrade.infinispan.v4.Setters.setters,
                        org.keycloak.playground.nodowntimeupgrade.infinispan.v4.Getters.getters
                ),
                org.keycloak.playground.nodowntimeupgrade.infinispan.v4.InfinispanObjectEntity::fromModel,
                org.keycloak.playground.nodowntimeupgrade.infinispan.v4.InfinispanObjectEntity::toModel
        );
    }

    @Override
    public void cleanup() {

    }
}
