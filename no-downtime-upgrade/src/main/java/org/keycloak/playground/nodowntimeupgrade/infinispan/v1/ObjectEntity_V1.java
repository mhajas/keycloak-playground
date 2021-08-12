package org.keycloak.playground.nodowntimeupgrade.infinispan.v1;

import org.keycloak.playground.nodowntimeupgrade.base.model.HasId;
import org.keycloak.playground.nodowntimeupgrade.infinispan.Versioned;

public interface ObjectEntity_V1 extends Versioned, HasId<String> {

    String getName();

    void setName(String name);

    String getClientTemplateId();

    void setClientTemplateId(String clientTemplateId);
}
