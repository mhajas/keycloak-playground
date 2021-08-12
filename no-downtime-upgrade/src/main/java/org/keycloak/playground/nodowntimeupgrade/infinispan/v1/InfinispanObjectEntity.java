package org.keycloak.playground.nodowntimeupgrade.infinispan.v1;

import org.infinispan.protostream.annotations.ProtoDoc;
import org.infinispan.protostream.annotations.ProtoField;
import org.keycloak.playground.nodowntimeupgrade.base.model.ModelVersion;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V1;


public class InfinispanObjectEntity implements ObjectEntity_V1 {

    @ProtoField(number = 1, required = true)
    @ProtoDoc("@Field(index = Index.NO, store = Store.YES)")
    public int entityVersion = ModelVersion.VERSION_1.getVersion();

    @ProtoField(number = 2, required = true)
    @ProtoDoc("@Field(store = Store.YES)")
    public String id;

    @ProtoField(number = 3)
    @ProtoDoc("@Field(index = Index.YES, store = Store.YES")
    public String name;

    @ProtoField(number = 4)
    @ProtoDoc("@Field(index = Index.YES, store = Store.YES)")
    public String clientTemplateId;

    @ProtoField(number = 5)
    public String node2 = "";

    @Override
    public String getId() {
        return id;
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

    public static InfinispanObjectEntity toEntity(ObjectModel_V1 model) {
        InfinispanObjectEntity infinispanObjectEntity = new InfinispanObjectEntity();
        infinispanObjectEntity.entityVersion = ModelVersion.VERSION_1.getVersion();
        infinispanObjectEntity.id = model.getId();
        infinispanObjectEntity.name = model.getName();
        infinispanObjectEntity.clientTemplateId = model.getClientTemplateId();

        return infinispanObjectEntity;
    }

    public static ObjectModel_V1 toModel(InfinispanObjectEntity entity) {
        return new InfinispanObjectAdapter_V1(entity);
    }

    @Override
    public int getEntityVersion() {
        return entityVersion;
    }
}
