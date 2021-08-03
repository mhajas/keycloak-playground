package org.keycloak.playground.nodowntimeupgrade.infinispan.v2;

import org.infinispan.protostream.ProtobufTagMarshaller;
import org.infinispan.protostream.TagReader;
import org.infinispan.protostream.TagWriter;
import org.keycloak.playground.nodowntimeupgrade.infinispan.Field;

import java.io.IOException;

import static org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V2.TEMPLATE_PREFIX;
import static org.keycloak.playground.nodowntimeupgrade.infinispan.Field.CLIENT_SCOPE_ID;
import static org.keycloak.playground.nodowntimeupgrade.infinispan.Field.CLIENT_TEMPLATE_ID;
import static org.keycloak.playground.nodowntimeupgrade.infinispan.Field.ENTITY_VERSION;
import static org.keycloak.playground.nodowntimeupgrade.infinispan.Field.ID;
import static org.keycloak.playground.nodowntimeupgrade.infinispan.Field.NAME;
import static org.keycloak.playground.nodowntimeupgrade.infinispan.Field.NODE2;
import static org.keycloak.playground.nodowntimeupgrade.infinispan.WriteUtils.writeField;


public class InfinispanObjectEntityTagMarshaller implements ProtobufTagMarshaller<InfinispanObjectEntity> {

    @Override
    public Class<? extends InfinispanObjectEntity> getJavaClass() {
        return InfinispanObjectEntity.class;
    }

    @Override
    public String getTypeName() {
        return "nodowntimeupgrade.InfinispanObjectEntity";
    }

    @Override
    public InfinispanObjectEntity read(ReadContext ctx) throws IOException {
        TagReader reader = ctx.getReader();

        InfinispanObjectEntity o = new InfinispanObjectEntity();

        // Read version
        int tag = reader.readTag();
        if (tag != ENTITY_VERSION.getTagIndex()) {
            throw new IllegalStateException("Entity in storage didn't contain entity version");
        }

        o.entityVersion = reader.readInt32();
        if (o.entityVersion > 3) {
            throw new IllegalStateException("Cannot read entity version > 3 with marshaller for version 2");
        }

        boolean end = false;
        while (!end) {
            tag = reader.readTag();
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
                case CLIENT_TEMPLATE_ID:
                    o.clientScopeId = TEMPLATE_PREFIX + reader.readString();
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

        return o;
    }

    @Override
    public void write(WriteContext ctx, InfinispanObjectEntity infinispanObjectEntity) throws IOException {
        TagWriter writer = ctx.getWriter();

        writeField(writer, ENTITY_VERSION, infinispanObjectEntity.entityVersion);
        writeField(writer, ID, infinispanObjectEntity.id);
        writeField(writer, NAME, infinispanObjectEntity.name);
        writeField(writer, CLIENT_TEMPLATE_ID, infinispanObjectEntity.clientTemplateId);
        writeField(writer, NODE2, infinispanObjectEntity.node2);
        writeField(writer, CLIENT_SCOPE_ID, infinispanObjectEntity.clientScopeId);
    }
}
