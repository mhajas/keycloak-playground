package org.keycloak.playground.nodowntimeupgrade.infinispan;

import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V1;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V3;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V4;
import org.keycloak.playground.nodowntimeupgrade.base.storage.Storage;
import org.keycloak.playground.nodowntimeupgrade.base.storage.VersionedStorage;
import org.keycloak.playground.nodowntimeupgrade.infinispan.v1.InfinispanObjectEntity;
import org.keycloak.playground.nodowntimeupgrade.infinispan.v1.ObjectV1TagMarshaller;
import org.keycloak.playground.nodowntimeupgrade.infinispan.v3.ObjectV3TagMarshaller;
import org.keycloak.playground.nodowntimeupgrade.infinispan.v4.ObjectV4TagMarshaller;


public class InfinispanVersionedStorage implements VersionedStorage {

    public static final String CACHE_NAME = "myObjectsCache";

    @Override
    public Storage<ObjectModel_V1> getStorageV1() {

        return new InfinispanStorage<>(
                InfinispanObjectEntity.class,
                new ObjectV1TagMarshaller(),
                InfinispanObjectEntity::fromModel,
                InfinispanObjectEntity::toModel
                );
    }

    @Override
    public Storage<ObjectModel_V3> getStorageV3() {
        return new InfinispanStorage<>(
                org.keycloak.playground.nodowntimeupgrade.infinispan.v3.InfinispanObjectEntity.class,
                new ObjectV3TagMarshaller(),
                org.keycloak.playground.nodowntimeupgrade.infinispan.v3.InfinispanObjectEntity::fromModel,
                org.keycloak.playground.nodowntimeupgrade.infinispan.v3.InfinispanObjectEntity::toModel
        );
    }

    @Override
    public Storage<ObjectModel_V4> getStorageV4() {
        return new InfinispanStorage<>(
                org.keycloak.playground.nodowntimeupgrade.infinispan.v4.InfinispanObjectEntity.class,
                new ObjectV4TagMarshaller(),
                org.keycloak.playground.nodowntimeupgrade.infinispan.v4.InfinispanObjectEntity::fromModel,
                org.keycloak.playground.nodowntimeupgrade.infinispan.v4.InfinispanObjectEntity::toModel
        );
    }

    @Override
    public void cleanup() {

    }
}
