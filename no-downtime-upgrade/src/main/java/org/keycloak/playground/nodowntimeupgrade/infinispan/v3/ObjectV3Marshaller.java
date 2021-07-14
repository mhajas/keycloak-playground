package org.keycloak.playground.nodowntimeupgrade.infinispan.v3;

import org.infinispan.protostream.MessageMarshaller;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectAdapter_V3;
import org.keycloak.playground.nodowntimeupgrade.infinispan.v3.InfinispanObjectEntity;

import java.io.IOException;

public class ObjectV3Marshaller implements MessageMarshaller<InfinispanObjectEntity>  {

    @Override
    public InfinispanObjectEntity readFrom(ProtoStreamReader reader) throws IOException {
        final int version = reader.readInt("entityVersion");

        if (version > 4) {
            throw new IllegalArgumentException("Cannot read entity version > 4 with marshaller for version 3");
        }

        if (version == 1) {
            final String id = reader.readString("id");
            final String name = reader.readString("name");
            final String clientScopeId = "template-" + reader.readString("clientScopeId");

            InfinispanObjectEntity o = new InfinispanObjectEntity();
            o.entityVersion = version;
            o.id = id;
            o.name = name;
            o.clientScopeId = clientScopeId;

            return o;
        }

        final String id = reader.readString("id");
        final String name = reader.readString("name");
        final String clientScopeId = reader.readString("clientScopeId");
        final int timeout = reader.readInt("timeout");

        InfinispanObjectEntity o = new InfinispanObjectEntity();
        o.entityVersion = version;
        o.id = id;
        o.name = name;
        o.clientScopeId = clientScopeId;
        o.timeout = timeout;

        return o;
    }

    @Override
    public void writeTo(ProtoStreamWriter writer, InfinispanObjectEntity infinispanObjectEntity_v3) throws IOException {
        writer.writeInt("entityVersion", infinispanObjectEntity_v3.entityVersion);
        writer.writeString("id", infinispanObjectEntity_v3.id);
        writer.writeString("name", infinispanObjectEntity_v3.name);
        writer.writeString("clientScopeId", infinispanObjectEntity_v3.clientScopeId);
        writer.writeInt("timeout", infinispanObjectEntity_v3.timeout);
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
