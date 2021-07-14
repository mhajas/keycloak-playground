package org.keycloak.playground.nodowntimeupgrade.infinispan.v2;

import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V2;

public class ObjectModelV2DelegateV2 implements ObjectModel_V2 {

    private final ObjectModel_V2 objectModel_V2;

    public ObjectModelV2DelegateV2(ObjectModel_V2 objectModel_V2) {
        this.objectModel_V2 = objectModel_V2;
    }

    @Override
    public String getId() {
        return objectModel_V2.getId();
    }

    @Override
    public String getName() {
        return objectModel_V2.getName();
    }

    @Override
    public void setName(String name) {
        objectModel_V2.setName(name);
    }

    @Override
    public String getClientTemplateId() {
        if (getClientScopeId().startsWith(TEMPLATE_PREFIX)) {
            return getClientScopeId().substring(TEMPLATE_PREFIX.length());
        }
        return objectModel_V2.getClientTemplateId();
    }

    @Override
    public void setClientTemplateId(String clientTemplateId) {
        objectModel_V2.setClientTemplateId(clientTemplateId);
    }

    @Override
    public String getNode2() {
        return objectModel_V2.getNode2();
    }

    @Override
    public void setNode2(String node2) {
        objectModel_V2.setNode2(node2);
    }

    @Override
    public String getClientScopeId() {
        return objectModel_V2.getClientScopeId();
    }

    @Override
    public void setClientScopeId(String clientScopeId) {
        objectModel_V2.setClientScopeId(clientScopeId);
    }
}
