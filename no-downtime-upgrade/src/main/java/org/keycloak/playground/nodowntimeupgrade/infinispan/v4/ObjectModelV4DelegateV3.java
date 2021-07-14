package org.keycloak.playground.nodowntimeupgrade.infinispan.v4;

import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V3;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V4;

public class ObjectModelV4DelegateV3 implements ObjectModel_V4 {

    private final ObjectModel_V3 objectModel_V3;
    private Integer timeout1;
    private Integer timeout2;

    public ObjectModelV4DelegateV3(ObjectModel_V3 objectModel_V3) {
        this.objectModel_V3 = objectModel_V3;
    }

    @Override
    public String getId() {
        return objectModel_V3.getId();
    }

    @Override
    public String getName() {
        return objectModel_V3.getName();
    }

    @Override
    public void setName(String name) {
        objectModel_V3.setName(name);
    }

    @Override
    public String getClientScopeId() {
        return objectModel_V3.getClientScopeId();
    }

    @Override
    public void setClientScopeId(String clientScopeId) {
        objectModel_V3.setClientScopeId(clientScopeId);
    }

    @Override
    public Integer getTimeout1() {
        if (timeout1 == null) return objectModel_V3.getTimeout();
        return timeout1;
    }

    @Override
    public void setTimeout1(Integer timeout1) {
        this.timeout1 = timeout1;
    }

    @Override
    public Integer getTimeout2() {
        if (timeout2 == null) return objectModel_V3.getTimeout();
        return timeout2;
    }

    @Override
    public void setTimeout2(Integer timeout2) {
        this.timeout2 = timeout2;
    }


}
