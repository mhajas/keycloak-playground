package org.keycloak.playground.nodowntimeupgrade.infinispan.migration;

import org.keycloak.playground.nodowntimeupgrade.base.model.ModelVersion;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import static org.keycloak.playground.nodowntimeupgrade.infinispan.migration.Field.CLIENT_SCOPE_ID;
import static org.keycloak.playground.nodowntimeupgrade.infinispan.migration.Field.NODE2;

public class Migrators {

    public static Map<Integer, Map<Integer, Migrator<?>>> migrators = new HashMap<>();

    static {
        put(CLIENT_SCOPE_ID, ModelVersion.VERSION_3, str -> "template-" + str);

        put(NODE2, ModelVersion.VERSION_2, str -> null);
    }

    private static void putReaderMigrator(Field field, ModelVersion toVersion, Migrator<?> reader) {
        if (!migrators.containsKey(field.getTagIndex())) {
            migrators.put(field.getTagIndex(), new HashMap<>());
        }

        migrators.get(field.getTagIndex()).put(toVersion.getVersion(), reader);
    }

    private static void put(Field field, ModelVersion toVersion, UnaryOperator<Object> valueMigrator) {
        if (!migrators.containsKey(field.getTagIndex())) {
            migrators.put(field.getTagIndex(), new HashMap<>());
        }

        migrators.get(field.getTagIndex()).put(toVersion.getVersion(), valueReader -> ((tagReader) -> valueMigrator.apply(valueReader.read(tagReader))));
    }


    @FunctionalInterface
    public interface Migrator<T> {
        Readers.Reader<T> migrate(Readers.Reader<T> previousReader);
    }

    public static <T> Readers.Reader<T> updateReaderToVersion(Readers.Reader r, Integer tagIndex, Integer fromVersion, Integer toVersion) {
        Map<Integer, Migrator<?>> migratorsForField = migrators.get(tagIndex);

        List<Migrator<?>> migrators = migratorsForField.keySet()
                .stream()
                .filter(i -> i > fromVersion && i <= toVersion)
                .sorted(Comparator.comparingInt(i -> i))
                .map(migratorsForField::get)
                .collect(Collectors.toList());

        for (Migrator<?> migrator : migrators) {
            r = r.migrate(migrator);
        }

        return r;
    }
}
