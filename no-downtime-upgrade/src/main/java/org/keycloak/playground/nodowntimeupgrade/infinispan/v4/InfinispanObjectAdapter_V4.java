package org.keycloak.playground.nodowntimeupgrade.infinispan.v4;

import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V4;

public class InfinispanObjectAdapter_V4 implements ObjectModel_V4 {

    private final InfinispanObjectEntity entity;

    public InfinispanObjectAdapter_V4(InfinispanObjectEntity entity) {
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
    public Integer getTimeout1() {
        return entity.timeout1;
    }

    @Override
    public void setTimeout1(Integer timeout1) {
        entity.timeout1 = timeout1;
    }

    @Override
    public Integer getTimeout2() {
        return entity.timeout2;
    }

    @Override
    public void setTimeout2(Integer timeout2) {
        entity.timeout2 = timeout2;
    }
}
