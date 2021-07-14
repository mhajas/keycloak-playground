package org.keycloak.playground.nodowntimeupgrade.infinispan.v4;

import org.keycloak.playground.nodowntimeupgrade.infinispan.migration.Field;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

public class Getters {
    public static Map<Field, Function<InfinispanObjectEntity, Object>> getters = new EnumMap<>(Field.class);

    static {
        getters.put(Field.ENTITY_VERSION, ((o) -> o.entityVersion ));
        getters.put(Field.ID, ((o) -> o.id ));
        getters.put(Field.NAME, ((o) -> o.name ));
        getters.put(Field.CLIENT_SCOPE_ID, ((o) -> o.clientScopeId ));
        getters.put(Field.TIMEOUT1, ((o) -> o.timeout1 ));
        getters.put(Field.TIMEOUT2, ((o) -> o.timeout2 ));
    }
}
