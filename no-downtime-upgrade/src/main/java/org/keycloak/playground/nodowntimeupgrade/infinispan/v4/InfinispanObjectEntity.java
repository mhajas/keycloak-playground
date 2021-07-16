package org.keycloak.playground.nodowntimeupgrade.infinispan.v4;

import org.infinispan.protostream.annotations.ProtoDoc;
import org.infinispan.protostream.annotations.ProtoField;
import org.keycloak.playground.nodowntimeupgrade.base.model.HasId;
import org.keycloak.playground.nodowntimeupgrade.base.model.ModelVersion;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectAdapter_V3;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectAdapter_V4;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V3;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V4;

import static org.keycloak.playground.nodowntimeupgrade.base.model.Constants.DEFAULT_V3_TIMEOUT;
import static org.keycloak.playground.nodowntimeupgrade.base.model.Constants.DEFAULT_V4_TIMEOUT1;
import static org.keycloak.playground.nodowntimeupgrade.base.model.Constants.DEFAULT_V4_TIMEOUT2;

public class InfinispanObjectEntity implements HasId<String> {

    @ProtoField(number = 1, required = true)
    @ProtoDoc("@Field(index = Index.NO, store = Store.YES)")
    public int entityVersion = ModelVersion.VERSION_4.getVersion();

    @ProtoField(number = 2, required = true)
    @ProtoDoc("@Field(store = Store.YES)")
    public String id;

    @ProtoField(number = 3)
    @ProtoDoc("@Field(index = Index.NO, store = Store.NO)")
    public String name;

    @ProtoField(number = 4)
    @ProtoDoc("@Field(index = Index.NO, store = Store.NO)")
    public String clientScopeId;

    @ProtoField(number = 6)
    @ProtoDoc("@Field(index = Index.NO, store = Store.NO)")
    public Integer timeout1;

    @ProtoField(number = 7)
    @ProtoDoc("@Field(index = Index.NO, store = Store.NO)")
    public Integer timeout2;

    public ObjectAdapter_V4 toModel() {
        final ObjectAdapter_V4 res = new ObjectAdapter_V4(id);
        res.setName(name);
        res.setClientScopeId(clientScopeId);
        res.setTimeout1(timeout1);
        res.setTimeout2(timeout2);
        return res;
    }

    public static InfinispanObjectEntity fromModel(ObjectModel_V4 object) {
        InfinispanObjectEntity res = new InfinispanObjectEntity();
        res.id = object.getId();
        res.name = object.getName();
        res.clientScopeId = object.getClientScopeId();
        res.timeout1 = object.getTimeout1();
        res.timeout2 = object.getTimeout2();

        return res;
    }

    @Override
    public String getId() {
        return id;
    }
}
