package org.keycloak.playground.nodowntimeupgrade.infinispan.v1;

import org.infinispan.protostream.MessageMarshaller;

import java.io.IOException;

public class ObjectV1Marshaller implements MessageMarshaller<InfinispanObjectEntity>  {

    @Override
    public InfinispanObjectEntity readFrom(ProtoStreamReader reader) throws IOException {
        final int version = reader.readInt("entityVersion");

        if (version > 2) {
            throw new IllegalArgumentException("Cannot read entity version > 2 with marshaller for version 1");
        }

        final String id = reader.readString("id");
        final String name = reader.readString("name");
        final String clientTemplateId = reader.readString("clientTemplateId");
        final String node2 = reader.readString("node2");
        InfinispanObjectEntity o = new InfinispanObjectEntity();
        o.entityVersion = version;
        o.id = id;
        o.name = name;
        o.clientTemplateId = clientTemplateId;
        o.node2 = node2;

        return o;
    }

    @Override
    public void writeTo(ProtoStreamWriter writer, InfinispanObjectEntity infinispanObjectEntity) throws IOException {
        writer.writeInt("entityVersion", infinispanObjectEntity.entityVersion);
        writer.writeString("id", infinispanObjectEntity.id);
        writer.writeString("name", infinispanObjectEntity.name);
        writer.writeString("clientTemplateId", infinispanObjectEntity.clientTemplateId);
        writer.writeString("node2", infinispanObjectEntity.node2);
    }

    @Override
    public Class<? extends InfinispanObjectEntity> getJavaClass() {
        return InfinispanObjectEntity.class;
    }

    @Override
    public String getTypeName() {
        return "nodowntimeupgrade.InfinispanObjectEntity";
    }
}
