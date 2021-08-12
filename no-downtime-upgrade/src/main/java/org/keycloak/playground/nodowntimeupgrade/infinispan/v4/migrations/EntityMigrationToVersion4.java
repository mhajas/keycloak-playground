package org.keycloak.playground.nodowntimeupgrade.infinispan.v4.migrations;

import org.keycloak.playground.nodowntimeupgrade.infinispan.v4.ObjectEntity_V4;

public class EntityMigrationToVersion4 extends EntityMigrationToVersion3 {
    public EntityMigrationToVersion4(ObjectEntity_V4 delegate) {
        super(delegate);
    }

    @Override
    public Integer getTimeout1() {
        if (super.getTimeout1() == null) return super.getTimeout();
        return super.getTimeout1();
    }

    @Override
    public Integer getTimeout2() {
        if (super.getTimeout2() == null) return super.getTimeout();
        return super.getTimeout2();
    }
}
