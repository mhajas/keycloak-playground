package org.keycloak.playground.nodowntimeupgrade.infinispan.v1;

import org.keycloak.playground.nodowntimeupgrade.infinispan.migration.Field;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Getters {
    public static Map<Field, Function<InfinispanObjectEntity, Object>> getters = new HashMap<>();

    static {
        getters.put(Field.ENTITY_VERSION, ((o) -> o.entityVersion ));
        getters.put(Field.ID, ((o) -> o.id ));
        getters.put(Field.NAME, ((o) -> o.name ));
        getters.put(Field.CLIENT_SCOPE_ID, ((o) -> o.clientTemplateId ));
        getters.put(Field.NODE2, ((o) -> o.node2 ));
    }
}
