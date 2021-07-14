package org.keycloak.playground.nodowntimeupgrade.infinispan.v1;

import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V1;

public class InfinispanObjectAdapter_V1 implements ObjectModel_V1 {

    private final InfinispanObjectEntity entity;
    
    public InfinispanObjectAdapter_V1(InfinispanObjectEntity entity) {
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
    public String getClientTemplateId() {
        return entity.clientTemplateId;
    }

    @Override
    public void setClientTemplateId(String clientTemplateId) {
        entity.clientTemplateId = clientTemplateId;
    }

    @Override
    public String getNode2() {
        return entity.node2;
    }

    @Override
    public void setNode2(String node2) {
        entity.node2 = node2;
    }
}
