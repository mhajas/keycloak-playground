package org.keycloak.playground.nodowntimeupgrade.infinispan.v2;

import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V1;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V2;

public class ObjectModelV2DelegateV1 implements ObjectModel_V2 {

    private final ObjectModel_V1 objectV1;
    private String clientScopeId;

    public ObjectModelV2DelegateV1(ObjectModel_V1 objectV1) {
        this.objectV1 = objectV1;
    }

    @Override
    public String getName() {
        return objectV1.getName();
    }

    @Override
    public void setName(String name) {
        objectV1.setName(name);
    }

    @Override
    public String getClientTemplateId() {
        return objectV1.getClientTemplateId();
    }

    @Override
    public void setClientTemplateId(String clientTemplateId) {
        objectV1.setClientTemplateId(clientTemplateId);
    }

    @Override
    public String getClientScopeId() {
        if (clientScopeId == null) {
            return TEMPLATE_PREFIX + objectV1.getClientTemplateId();
        }

        return clientScopeId;
    }

    @Override
    public void setClientScopeId(String clientScopeId) {
        if (clientScopeId.startsWith(TEMPLATE_PREFIX)) {
            objectV1.setClientTemplateId(clientScopeId.substring(TEMPLATE_PREFIX.length()));
        }

        this.clientScopeId = clientScopeId;
    }

    @Override
    public String getNode2() {
        return objectV1.getNode2();
    }

    @Override
    public void setNode2(String node2) {
        objectV1.setNode2(node2);
    }

    @Override
    public String getId() {
        return objectV1.getId();
    }
}
