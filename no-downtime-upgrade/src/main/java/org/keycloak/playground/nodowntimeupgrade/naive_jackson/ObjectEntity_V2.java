/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.keycloak.playground.nodowntimeupgrade.naive_jackson;

import org.keycloak.playground.nodowntimeupgrade.base.model.ModelVersion;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectAdapter_V2;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectAdapter_V3;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V2;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V3;

import static org.keycloak.playground.nodowntimeupgrade.base.model.Constants.DEFAULT_V3_TIMEOUT;

/**
 *
 * @author hmlnarik
 */
public class ObjectEntity_V2 {

    public int entityVersion = ModelVersion.VERSION_2.getVersion();

    public String id;
    public String name;
    @Deprecated
    public String node2;
    public String clientScopeId;
    public ObjectModel_V2 toModel() {
        final ObjectAdapter_V2 res = new ObjectAdapter_V2(id);
        res.setName(name);
        res.setClientScopeId(clientScopeId);
        res.setNode2(node2);

        return res;
    }

    public static ObjectEntity_V2 fromModel(ObjectModel_V2 object) {
        ObjectEntity_V2 res = new ObjectEntity_V2();
        res.id = object.getId();
        res.name = object.getName();
        res.clientScopeId = object.getClientScopeId();
        res.node2 = object.getNode2();
        return res;
    }

}
