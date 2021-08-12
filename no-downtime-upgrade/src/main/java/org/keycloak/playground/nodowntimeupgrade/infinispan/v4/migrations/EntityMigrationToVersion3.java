package org.keycloak.playground.nodowntimeupgrade.infinispan.v4.migrations;

import org.keycloak.playground.nodowntimeupgrade.infinispan.v4.ObjectEntity_V4;

import static org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V3.TEMPLATE_PREFIX;

public class EntityMigrationToVersion3 extends ObjectEntityDelegate {

    public EntityMigrationToVersion3(ObjectEntity_V4 delegate) {
        super(delegate);
    }

    @Override
    public String getClientScopeId() {
        if (super.getClientScopeId() == null && super.getClientTemplateId() != null) {
            return TEMPLATE_PREFIX + super.getClientTemplateId();
        }

        return super.getClientScopeId();
    }
}
