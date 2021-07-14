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
package org.keycloak.playground.nodowntimeupgrade;

import org.junit.Assert;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectAdapter_V1;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectAdapter_V2;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V1;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V2;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.keycloak.playground.nodowntimeupgrade.VersionUtil.Constants.INITIAL_INDEX_FACTOR;
import static org.keycloak.playground.nodowntimeupgrade.VersionUtil.Constants.INITIAL_INDEX_V1;
import static org.keycloak.playground.nodowntimeupgrade.VersionUtil.Constants.INITIAL_INDEX_V2;
import static org.keycloak.playground.nodowntimeupgrade.VersionUtil_V4.formatName;
import static org.keycloak.playground.nodowntimeupgrade.base.model.Constants.DEFAULT_V3_TIMEOUT;
import static org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V2.TEMPLATE_PREFIX;

/**
 *
 * @author hmlnarik
 */
public class VersionUtil_V2 implements VersionUtil<ObjectModel_V2> {

    public static final VersionUtil_V2 V2_UTIL = new VersionUtil_V2();

    @Override
    public void assertValid(ObjectModel_V2 model) {
        int originalModelVersion = Integer.valueOf(model.getId()) / INITIAL_INDEX_FACTOR;
        int originalModelIndex = Integer.valueOf(model.getId()) % INITIAL_INDEX_FACTOR;


        switch (originalModelVersion) {
            case 1:
                assertThat(model.getClientScopeId(), is(TEMPLATE_PREFIX + "ct" + (originalModelIndex / 10)));
                break;
            case 2:
                assertThat(model.getClientScopeId(), is(((originalModelIndex % 2 == 0) ? TEMPLATE_PREFIX : "") + "ct" + (originalModelIndex / 10)));
                break;
            default:
                Assert.fail("Unknown model version: " + model);
        }
    }

    @Override
    public ObjectModel_V2 newInstance(int localIndex) {
        ObjectAdapter_V2 model = new ObjectAdapter_V2(String.valueOf(INITIAL_INDEX_V2 + localIndex));
        if (localIndex % 2 == 0) {
            model.setClientScopeId("ct" + (localIndex / 10));
        } else {
            model.setClientScopeId("template-ct" + (localIndex / 10));
        }
        model.setName(formatName(model.getId()));

        return model;
    }

}
