package org.keycloak.playground.nodowntimeupgrade.infinispan.v3;

import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V3;

public class InfinispanObjectAdapter_V3 implements ObjectModel_V3 {

    private final ObjectEntity_V3 entity;

    public InfinispanObjectAdapter_V3(ObjectEntity_V3 entity) {
        this.entity = entity;
    }

    @Override
    public String getId() {
        return entity.getId();
    }

    @Override
    public String getName() {
        return entity.getName();
    }

    @Override
    public void setName(String name) {
        entity.setName(name);
    }

    @Override
    public String getClientScopeId() {
        return entity.getClientScopeId();
    }

    @Override
    public void setClientScopeId(String clientScopeId) {
        entity.setClientScopeId(clientScopeId);
    }

    @Override
    public int getTimeout() {
        return entity.getTimeout();
    }

    @Override
    public void setTimeout(int timeout) {
        entity.setTimeout(timeout);
    }
}
