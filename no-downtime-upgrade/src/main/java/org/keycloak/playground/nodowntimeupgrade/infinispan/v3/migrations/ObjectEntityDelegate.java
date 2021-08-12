package org.keycloak.playground.nodowntimeupgrade.infinispan.v3.migrations;

import org.keycloak.playground.nodowntimeupgrade.infinispan.v3.ObjectEntity_V3;

public class ObjectEntityDelegate implements ObjectEntity_V3 {

    private final ObjectEntity_V3 delegate;

    public ObjectEntityDelegate(ObjectEntity_V3 delegate) {
        this.delegate = delegate;
    }


    @Override
    public String getId() {
        return delegate.getId();
    }

    @Override
    public int getEntityVersion() {
        return delegate.getEntityVersion();
    }

    @Override
    public String getName() {
        return delegate.getName();
    }

    @Override
    public void setName(String name) {
        delegate.setName(name);
    }

    @Override
    public String getClientTemplateId() {
        return delegate.getClientTemplateId();
    }

    @Override
    public void setClientTemplateId(String clientTemplateId) {
        delegate.setClientTemplateId(clientTemplateId);
    }

    @Override
    public String getClientScopeId() {
        return delegate.getClientScopeId();
    }

    @Override
    public void setClientScopeId(String clientScopeId) {
        delegate.setClientScopeId(clientScopeId);
    }

    @Override
    public int getTimeout() {
        return delegate.getTimeout();
    }

    @Override
    public void setTimeout(int timeout) {
        delegate.setTimeout(timeout);
    }
}
