package org.keycloak.playground.nodowntimeupgrade.infinispan.v4;

import org.infinispan.protostream.ProtobufTagMarshaller;
import org.infinispan.protostream.TagReader;
import org.infinispan.protostream.TagWriter;

import java.io.IOException;

public class ObjectV4TagMarshaller implements ProtobufTagMarshaller<InfinispanObjectEntity> {

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
                    if (o.entityVersion > 4) {
                        throw new IllegalArgumentException("Cannot read entity version > 4 with marshaller for version 3");
                    }
                    break;
                case 18:
                    o.id = reader.readString();
                    break;
                case 26:
                    o.name = reader.readString();
                    break;
                case 34:
                    o.clientScopeId = reader.readString();
                    if (o.entityVersion == 1) {
                        o.clientScopeId = "template-" + o.clientScopeId;
                    }
                    break;
                case 48:
                    o.timeout1 = reader.readInt32();

                    if (o.entityVersion == 3) {
                        o.timeout2 = o.timeout1;
                    }
                    break;
                case 56:
                    o.timeout2 = reader.readInt32();
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
        writer.writeString(4, infinispanObjectEntity.clientScopeId);
        if (infinispanObjectEntity.timeout1 != null) {
            writer.writeInt32(6, infinispanObjectEntity.timeout1);
        }

        if (infinispanObjectEntity.timeout2 != null) {
            writer.writeInt32(7, infinispanObjectEntity.timeout2);
        }
    }
}
