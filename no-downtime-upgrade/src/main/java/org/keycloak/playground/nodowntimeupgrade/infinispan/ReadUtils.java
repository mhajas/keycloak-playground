package org.keycloak.playground.nodowntimeupgrade.infinispan;

import org.infinispan.protostream.TagReader;
import org.keycloak.playground.nodowntimeupgrade.base.model.ModelVersion;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V1;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V2;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V3;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V4;
import org.keycloak.playground.nodowntimeupgrade.infinispan.v2.ObjectModelV2DelegateV1;
import org.keycloak.playground.nodowntimeupgrade.infinispan.v3.ObjectModelV3DelegateV2;
import org.keycloak.playground.nodowntimeupgrade.infinispan.v4.ObjectModelV4DelegateV3;

import java.io.IOException;

import static org.keycloak.playground.nodowntimeupgrade.infinispan.Field.ENTITY_VERSION;

public class ReadUtils {
    
    public static int getAndAssertVersion(TagReader reader, ModelVersion version) throws IOException {
        int tag = reader.readTag();
        if (tag != ENTITY_VERSION.getTagIndex()) {
            throw new IllegalStateException("Entity in storage didn't contain entity version");
        }

        int entityVersion = reader.readInt32();
        if (entityVersion > version.getVersion() + 1) {
            throw new IllegalStateException("Cannot read entity version > " + entityVersion + " with marshaller for version" + version.getVersion());
        }
        
        return entityVersion;
    }

    public static ObjectModel_V2 getObjectModelV2(ObjectModel_V1 objectModel_v1) {
        return new ObjectModelV2DelegateV1(objectModel_v1);
    }


    public static ObjectModel_V3 getObjectModelV3(ObjectModel_V2 objectModel_v2) {
        return new ObjectModelV3DelegateV2(objectModel_v2);
    }

    public static ObjectModel_V3 getObjectModelV3(ObjectModel_V1 objectModel_v1) {
        return getObjectModelV3(getObjectModelV2(objectModel_v1));
    }

    public static ObjectModel_V4 getObjectModelV4(ObjectModel_V3 objectModel_v3) {
        return new ObjectModelV4DelegateV3(objectModel_v3);
    }

    public static ObjectModel_V4 getObjectModelV4(ObjectModel_V2 objectModel_v2) {
        return getObjectModelV4(getObjectModelV3(objectModel_v2));
    }

    public static ObjectModel_V4 getObjectModelV4(ObjectModel_V1 objectModel_v1) {
        return getObjectModelV4(getObjectModelV3(getObjectModelV2(objectModel_v1)));
    }
}
