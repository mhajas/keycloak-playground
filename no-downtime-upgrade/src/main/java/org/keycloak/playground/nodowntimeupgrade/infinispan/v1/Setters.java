package org.keycloak.playground.nodowntimeupgrade.infinispan.v1;

import org.keycloak.playground.nodowntimeupgrade.infinispan.migration.Field;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

public class Setters {
    public static Map<Integer, BiConsumer<InfinispanObjectEntity, Object>> setters = new HashMap<>();

    static {
        setters.put(Field.ID.getTagIndex(), ((o, s) -> o.id = (String) s));
        setters.put(Field.NAME.getTagIndex(), ((o, s) -> o.name = (String) s));
        setters.put(Field.CLIENT_SCOPE_ID.getTagIndex(), ((o, s) -> o.clientTemplateId = (String) s));
        setters.put(Field.NODE2.getTagIndex(), ((o, s) -> o.node2 = (String) s));
    }

}
