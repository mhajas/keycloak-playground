package org.keycloak.playground.nodowntimeupgrade.infinispan.v3;

import org.infinispan.protostream.ProtobufTagMarshaller;
import org.infinispan.protostream.TagReader;
import org.infinispan.protostream.TagWriter;

import java.io.IOException;

public class ObjectV3TagMarshaller implements ProtobufTagMarshaller<InfinispanObjectEntity> {

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
            System.out.println(tag);
            switch (tag) {
                case 0:
                    end = true;
                    break;
                case 8:
                    o.entityVersion = reader.readInt32();
                    System.out.println(o.entityVersion);
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
                    o.timeout = reader.readInt32();
                    break;
                default:
                    System.out.println("No reader for tag + " + tag);
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
        writer.writeInt32(6, infinispanObjectEntity.timeout);
    }
}
