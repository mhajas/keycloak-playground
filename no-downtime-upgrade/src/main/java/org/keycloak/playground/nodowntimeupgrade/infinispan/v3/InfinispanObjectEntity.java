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

    @ProtoField(number = 2, required = true)
    @ProtoDoc("@Field(store = Store.YES)")
    public String id;

    @ProtoField(number = 3)
    @ProtoDoc("@Field(index = Index.YES, store = Store.NO")
    public String name;

    /**
     * This field replaces previous field clientTemplateId
     *  Migration path is following:
     *      To get client scope ID from client template ID,
     *      the template ID needs to be prefixed by {@code "template-"}.
     */
    @ProtoField(number = 4)
    @ProtoDoc("@Field(index = Index.NO, store = Store.NO)")
    public String clientScopeId;

    @ProtoField(number = 6, defaultValue = "" + DEFAULT_V3_TIMEOUT)
    @ProtoDoc("@Field(index = Index.NO, store = Store.NO)")
    public int timeout = DEFAULT_V3_TIMEOUT;

    public ObjectModel_V3 toModel() {
        return new InfinispanObjectAdapter_V3(this);
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
