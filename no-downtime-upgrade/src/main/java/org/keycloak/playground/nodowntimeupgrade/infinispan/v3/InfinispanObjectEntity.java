package org.keycloak.playground.nodowntimeupgrade.infinispan.v3;

import org.infinispan.protostream.annotations.ProtoDoc;
import org.infinispan.protostream.annotations.ProtoField;
import org.keycloak.playground.nodowntimeupgrade.base.model.HasId;
import org.keycloak.playground.nodowntimeupgrade.base.model.ModelVersion;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectAdapter_V3;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V3;

import static org.keycloak.playground.nodowntimeupgrade.base.model.Constants.DEFAULT_V3_TIMEOUT;

public class InfinispanObjectEntity implements HasId<String> {

    @ProtoField(number = 1, required = true)
    @ProtoDoc("@Field(index = Index.NO, store = Store.YES)")
    public int entityVersion = ModelVersion.VERSION_3.getVersion();

    @ProtoField(number = 2)
    @ProtoDoc("@Field(store = Store.YES)")
    public String id;

    @ProtoField(number = 3)
    @ProtoDoc("@Field(index = Index.NO, store = Store.NO)")
    public String name;

    @ProtoField(number = 4)
    @ProtoDoc("@Field(index = Index.NO, store = Store.NO)")
    public String clientScopeId;

    @ProtoField(number = 6, defaultValue = "" + DEFAULT_V3_TIMEOUT)
    public int timeout;

    public ObjectAdapter_V3 toModel() {
        final ObjectAdapter_V3 res = new ObjectAdapter_V3(id);
        res.setName(name);
        res.setClientScopeId(clientScopeId);
        res.setTimeout(timeout);
        return res;
    }

    public static InfinispanObjectEntity fromModel(ObjectModel_V3 object) {
        InfinispanObjectEntity res = new InfinispanObjectEntity();
        res.id = object.getId();
        res.name = object.getName();
        res.clientScopeId = object.getClientScopeId();
        res.timeout = object.getTimeout();

        return res;
    }

    @Override
    public String getId() {
        return id;
    }
}
