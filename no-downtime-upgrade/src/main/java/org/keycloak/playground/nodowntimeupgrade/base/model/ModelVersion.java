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
package org.keycloak.playground.nodowntimeupgrade.base.model;

/**
 * Version of the logical entity.
 * @author hmlnarik
 */
public enum ModelVersion {

    VERSION_1(1),
    VERSION_2(2),
    VERSION_3(3),
    VERSION_4(4);

    private final int version;

    private ModelVersion(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }

    public static ModelVersion fromVersion(int number) {
        return ModelVersion.values()[number - 1];
    }
}
