package org.keycloak.playground.nodowntimeupgrade.infinispan.v4;

import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V4;

public class ObjectModelV4DelegateV4 implements ObjectModel_V4 {

    private final ObjectModel_V4 objectModel_V4;

    public ObjectModelV4DelegateV4(ObjectModel_V4 objectModel_V4) {
        this.objectModel_V4 = objectModel_V4;
    }

    @Override
    public String getId() {
        return objectModel_V4.getId();
    }

    @Override
    public String getName() {
        return objectModel_V4.getName();
    }

    @Override
    public void setName(String name) {
        objectModel_V4.setName(name);
    }

    @Override
    public String getClientScopeId() {
        return objectModel_V4.getClientScopeId();
    }

    @Override
    public void setClientScopeId(String clientScopeId) {
        objectModel_V4.setClientScopeId(clientScopeId);
    }

    @Override
    public Integer getTimeout1() {
        return objectModel_V4.getTimeout1();
    }

    @Override
    public void setTimeout1(Integer timeout1) {
        objectModel_V4.setTimeout1(timeout1);
    }

    @Override
    public Integer getTimeout2() {
        return objectModel_V4.getTimeout2();
    }

    @Override
    public void setTimeout2(Integer timeout2) {
        objectModel_V4.setTimeout2(timeout2);
    }


}
