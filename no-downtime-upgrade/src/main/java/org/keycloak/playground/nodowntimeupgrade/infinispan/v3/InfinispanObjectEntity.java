package org.keycloak.playground.nodowntimeupgrade.infinispan.v3;

import org.infinispan.protostream.annotations.ProtoDoc;
import org.infinispan.protostream.annotations.ProtoField;
import org.keycloak.playground.nodowntimeupgrade.base.model.ModelVersion;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V3;
import org.keycloak.playground.nodowntimeupgrade.infinispan.v3.migrations.EntityMigrationToVersion3;

import static org.keycloak.playground.nodowntimeupgrade.base.model.Constants.DEFAULT_V3_TIMEOUT;

public class InfinispanObjectEntity implements ObjectEntity_V3 {

    @ProtoField(number = 1, required = true)
    @ProtoDoc("@Field(index = Index.NO, store = Store.YES)")
    public int entityVersion = ModelVersion.VERSION_3.getVersion();

    @ProtoField(number = 2, required = true)
    @ProtoDoc("@Field(store = Store.YES)")
    public String id;

    @ProtoField(number = 3)
    @ProtoDoc("@Field(index = Index.YES, store = Store.NO")
    public String name;

    @Deprecated
    @ProtoField(number = 4)
    @ProtoDoc("@Field(index = Index.YES, store = Store.YES)")
    public String clientTemplateId;

    @ProtoField(number = 6)
    @ProtoDoc("@Field(index = Index.YES, store = Store.NO)")
    public String clientScopeId;

    @ProtoField(number = 7, defaultValue = "" + DEFAULT_V3_TIMEOUT)
    @ProtoDoc("@Field(index = Index.NO, store = Store.NO)")
    public int timeout = DEFAULT_V3_TIMEOUT;

    @Override
    public String getId() {
        return id;
    }

    public static InfinispanObjectEntity toEntity(ObjectModel_V3 model) {
        InfinispanObjectEntity infinispanObjectEntity = new InfinispanObjectEntity();

        infinispanObjectEntity.entityVersion = ModelVersion.VERSION_3.getVersion();
        infinispanObjectEntity.id = model.getId();
        infinispanObjectEntity.name = model.getName();
        infinispanObjectEntity.clientScopeId = model.getClientScopeId();
        infinispanObjectEntity.timeout = model.getTimeout();

        return infinispanObjectEntity;
    }

    public static ObjectModel_V3 toModel(ObjectEntity_V3 entity) {
        if (entity.getEntityVersion() < 3)
            return new InfinispanObjectAdapter_V3(new EntityMigrationToVersion3(entity));

        return new InfinispanObjectAdapter_V3(entity);
    }

    @Override
    public int getEntityVersion() {
        return entityVersion;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getClientTemplateId() {
        return this.clientTemplateId;
    }

    @Override
    public void setClientTemplateId(String clientTemplateId) {
        this.clientTemplateId = clientTemplateId;
    }

    @Override
    public String getClientScopeId() {
        return this.clientScopeId;
    }

    @Override
    public void setClientScopeId(String clientScopeId) {
        this.clientScopeId = clientScopeId;
    }

    @Override
    public int getTimeout() {
        return timeout;
    }

    @Override
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
