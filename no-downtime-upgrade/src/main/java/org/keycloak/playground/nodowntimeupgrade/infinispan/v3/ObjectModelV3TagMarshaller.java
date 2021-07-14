package org.keycloak.playground.nodowntimeupgrade.infinispan.v3;

import org.infinispan.protostream.ProtobufTagMarshaller;
import org.infinispan.protostream.TagReader;
import org.infinispan.protostream.TagWriter;
import org.keycloak.playground.nodowntimeupgrade.base.model.ModelVersion;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V1;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V2;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V3;
import org.keycloak.playground.nodowntimeupgrade.infinispan.Field;
import org.keycloak.playground.nodowntimeupgrade.infinispan.v1.ObjectModelV1TagMarshaller;
import org.keycloak.playground.nodowntimeupgrade.infinispan.v2.ObjectModelV2TagMarshaller;

import java.io.IOException;

import static org.keycloak.playground.nodowntimeupgrade.infinispan.Field.CLIENT_SCOPE_ID;
import static org.keycloak.playground.nodowntimeupgrade.infinispan.Field.ENTITY_VERSION;
import static org.keycloak.playground.nodowntimeupgrade.infinispan.Field.ID;
import static org.keycloak.playground.nodowntimeupgrade.infinispan.Field.NAME;
import static org.keycloak.playground.nodowntimeupgrade.infinispan.Field.TIMEOUT;
import static org.keycloak.playground.nodowntimeupgrade.infinispan.ReadUtils.getAndAssertVersion;
import static org.keycloak.playground.nodowntimeupgrade.infinispan.ReadUtils.getObjectModelV3;
import static org.keycloak.playground.nodowntimeupgrade.infinispan.WriteUtils.writeField;


public class ObjectModelV3TagMarshaller implements ProtobufTagMarshaller<ObjectModel_V3> {

    @Override
    public Class<? extends ObjectModel_V3> getJavaClass() {
        return ObjectModelV3DelegateV3.class;
    }

    @Override
    public String getTypeName() {
        return "nodowntimeupgrade.InfinispanObjectEntity";
    }

    @Override
    public ObjectModel_V3 read(ReadContext ctx) throws IOException {
        TagReader reader = ctx.getReader();

        // Read version
        int entityVersion = getAndAssertVersion(reader, ModelVersion.VERSION_3);
        switch (entityVersion) {
            case 1:
                ObjectModel_V1 objectV1 = ObjectModelV1TagMarshaller.readModel(reader);
                return getObjectModelV3(objectV1);
            case 2:
                ObjectModel_V2 objectV2 = ObjectModelV2TagMarshaller.readModel(reader);
                return getObjectModelV3(objectV2);
        }


        return readModel(reader);
    }

    public static ObjectModel_V3 readModel(TagReader reader) throws IOException {
        InfinispanObjectEntity o = new InfinispanObjectEntity();
        o.entityVersion = ModelVersion.VERSION_3.getVersion();

        boolean end = false;
        while (!end) {
            int tag = reader.readTag();
            if (tag == 0) { // End of stream
                break;
            }

            Field field = Field.fromTag(tag);
            if (field == null) {
                throw new IllegalStateException("Unknown tag read from Infinispan: " + tag);
            }

            switch (field) {
                case ID:
                    o.id = reader.readString();
                    break;
                case NAME:
                    o.name = reader.readString();
                    break;
                case CLIENT_SCOPE_ID:
                    o.clientScopeId = reader.readString();
                    break;
                case TIMEOUT:
                    o.timeout = reader.readInt32();
                    break;
                default:
                    System.out.println("No reader for field: " + field);
                    if (!reader.skipField(field.getTagIndex())) {
                        end = true;
                    }
            }
        }

        return new InfinispanObjectAdapter_V3(o);
    }

    @Override
    public void write(WriteContext ctx, ObjectModel_V3 infinispanObjectEntity) throws IOException {
        TagWriter writer = ctx.getWriter();

        writeField(writer, ENTITY_VERSION, ModelVersion.VERSION_3.getVersion());
        writeField(writer, ID, infinispanObjectEntity.getId());
        writeField(writer, NAME, infinispanObjectEntity.getName());
        writeField(writer, CLIENT_SCOPE_ID, infinispanObjectEntity.getClientScopeId());
        writeField(writer, TIMEOUT, infinispanObjectEntity.getTimeout());
    }
}
