package org.keycloak.playground.nodowntimeupgrade.infinispan.v1;

import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V1;

public class InfinispanObjectAdapter_V1 implements ObjectModel_V1 {

    private final ObjectEntity_V1 entity;
    
    public InfinispanObjectAdapter_V1(ObjectEntity_V1 entity) {
        if (entity.getEntityVersion() > 2) {
            throw new IllegalStateException("Cannot read entity version > 2 with marshaller for version 1");
        }

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
    public String getClientTemplateId() {
        return entity.getClientTemplateId();
    }

    @Override
    public void setClientTemplateId(String clientTemplateId) {
        entity.setClientTemplateId(clientTemplateId);
    }
}
