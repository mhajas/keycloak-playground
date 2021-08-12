package org.keycloak.playground.nodowntimeupgrade.infinispan.v4;

import org.infinispan.protostream.annotations.ProtoDoc;
import org.infinispan.protostream.annotations.ProtoField;
import org.keycloak.playground.nodowntimeupgrade.base.model.ModelVersion;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V4;
import org.keycloak.playground.nodowntimeupgrade.infinispan.v4.migrations.EntityMigrationToVersion4;

import static org.keycloak.playground.nodowntimeupgrade.base.model.Constants.DEFAULT_V3_TIMEOUT;

public class InfinispanObjectEntity implements ObjectEntity_V4 {

    @ProtoField(number = 1, required = true)
    @ProtoDoc("@Field(index = Index.NO, store = Store.YES)")
    public int entityVersion = ModelVersion.VERSION_4.getVersion();

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

    @Deprecated
    @ProtoField(number = 5)
    public String node2 = "";

    @ProtoField(number = 6)
    @ProtoDoc("@Field(index = Index.YES, store = Store.NO)")
    public String clientScopeId;

    @Deprecated // Remove in next version
    @ProtoField(number = 7, defaultValue = "" + DEFAULT_V3_TIMEOUT)
    @ProtoDoc("@Field(index = Index.NO, store = Store.NO)")
    public int timeout = DEFAULT_V3_TIMEOUT;

    @ProtoField(number = 8)
    @ProtoDoc("@Field(index = Index.NO, store = Store.NO)")
    public Integer timeout1;

    @ProtoField(number = 9)
    @ProtoDoc("@Field(index = Index.NO, store = Store.NO)")
    public Integer timeout2;

    @Override
    public String getId() {
        return id;
    }

    public static InfinispanObjectEntity toEntity(ObjectModel_V4 model) {
        InfinispanObjectEntity infinispanObjectEntity = new InfinispanObjectEntity();

        infinispanObjectEntity.entityVersion = ModelVersion.VERSION_4.getVersion();
        infinispanObjectEntity.id = model.getId();
        infinispanObjectEntity.name = model.getName();
        infinispanObjectEntity.clientScopeId = model.getClientScopeId();
        infinispanObjectEntity.timeout1 = model.getTimeout1();
        infinispanObjectEntity.timeout2 = model.getTimeout2();

        // We need to make sure version 3 is able to read version 4 entities so we need to write timeout as well
        if (infinispanObjectEntity.timeout1 != null) {
            if (infinispanObjectEntity.timeout2 != null) {
                infinispanObjectEntity.timeout = Math.min(infinispanObjectEntity.timeout1, infinispanObjectEntity.timeout2);
            } else {
                infinispanObjectEntity.timeout = infinispanObjectEntity.timeout1;
            }
        } else if (infinispanObjectEntity.timeout2 != null) {
            infinispanObjectEntity.timeout = infinispanObjectEntity.timeout2;
        }

        return infinispanObjectEntity;
    }

    public static ObjectModel_V4 toModel(ObjectEntity_V4 entity) {
        if (entity.getEntityVersion() < 4) entity = new EntityMigrationToVersion4(entity);

        return new InfinispanObjectAdapter_V4(entity);
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
        return clientTemplateId;
    }

    @Override
    public void setClientTemplateId(String clientTemplateId) {
        this.clientTemplateId = clientTemplateId;
    }

    @Override
    public String getClientScopeId() {
        return clientScopeId;
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

    @Override
    public Integer getTimeout1() {
        return timeout1;
    }

    @Override
    public void setTimeout1(Integer timeout1) {
        this.timeout1 = timeout1;
    }

    @Override
    public Integer getTimeout2() {
        return timeout2;
    }

    @Override
    public void setTimeout2(Integer timeout2) {
        this.timeout2 = timeout2;
    }
}
