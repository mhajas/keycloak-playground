package org.keycloak.playground.nodowntimeupgrade.infinispan.v3;

import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V3;

public class InfinispanObjectAdapter_V3 implements ObjectModel_V3 {

    private final InfinispanObjectEntity entity;

    public InfinispanObjectAdapter_V3(InfinispanObjectEntity entity) {
        this.entity = entity;
    }

    @Override
    public String getId() {
        return entity.id;
    }

    @Override
    public String getName() {
        return entity.name;
    }

    @Override
    public void setName(String name) {
        entity.name = name;
    }

    @Override
    public String getClientScopeId() {
        return entity.clientScopeId;
    }

    @Override
    public void setClientScopeId(String clientScopeId) {
        entity.clientScopeId = clientScopeId;
    }

    @Override
    public int getTimeout() {
        return entity.timeout;
    }

    @Override
    public void setTimeout(int timeout) {
        entity.timeout = timeout;
    }
}
