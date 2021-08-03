package org.keycloak.playground.nodowntimeupgrade.infinispan.v3;

import org.infinispan.protostream.ProtobufTagMarshaller;
import org.infinispan.protostream.TagReader;
import org.infinispan.protostream.TagWriter;
import org.keycloak.playground.nodowntimeupgrade.infinispan.Field;

import java.io.IOException;

import static org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V2.TEMPLATE_PREFIX;
import static org.keycloak.playground.nodowntimeupgrade.infinispan.Field.CLIENT_SCOPE_ID;
import static org.keycloak.playground.nodowntimeupgrade.infinispan.Field.ENTITY_VERSION;
import static org.keycloak.playground.nodowntimeupgrade.infinispan.Field.ID;
import static org.keycloak.playground.nodowntimeupgrade.infinispan.Field.NAME;
import static org.keycloak.playground.nodowntimeupgrade.infinispan.Field.TIMEOUT;
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
        if (o.entityVersion > 4) {
            throw new IllegalStateException("Cannot read entity version > 4 with marshaller for version 3");
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
                    o.clientScopeId = TEMPLATE_PREFIX + reader.readString(); // migration
                    break;
                case NODE2:
                    // Was removed, ignore
                    reader.skipField(field.getTagIndex());
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

        return o;
    }

    @Override
    public void write(WriteContext ctx, InfinispanObjectEntity infinispanObjectEntity) throws IOException {
        TagWriter writer = ctx.getWriter();

        writeField(writer, ENTITY_VERSION, infinispanObjectEntity.entityVersion);
        writeField(writer, ID, infinispanObjectEntity.id);
        writeField(writer, NAME, infinispanObjectEntity.name);
        writeField(writer, CLIENT_SCOPE_ID, infinispanObjectEntity.clientScopeId);
        writeField(writer, TIMEOUT, infinispanObjectEntity.timeout);
    }
}
