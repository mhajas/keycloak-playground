package org.keycloak.playground.nodowntimeupgrade.infinispan.v2;

import org.infinispan.protostream.annotations.ProtoDoc;
import org.infinispan.protostream.annotations.ProtoField;
import org.keycloak.playground.nodowntimeupgrade.base.model.HasId;
import org.keycloak.playground.nodowntimeupgrade.base.model.ModelVersion;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectAdapter_V2;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V2;

import static org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V2.TEMPLATE_PREFIX;


public class InfinispanObjectEntity implements HasId<String> {

    @ProtoField(number = 1, required = true)
    @ProtoDoc("@Field(index = Index.NO, store = Store.YES)")
    public int entityVersion = ModelVersion.VERSION_2.getVersion();

    @ProtoField(number = 2, required = true)
    @ProtoDoc("@Field(store = Store.YES)")
    public String id;

    @ProtoField(number = 3)
    @ProtoDoc("@Field(index = Index.YES, store = Store.YES")
    public String name;

    @Deprecated
    @ProtoField(number = 4)
    @ProtoDoc("@Field(index = Index.YES, store = Store.YES)")
    public String clientTemplateId;

    /**
     * @deprecated TODO: For backwards compatibility with version 1, remove in version 3
     */
    @Deprecated
    @ProtoField(number = 5)
    public String node2 = "";

    @ProtoField(number = 6)
    @ProtoDoc("@Field(index = Index.YES, store = Store.YES)")
    public String clientScopeId;

    public ObjectAdapter_V2 toModel() {
        final ObjectAdapter_V2 res = new ObjectAdapter_V2(id);
        res.setName(name);
        res.setClientScopeId(clientScopeId);
        return res;
    }

    public static InfinispanObjectEntity fromModel(ObjectModel_V2 object) {
        InfinispanObjectEntity res = new InfinispanObjectEntity();
        res.id = object.getId();
        res.name = object.getName();
        res.clientScopeId = object.getClientScopeId();
        if (res.clientScopeId.startsWith(TEMPLATE_PREFIX)) { // We need to make sure version 1 is able to read version 2
            res.clientTemplateId = res.clientScopeId.substring(TEMPLATE_PREFIX.length());
        }

        return res;
    }

    @Override
    public String getId() {
        return id;
    }
}
