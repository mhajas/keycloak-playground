package org.keycloak.playground.nodowntimeupgrade.infinispan.v4;


import org.keycloak.playground.nodowntimeupgrade.infinispan.v3.InfinispanObjectEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class Setters {
    public static Map<Integer, BiConsumer<?, ?>> setters = new HashMap<>();

    static {
        setters.put(34, ((BiConsumer<InfinispanObjectEntity, String>) (o, s) -> o.clientScopeId = s ));
    }

}
