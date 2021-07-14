package org.keycloak.playground.nodowntimeupgrade.infinispan.v3;

import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V3;

public class ObjectModelV3DelegateV3 implements ObjectModel_V3 {

    private final ObjectModel_V3 objectModel_V3;

    public ObjectModelV3DelegateV3(ObjectModel_V3 objectModel_V3) {
        this.objectModel_V3 = objectModel_V3;
    }

    @Override
    public String getId() {
        return objectModel_V3.getId();
    }

    @Override
    public String getName() {
        return objectModel_V3.getName();
    }

    @Override
    public void setName(String name) {
        objectModel_V3.setName(name);
    }

    @Override
    public String getClientScopeId() {
        return objectModel_V3.getClientScopeId();
    }

    @Override
    public void setClientScopeId(String clientScopeId) {
        objectModel_V3.setClientScopeId(clientScopeId);
    }

    @Override
    public int getTimeout() {
        return objectModel_V3.getTimeout();
    }

    @Override
    public void setTimeout(int timeout) {
        objectModel_V3.setTimeout(timeout);
    }
}
