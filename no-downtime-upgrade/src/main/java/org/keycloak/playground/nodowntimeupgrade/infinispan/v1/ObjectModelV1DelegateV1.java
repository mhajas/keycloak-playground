package org.keycloak.playground.nodowntimeupgrade.infinispan.v1;

import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V1;

public class ObjectModelV1DelegateV1 implements ObjectModel_V1 {
    
    private final ObjectModel_V1 objectModel_v1;

    public ObjectModelV1DelegateV1(ObjectModel_V1 objectModel_v1) {
        this.objectModel_v1 = objectModel_v1;
    }

    @Override
    public String getId() {
        return objectModel_v1.getId();
    }

    @Override
    public String getName() {
        return objectModel_v1.getName();
    }

    @Override
    public void setName(String name) {
        objectModel_v1.setName(name);
    }

    @Override
    public String getClientTemplateId() {
        return objectModel_v1.getClientTemplateId();
    }

    @Override
    public void setClientTemplateId(String clientTemplateId) {
        objectModel_v1.setClientTemplateId(clientTemplateId);
    }

    @Override
    public String getNode2() {
        return objectModel_v1.getNode2();
    }

    @Override
    public void setNode2(String node2) {
        objectModel_v1.setNode2(node2);
    }
}
