package org.keycloak.playground.nodowntimeupgrade.infinispan.v4;

import org.keycloak.playground.nodowntimeupgrade.base.model.HasId;
import org.keycloak.playground.nodowntimeupgrade.infinispan.Versioned;

public interface ObjectEntity_V4 extends Versioned, HasId<String> {

    String getName();
    void setName(String name);

    @Deprecated // Can be removed in a major release in the future
    String getClientTemplateId();
    @Deprecated // Can be removed in a major release in the future
    void setClientTemplateId(String clientTemplateId);

    String getClientScopeId();
    void setClientScopeId(String clientScopeId);

    @Deprecated // Can be removed in a major release in the future
    int getTimeout();
    @Deprecated // Can be removed in a major release in the future
    void setTimeout(int timeout);

    Integer getTimeout1();
    void setTimeout1(Integer timeout1);

    Integer getTimeout2();
    void setTimeout2(Integer timeout2);

}
