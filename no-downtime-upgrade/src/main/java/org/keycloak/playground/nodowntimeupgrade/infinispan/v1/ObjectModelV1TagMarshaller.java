package org.keycloak.playground.nodowntimeupgrade.infinispan.v1;

import org.infinispan.protostream.ProtobufTagMarshaller;
import org.infinispan.protostream.TagReader;
import org.infinispan.protostream.TagWriter;
import org.keycloak.playground.nodowntimeupgrade.base.model.ModelVersion;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V1;
import org.keycloak.playground.nodowntimeupgrade.infinispan.Field;

import java.io.IOException;

import static org.keycloak.playground.nodowntimeupgrade.infinispan.Field.CLIENT_TEMPLATE_ID;
import static org.keycloak.playground.nodowntimeupgrade.infinispan.Field.ENTITY_VERSION;
import static org.keycloak.playground.nodowntimeupgrade.infinispan.Field.ID;
import static org.keycloak.playground.nodowntimeupgrade.infinispan.Field.NAME;
import static org.keycloak.playground.nodowntimeupgrade.infinispan.Field.NODE2;
import static org.keycloak.playground.nodowntimeupgrade.infinispan.ReadUtils.getAndAssertVersion;
import static org.keycloak.playground.nodowntimeupgrade.infinispan.WriteUtils.writeField;


public class ObjectModelV1TagMarshaller implements ProtobufTagMarshaller<ObjectModel_V1> {

    @Override
    public Class<? extends ObjectModel_V1> getJavaClass() {
        return ObjectModelV1DelegateV1.class;
    }

    @Override
    public String getTypeName() {
        return "nodowntimeupgrade.InfinispanObjectEntity";
    }

    @Override
    public ObjectModel_V1 read(ReadContext ctx) throws IOException {
        TagReader reader = ctx.getReader();
        
        // Read version
        getAndAssertVersion(reader, ModelVersion.VERSION_1);

        // Read model
        return readModel(reader);
    }
    
    public static ObjectModel_V1 readModel(TagReader reader) throws IOException {
        InfinispanObjectEntity o = new InfinispanObjectEntity();
        o.entityVersion = ModelVersion.VERSION_1.getVersion();
        
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
                case CLIENT_TEMPLATE_ID:
                    o.clientTemplateId = reader.readString();
                    break;
                case NODE2:
                    o.node2 = reader.readString();
                    break;
                default:
                    System.out.println("No reader for field: " + field);
                    if (!reader.skipField(field.getTagIndex())) {
                        end = true;
                    }
            }
        }
        
        return new InfinispanObjectAdapter_V1(o);
    }

    @Override
    public void write(WriteContext ctx, ObjectModel_V1 infinispanObjectEntity) throws IOException {
        TagWriter writer = ctx.getWriter();

        writeField(writer, ENTITY_VERSION, ModelVersion.VERSION_1.getVersion());
        writeField(writer, ID, infinispanObjectEntity.getId());
        writeField(writer, NAME, infinispanObjectEntity.getName());
        writeField(writer, CLIENT_TEMPLATE_ID, infinispanObjectEntity.getClientTemplateId());
        writeField(writer, NODE2, infinispanObjectEntity.getNode2());
    }
}
