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
}
