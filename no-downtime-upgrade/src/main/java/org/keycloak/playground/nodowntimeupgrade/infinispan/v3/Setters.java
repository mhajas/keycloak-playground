package org.keycloak.playground.nodowntimeupgrade.infinispan.v3;


import org.keycloak.playground.nodowntimeupgrade.infinispan.migration.Field;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class Setters {
    public static Map<Field, BiConsumer<InfinispanObjectEntity, Object>> setters = new EnumMap<>(Field.class);

    static {
        setters.put(Field.ID, ((o, s) -> o.id = (String) s));
        setters.put(Field.NAME, ((o, s) -> o.name = (String) s));
        setters.put(Field.CLIENT_SCOPE_ID, ((o, s) -> o.clientScopeId = (String) s));
        setters.put(Field.TIMEOUT1, ((o, s) -> o.timeout = (Integer) s));
    }

}
