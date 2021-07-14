package org.keycloak.playground.nodowntimeupgrade.infinispan.v2;

import org.infinispan.protostream.ProtobufTagMarshaller;
import org.infinispan.protostream.TagReader;
import org.infinispan.protostream.TagWriter;
import org.keycloak.playground.nodowntimeupgrade.base.model.ModelVersion;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V1;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V2;
import org.keycloak.playground.nodowntimeupgrade.infinispan.Field;
import org.keycloak.playground.nodowntimeupgrade.infinispan.v1.ObjectModelV1TagMarshaller;

import java.io.IOException;

import static org.keycloak.playground.nodowntimeupgrade.infinispan.Field.CLIENT_SCOPE_ID;
import static org.keycloak.playground.nodowntimeupgrade.infinispan.Field.CLIENT_TEMPLATE_ID;
import static org.keycloak.playground.nodowntimeupgrade.infinispan.Field.ENTITY_VERSION;
import static org.keycloak.playground.nodowntimeupgrade.infinispan.Field.ID;
import static org.keycloak.playground.nodowntimeupgrade.infinispan.Field.NAME;
import static org.keycloak.playground.nodowntimeupgrade.infinispan.Field.NODE2;
import static org.keycloak.playground.nodowntimeupgrade.infinispan.ReadUtils.getAndAssertVersion;
import static org.keycloak.playground.nodowntimeupgrade.infinispan.ReadUtils.getObjectModelV2;
import static org.keycloak.playground.nodowntimeupgrade.infinispan.WriteUtils.writeField;


public class ObjectModelV2TagMarshaller implements ProtobufTagMarshaller<ObjectModel_V2> {

    @Override
    public Class<? extends ObjectModel_V2> getJavaClass() {
        return ObjectModelV2DelegateV2.class;
    }

    @Override
    public String getTypeName() {
        return "nodowntimeupgrade.InfinispanObjectEntity";
    }

    @Override
    public ObjectModel_V2 read(ReadContext ctx) throws IOException {
        TagReader reader = ctx.getReader();

        int entityVersion = getAndAssertVersion(reader, ModelVersion.VERSION_2);

        if (entityVersion == 1) {
            ObjectModel_V1 objectV1 = ObjectModelV1TagMarshaller.readModel(reader);
            return getObjectModelV2(objectV1);
        }

        return readModel(reader);
    }

    public static ObjectModel_V2 readModel(TagReader reader) throws IOException {
        InfinispanObjectEntity o = new InfinispanObjectEntity();
        o.entityVersion = ModelVersion.VERSION_2.getVersion();

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
                case NODE2:
                    o.node2 = reader.readString();
                    break;
                case CLIENT_SCOPE_ID:
                    o.clientScopeId = reader.readString();
                    break;
                default:
                    System.out.println("No reader for field: " + field);
                    if (!reader.skipField(field.getTagIndex())) {
                        end = true;
                    }
            }
        }

        return new InfinispanObjectAdapter_V2(o);
    }

    @Override
    public void write(WriteContext ctx, ObjectModel_V2 infinispanObjectEntity) throws IOException {
        TagWriter writer = ctx.getWriter();

        writeField(writer, ENTITY_VERSION, ModelVersion.VERSION_2.getVersion());
        writeField(writer, ID, infinispanObjectEntity.getId());
        writeField(writer, NAME, infinispanObjectEntity.getName());
        writeField(writer, CLIENT_TEMPLATE_ID, infinispanObjectEntity.getClientTemplateId());
        writeField(writer, NODE2, infinispanObjectEntity.getNode2());
        writeField(writer, CLIENT_SCOPE_ID, infinispanObjectEntity.getClientScopeId());
    }
}
