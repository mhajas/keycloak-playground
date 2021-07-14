package org.keycloak.playground.nodowntimeupgrade.infinispan;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V1;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V3;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V4;
import org.keycloak.playground.nodowntimeupgrade.base.storage.Storage;
import org.keycloak.playground.nodowntimeupgrade.base.storage.VersionedStorage;
import org.keycloak.playground.nodowntimeupgrade.infinispan.v1.InfinispanObjectEntity;
import org.keycloak.playground.nodowntimeupgrade.infinispan.v1.ObjectV1Marshaller;
import org.keycloak.playground.nodowntimeupgrade.infinispan.v3.ObjectV3Marshaller;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

public class InfinispanVersionedStorage implements VersionedStorage {

    public static final String CACHE_NAME = "myObjectsCache";

    @Override
    public Storage<ObjectModel_V1> getStorageV1() {

        return new InfinispanStorage<>("ObjectModelV1.proto",
                InfinispanObjectEntity.class,
                new ObjectV1Marshaller(),
                InfinispanObjectEntity::fromModel,
                InfinispanObjectEntity::toModel
                );
    }

    @Override
    public Storage<ObjectModel_V3> getStorageV3() {
        return new InfinispanStorage<>("ObjectModelV3.proto",
                org.keycloak.playground.nodowntimeupgrade.infinispan.v3.InfinispanObjectEntity.class,
                new ObjectV3Marshaller(),
                org.keycloak.playground.nodowntimeupgrade.infinispan.v3.InfinispanObjectEntity::fromModel,
                org.keycloak.playground.nodowntimeupgrade.infinispan.v3.InfinispanObjectEntity::toModel
        );
    }

    @Override
    public Storage<ObjectModel_V4> getStorageV4() {
        return null;
    }

    @Override
    public void cleanup() {

    }
}
