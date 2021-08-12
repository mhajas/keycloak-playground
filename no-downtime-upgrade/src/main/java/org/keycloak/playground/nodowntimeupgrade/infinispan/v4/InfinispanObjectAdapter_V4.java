package org.keycloak.playground.nodowntimeupgrade.infinispan.v4;

import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V4;

public class InfinispanObjectAdapter_V4 implements ObjectModel_V4 {

    private final ObjectEntity_V4 entity;

    public InfinispanObjectAdapter_V4(ObjectEntity_V4 entity) {
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
    public Integer getTimeout1() {
        return entity.getTimeout1();
    }

    @Override
    public void setTimeout1(Integer timeout1) {
        entity.setTimeout1(timeout1);
    }

    @Override
    public Integer getTimeout2() {
        return entity.getTimeout2();
    }

    @Override
    public void setTimeout2(Integer timeout2) {
        entity.setTimeout2(timeout2);
    }
}
