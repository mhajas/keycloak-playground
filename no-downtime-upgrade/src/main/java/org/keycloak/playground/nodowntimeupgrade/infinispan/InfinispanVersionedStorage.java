package org.keycloak.playground.nodowntimeupgrade.infinispan;

import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V1;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V2;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V3;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V4;
import org.keycloak.playground.nodowntimeupgrade.base.storage.Storage;
import org.keycloak.playground.nodowntimeupgrade.base.storage.VersionedStorage;
import org.keycloak.playground.nodowntimeupgrade.infinispan.v1.InfinispanObjectEntity;
import org.keycloak.playground.nodowntimeupgrade.infinispan.v1.InfinispanObjectEntityTagMarshaller;

public class InfinispanVersionedStorage implements VersionedStorage {

    public static final String CACHE_NAME = "myObjectsCache";
    public static final Storage<ObjectModel_V1> storageV1 =  new InfinispanStorage<>(
            InfinispanObjectEntity.class,
            new InfinispanObjectEntityTagMarshaller(),
            InfinispanObjectEntity::fromModel,
            InfinispanObjectEntity::toModel
        );
    public static final Storage<ObjectModel_V2> storageV2 = new InfinispanStorage<>(
            org.keycloak.playground.nodowntimeupgrade.infinispan.v2.InfinispanObjectEntity.class,
            new org.keycloak.playground.nodowntimeupgrade.infinispan.v2.InfinispanObjectEntityTagMarshaller(),
            org.keycloak.playground.nodowntimeupgrade.infinispan.v2.InfinispanObjectEntity::fromModel,
            org.keycloak.playground.nodowntimeupgrade.infinispan.v2.InfinispanObjectEntity::toModel
        );
    public static final Storage<ObjectModel_V3> storageV3 = new InfinispanStorage<>(
            org.keycloak.playground.nodowntimeupgrade.infinispan.v3.InfinispanObjectEntity.class,
            new org.keycloak.playground.nodowntimeupgrade.infinispan.v3.InfinispanObjectEntityTagMarshaller(),
            org.keycloak.playground.nodowntimeupgrade.infinispan.v3.InfinispanObjectEntity::fromModel,
            org.keycloak.playground.nodowntimeupgrade.infinispan.v3.InfinispanObjectEntity::toModel
        );
    public static final Storage<ObjectModel_V4> storageV4 = null;
//    new InfinispanStorage<>(
//            org.keycloak.playground.nodowntimeupgrade.infinispan.v4.InfinispanObjectEntity.class,
//            new org.keycloak.playground.nodowntimeupgrade.infinispan.v4.InfinispanObjectEntityTagMarshaller(),
//            org.keycloak.playground.nodowntimeupgrade.infinispan.v4.InfinispanObjectEntity::fromModel,
//            org.keycloak.playground.nodowntimeupgrade.infinispan.v4.InfinispanObjectEntity::toModel
//        );

    @Override
    public Storage<ObjectModel_V1> getStorageV1() {
        return storageV1;
    }

    @Override
    public Storage<ObjectModel_V2> getStorageV2() {
        return storageV2;
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
        storageV3.keys().forEach(storageV3::delete);
    }
}
