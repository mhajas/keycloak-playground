package org.keycloak.playground.nodowntimeupgrade.infinispan.v1;

import org.infinispan.protostream.annotations.ProtoDoc;
import org.infinispan.protostream.annotations.ProtoField;
import org.keycloak.playground.nodowntimeupgrade.base.model.HasId;
import org.keycloak.playground.nodowntimeupgrade.base.model.ModelVersion;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectAdapter_V1;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V1;


public class InfinispanObjectEntity implements HasId<String> {

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

    public ObjectAdapter_V1 toModel() {
        final ObjectAdapter_V1 res = new ObjectAdapter_V1(id);
        res.setName(name);
        res.setClientTemplateId(clientTemplateId);
        return res;
    }

    public static InfinispanObjectEntity fromModel(ObjectModel_V1 object) {
        InfinispanObjectEntity res = new InfinispanObjectEntity();
        res.id = object.getId();
        res.name = object.getName();
        res.clientTemplateId = object.getClientTemplateId();
        return res;
    }

    @Override
    public String getId() {
        return id;
    }
}
