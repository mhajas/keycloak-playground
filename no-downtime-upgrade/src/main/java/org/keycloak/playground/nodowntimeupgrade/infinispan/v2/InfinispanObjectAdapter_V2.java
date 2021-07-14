package org.keycloak.playground.nodowntimeupgrade.infinispan.v2;

import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V2;

public class InfinispanObjectAdapter_V2 implements ObjectModel_V2 {

    private final InfinispanObjectEntity entity;

    public InfinispanObjectAdapter_V2(InfinispanObjectEntity entity) {
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
        if (entity.clientScopeId.startsWith(TEMPLATE_PREFIX)) {
            return entity.clientScopeId.substring(TEMPLATE_PREFIX.length());
        }

        return null;
    }

    @Override
    public void setClientTemplateId(String clientTemplateId) {
        entity.clientScopeId = TEMPLATE_PREFIX + clientTemplateId;
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
    public String getNode2() {
        return entity.node2;
    }

    @Override
    public void setNode2(String node2) {
        entity.node2 = node2;
    }


}
