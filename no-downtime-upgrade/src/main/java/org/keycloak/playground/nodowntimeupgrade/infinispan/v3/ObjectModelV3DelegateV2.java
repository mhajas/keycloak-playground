package org.keycloak.playground.nodowntimeupgrade.infinispan.v3;

import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V2;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V3;

import static org.keycloak.playground.nodowntimeupgrade.base.model.Constants.DEFAULT_V3_TIMEOUT;

public class ObjectModelV3DelegateV2 implements ObjectModel_V3 {

    private final ObjectModel_V2 objectModel_V2;
    private int timeout = DEFAULT_V3_TIMEOUT;

    public ObjectModelV3DelegateV2(ObjectModel_V2 objectModel_V2) {
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
    public String getClientScopeId() {
        return objectModel_V2.getClientScopeId();
    }

    @Override
    public void setClientScopeId(String clientScopeId) {
        objectModel_V2.setClientScopeId(clientScopeId);
    }

    @Override
    public int getTimeout() {
        return timeout;
    }

    @Override
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
