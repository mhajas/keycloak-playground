package org.keycloak.playground.nodowntimeupgrade.infinispan;

import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V1;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V2;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V3;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V4;
import org.keycloak.playground.nodowntimeupgrade.base.storage.Storage;
import org.keycloak.playground.nodowntimeupgrade.base.storage.VersionedStorage;
import org.keycloak.playground.nodowntimeupgrade.infinispan.v1.InfinispanObjectEntity;
import org.keycloak.playground.nodowntimeupgrade.infinispan.v1.ObjectModelV1TagMarshaller;
import org.keycloak.playground.nodowntimeupgrade.infinispan.v1.ObjectModelV1DelegateV1;
import org.keycloak.playground.nodowntimeupgrade.infinispan.v2.ObjectModelV2TagMarshaller;
import org.keycloak.playground.nodowntimeupgrade.infinispan.v2.ObjectModelV2DelegateV2;
import org.keycloak.playground.nodowntimeupgrade.infinispan.v3.ObjectModelV3TagMarshaller;
import org.keycloak.playground.nodowntimeupgrade.infinispan.v3.ObjectModelV3DelegateV3;
import org.keycloak.playground.nodowntimeupgrade.infinispan.v4.ObjectModelV4DelegateV4;
import org.keycloak.playground.nodowntimeupgrade.infinispan.v4.ObjectModelV4TagMarshaller;

public class InfinispanVersionedStorage implements VersionedStorage {

    public static final String CACHE_NAME = "myObjectsCache";
    public static final Storage<ObjectModel_V1> storageV1 =  new InfinispanStorage<>(
            InfinispanObjectEntity.class,
            new ObjectModelV1TagMarshaller(),
            ObjectModelV1DelegateV1::new
        );
    public static final Storage<ObjectModel_V2> storageV2 = new InfinispanStorage<>(
            org.keycloak.playground.nodowntimeupgrade.infinispan.v2.InfinispanObjectEntity.class,
            new ObjectModelV2TagMarshaller(),
            ObjectModelV2DelegateV2::new
        );
    public static final Storage<ObjectModel_V3> storageV3 = new InfinispanStorage<>(
            org.keycloak.playground.nodowntimeupgrade.infinispan.v3.InfinispanObjectEntity.class,
            new ObjectModelV3TagMarshaller(),
            ObjectModelV3DelegateV3::new
        );
    public static final Storage<ObjectModel_V4> storageV4 = new InfinispanStorage<>(
            org.keycloak.playground.nodowntimeupgrade.infinispan.v4.InfinispanObjectEntity.class,
            new ObjectModelV4TagMarshaller(),
            ObjectModelV4DelegateV4::new
        );

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
        storageV4.keys().forEach(storageV4::delete);
    }
}
