package org.keycloak.playground.nodowntimeupgrade.infinispan;

import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V1;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V3;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V4;
import org.keycloak.playground.nodowntimeupgrade.base.storage.Storage;
import org.keycloak.playground.nodowntimeupgrade.base.storage.VersionedStorage;
import org.keycloak.playground.nodowntimeupgrade.infinispan.v1.InfinispanObjectEntity;

public class InfinispanVersionedStorage implements VersionedStorage {

    public static final String CACHE_NAME = "myObjectsCache";
    public static final Storage<ObjectModel_V1> storageV1 = new InfinispanStorage<>(
            InfinispanObjectEntity.class,
            InfinispanObjectEntity::toModel, InfinispanObjectEntity::toEntity);

    public static final Storage<ObjectModel_V3> storageV3 = new InfinispanStorage<>(
            org.keycloak.playground.nodowntimeupgrade.infinispan.v3.InfinispanObjectEntity.class,
            org.keycloak.playground.nodowntimeupgrade.infinispan.v3.InfinispanObjectEntity::toModel,
            org.keycloak.playground.nodowntimeupgrade.infinispan.v3.InfinispanObjectEntity::toEntity
        );
    public static final Storage<ObjectModel_V4> storageV4 = new InfinispanStorage<>(
            org.keycloak.playground.nodowntimeupgrade.infinispan.v4.InfinispanObjectEntity.class,
            org.keycloak.playground.nodowntimeupgrade.infinispan.v4.InfinispanObjectEntity::toModel,
            org.keycloak.playground.nodowntimeupgrade.infinispan.v4.InfinispanObjectEntity::toEntity
        );

    @Override
    public Storage<ObjectModel_V1> getStorageV1() {
        return storageV1;
    }

    @Override
    public Storage<ObjectModel_V3> getStorageV3() {
        return storageV3;
    }

    @Override
    public Storage<ObjectModel_V4> getStorageV4() {
        return storageV4;
    }

    @Override
    public void cleanup() {
        storageV4.keys().forEach(storageV4::delete);
    }
}
