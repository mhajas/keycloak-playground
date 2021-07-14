package org.keycloak.playground.nodowntimeupgrade.infinispan.v4;

import org.infinispan.protostream.ProtobufTagMarshaller;
import org.infinispan.protostream.TagReader;
import org.infinispan.protostream.TagWriter;
import org.keycloak.playground.nodowntimeupgrade.base.model.ModelVersion;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V1;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V2;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V3;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V4;
import org.keycloak.playground.nodowntimeupgrade.infinispan.Field;
import org.keycloak.playground.nodowntimeupgrade.infinispan.v1.ObjectModelV1TagMarshaller;
import org.keycloak.playground.nodowntimeupgrade.infinispan.v2.ObjectModelV2TagMarshaller;
import org.keycloak.playground.nodowntimeupgrade.infinispan.v3.ObjectModelV3TagMarshaller;

import java.io.IOException;

import static org.keycloak.playground.nodowntimeupgrade.infinispan.Field.CLIENT_SCOPE_ID;
import static org.keycloak.playground.nodowntimeupgrade.infinispan.Field.ENTITY_VERSION;
import static org.keycloak.playground.nodowntimeupgrade.infinispan.Field.ID;
import static org.keycloak.playground.nodowntimeupgrade.infinispan.Field.NAME;
import static org.keycloak.playground.nodowntimeupgrade.infinispan.Field.TIMEOUT;
import static org.keycloak.playground.nodowntimeupgrade.infinispan.Field.TIMEOUT1;
import static org.keycloak.playground.nodowntimeupgrade.infinispan.Field.TIMEOUT2;
import static org.keycloak.playground.nodowntimeupgrade.infinispan.ReadUtils.getAndAssertVersion;
import static org.keycloak.playground.nodowntimeupgrade.infinispan.ReadUtils.getObjectModelV4;
import static org.keycloak.playground.nodowntimeupgrade.infinispan.WriteUtils.writeField;


public class ObjectModelV4TagMarshaller implements ProtobufTagMarshaller<ObjectModel_V4> {

    @Override
    public Class<? extends ObjectModel_V4> getJavaClass() {
        return ObjectModelV4DelegateV4.class;
    }

    @Override
    public String getTypeName() {
        return "nodowntimeupgrade.InfinispanObjectEntity";
    }

    @Override
    public ObjectModel_V4 read(ReadContext ctx) throws IOException {
        TagReader reader = ctx.getReader();

        int entityVersion = getAndAssertVersion(reader, ModelVersion.VERSION_4);

        switch (entityVersion) {
            case 1:
                ObjectModel_V1 objectModel_v1 = ObjectModelV1TagMarshaller.readModel(reader);
                return getObjectModelV4(objectModel_v1);
            case 2:
                ObjectModel_V2 objectModel_v2 = ObjectModelV2TagMarshaller.readModel(reader);
                return getObjectModelV4(objectModel_v2);
            case 3:
                ObjectModel_V3 objectModel_v3 = ObjectModelV3TagMarshaller.readModel(reader);
                return getObjectModelV4(objectModel_v3);
        }

        return readModel(reader);
    }

    public static ObjectModel_V4 readModel(TagReader reader) throws IOException {
        InfinispanObjectEntity o = new InfinispanObjectEntity();
        o.entityVersion = ModelVersion.VERSION_4.getVersion();

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
                case TIMEOUT1:
                    o.timeout1 = reader.readInt32();
                    break;
                case TIMEOUT2:
                    o.timeout2 = reader.readInt32();
                    break;
                default:
                    System.out.println("No reader for field: " + field);
                    if (!reader.skipField(field.getTagIndex())) {
                        end = true;
                    }
            }
        }

        return new InfinispanObjectAdapter_V4(o);
    }

    @Override
    public void write(WriteContext ctx, ObjectModel_V4 infinispanObjectEntity) throws IOException {
        TagWriter writer = ctx.getWriter();
        writeField(writer, ENTITY_VERSION, ModelVersion.VERSION_4.getVersion());
        writeField(writer, ID, infinispanObjectEntity.getId());
        writeField(writer, NAME, infinispanObjectEntity.getName());
        writeField(writer, CLIENT_SCOPE_ID, infinispanObjectEntity.getClientScopeId());
        writeField(writer, TIMEOUT, infinispanObjectEntity.getTimeout()); // deprecated, remove in next version, to make entity readable for version 3
        writeField(writer, TIMEOUT1, infinispanObjectEntity.getTimeout1());
        writeField(writer, TIMEOUT2, infinispanObjectEntity.getTimeout2());
    }
}
