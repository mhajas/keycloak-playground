package org.keycloak.playground.nodowntimeupgrade.infinispan;

import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V1;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V3;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V4;
import org.keycloak.playground.nodowntimeupgrade.base.storage.Storage;
import org.keycloak.playground.nodowntimeupgrade.base.storage.VersionedStorage;
import org.keycloak.playground.nodowntimeupgrade.infinispan.v1.InfinispanObjectEntity;
import org.keycloak.playground.nodowntimeupgrade.infinispan.v1.InfinispanObjectEntityTagMarshaller;

public class InfinispanVersionedStorage implements VersionedStorage {

    public static final String CACHE_NAME = "myObjectsCache";

    @Override
    public Storage<ObjectModel_V1> getStorageV1() {
        return new InfinispanStorage<>(
                InfinispanObjectEntity.class,
                new InfinispanObjectEntityTagMarshaller(),
                InfinispanObjectEntity::fromModel,
                InfinispanObjectEntity::toModel
                );
    }

    @Override
    public Storage<ObjectModel_V3> getStorageV3() {
        return new InfinispanStorage<>(
                org.keycloak.playground.nodowntimeupgrade.infinispan.v3.InfinispanObjectEntity.class,
                new org.keycloak.playground.nodowntimeupgrade.infinispan.v3.InfinispanObjectEntityTagMarshaller(),
                org.keycloak.playground.nodowntimeupgrade.infinispan.v3.InfinispanObjectEntity::fromModel,
                org.keycloak.playground.nodowntimeupgrade.infinispan.v3.InfinispanObjectEntity::toModel
        );
    }

    @Override
    public Storage<ObjectModel_V4> getStorageV4() {
        return new InfinispanStorage<>(
                org.keycloak.playground.nodowntimeupgrade.infinispan.v4.InfinispanObjectEntity.class,
                new org.keycloak.playground.nodowntimeupgrade.infinispan.v4.InfinispanObjectEntityTagMarshaller(),
                org.keycloak.playground.nodowntimeupgrade.infinispan.v4.InfinispanObjectEntity::fromModel,
                org.keycloak.playground.nodowntimeupgrade.infinispan.v4.InfinispanObjectEntity::toModel
        );
    }

    @Override
    public void cleanup() {

    }
}
