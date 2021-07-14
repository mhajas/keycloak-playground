package org.keycloak.playground.nodowntimeupgrade.infinispan.migration;

import org.keycloak.playground.nodowntimeupgrade.base.model.ModelVersion;
import org.keycloak.playground.nodowntimeupgrade.infinispan.v4.InfinispanObjectEntity;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import static org.keycloak.playground.nodowntimeupgrade.infinispan.migration.Field.CLIENT_SCOPE_ID;
import static org.keycloak.playground.nodowntimeupgrade.infinispan.migration.Field.NODE2;

public class Migrators {

    public static Map<Field, Map<Integer, Migrator<?>>> migrators = new EnumMap<>(Field.class);
    public static Map<Integer, EntityMigrator<Object>> entityMigrators = new HashMap<>();

    static {
        put(CLIENT_SCOPE_ID, ModelVersion.VERSION_3, str -> "template-" + str);
        put(NODE2, ModelVersion.VERSION_2, str -> null);

        putEntityMigrator(ModelVersion.VERSION_4, entity -> {
            InfinispanObjectEntity oEntity = (InfinispanObjectEntity) entity;
            oEntity.timeout2 = oEntity.timeout1;
            return oEntity;
        });
    }

    private static void putEntityMigrator(ModelVersion toVersion, EntityMigrator<Object> migrator) {
        entityMigrators.put(toVersion.getVersion(), migrator);
    }

    private static void put(Field field, ModelVersion toVersion, UnaryOperator<Object> valueMigrator) {
        if (!migrators.containsKey(field)) {
            migrators.put(field, new HashMap<>());
        }

        migrators.get(field).put(toVersion.getVersion(), valueReader -> ((tagReader) -> valueMigrator.apply(valueReader.read(tagReader))));
    }


    @FunctionalInterface
    public interface Migrator<T> {
        Readers.Reader<T> migrate(Readers.Reader<T> previousReader);
    }

    @FunctionalInterface
    public interface EntityMigrator<T> {
        T migrate(T previousValue);

        default EntityMigrator<T> andThen(EntityMigrator<T> nextMigrator) {
            return previousValue -> nextMigrator.migrate(this.migrate(previousValue));
        }
    }

    public static <T> Readers.Reader<T> updateReaderToVersion(Readers.Reader r, Field field, Integer fromVersion, Integer toVersion) {
        Map<Integer, Migrator<?>> migratorsForField = migrators.get(field);

        if (migratorsForField != null) {
            List<Migrator<?>> migrators = migratorsForField.keySet()
                    .stream()
                    .filter(i -> i > fromVersion && i <= toVersion)
                    .sorted(Comparator.comparingInt(i -> i))
                    .map(migratorsForField::get)
                    .collect(Collectors.toList());

            for (Migrator<?> migrator : migrators) {
                r = r.migrate(migrator);
            }
        }

        return r;
    }

    public static <T> T updateEntityToVersion(ModelVersion fromVersion, ModelVersion toVersion, T entity) {

        return (T) entityMigrators.keySet()
                .stream()
                .filter(i -> i > fromVersion.getVersion() && i <= toVersion.getVersion())
                .map(entityMigrators::get)
                .reduce(x -> x, EntityMigrator::andThen)
                .migrate(entity);
     }
}
