package org.keycloak.playground.nodowntimeupgrade.infinispan.v4.migrations;

import org.keycloak.playground.nodowntimeupgrade.infinispan.v4.ObjectEntity_V4;

public class ObjectEntityDelegate implements ObjectEntity_V4 {

    private final ObjectEntity_V4 delegate;

    public ObjectEntityDelegate(ObjectEntity_V4 delegate) {
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

    @Override
    public Integer getTimeout1() {
        return delegate.getTimeout1();
    }

    @Override
    public void setTimeout1(Integer timeout1) {
        delegate.setTimeout1(timeout1);
    }

    @Override
    public Integer getTimeout2() {
        return delegate.getTimeout2();
    }

    @Override
    public void setTimeout2(Integer timeout2) {
        delegate.setTimeout2(timeout2);
    }
}
