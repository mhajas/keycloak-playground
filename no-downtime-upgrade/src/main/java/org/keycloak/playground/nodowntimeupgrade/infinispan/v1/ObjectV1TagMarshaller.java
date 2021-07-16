package org.keycloak.playground.nodowntimeupgrade.infinispan.v1;

import org.infinispan.protostream.ProtobufTagMarshaller;
import org.infinispan.protostream.TagReader;
import org.infinispan.protostream.TagWriter;

import java.io.IOException;

public class ObjectV1TagMarshaller implements ProtobufTagMarshaller<InfinispanObjectEntity> {

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

        boolean end = false;
        while (!end) {
            int tag = reader.readTag();

            switch (tag) {
                case 0:
                    end = true;
                    break;
                case 8:
                    o.entityVersion = reader.readInt32();
                    if (o.entityVersion > 2) {
                        throw new IllegalArgumentException("Cannot read entity version > 2 with marshaller for version 1");
                    }
                    break;
                case 18:
                    o.id = reader.readString();
                    break;
                case 26:
                    o.name = reader.readString();
                    break;
                case 34:
                    o.clientTemplateId = reader.readString();
                    break;
                case 42:
                    o.node2 = reader.readString();
                    break;
                default:
                    if (!reader.skipField(tag)) {
                        end = true;
                    }
            }
        }

        return o;
    }

    @Override
    public void write(WriteContext ctx, InfinispanObjectEntity infinispanObjectEntity) throws IOException {
        TagWriter writer = ctx.getWriter();
        writer.writeInt32(1, infinispanObjectEntity.entityVersion);
        writer.writeString(2, infinispanObjectEntity.id);
        writer.writeString(3, infinispanObjectEntity.name);
        writer.writeString(4, infinispanObjectEntity.clientTemplateId);
        writer.writeString(5, infinispanObjectEntity.node2);
    }
}
