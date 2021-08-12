package org.keycloak.playground.nodowntimeupgrade.infinispan.v3;

import org.keycloak.playground.nodowntimeupgrade.base.model.HasId;
import org.keycloak.playground.nodowntimeupgrade.infinispan.Versioned;

public interface ObjectEntity_V3 extends Versioned, HasId<String> {

    String getName();
    void setName(String name);

    @Deprecated // Can be removed in a major release in the future
    String getClientTemplateId();
    @Deprecated // Can be removed in a major release in the future
    void setClientTemplateId(String clientTemplateId);

    String getClientScopeId();
    void setClientScopeId(String clientScopeId);

    int getTimeout();
    void setTimeout(int timeout);
}
