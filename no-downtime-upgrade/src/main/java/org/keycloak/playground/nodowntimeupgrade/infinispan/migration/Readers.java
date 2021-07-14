package org.keycloak.playground.nodowntimeupgrade.infinispan.migration;

import org.infinispan.protostream.TagReader;
import org.keycloak.playground.nodowntimeupgrade.base.model.ModelVersion;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class Readers {
    public static Map<Field, Map<Integer, Reader<?>>> readers = new EnumMap<>(Field.class);

    @FunctionalInterface
    public interface Reader<T> {
        T read(TagReader reader);

        default Reader<T> migrate(Migrators.Migrator<T> migrator) {
            return migrator.migrate(this);
        }
    }

    static {
        // Version 1 readers
        put(Field.ENTITY_VERSION, ModelVersion.VERSION_1, ExceptionSafeIO::readInt32);
        put(Field.ID, ModelVersion.VERSION_1, ExceptionSafeIO::readString);
        put(Field.NAME, ModelVersion.VERSION_1, ExceptionSafeIO::readString);
        put(Field.CLIENT_SCOPE_ID, ModelVersion.VERSION_1, ExceptionSafeIO::readString);
        put(Field.NODE2, ModelVersion.VERSION_1, ExceptionSafeIO::readString);

        // Version 3 readers
        put(Field.TIMEOUT1, ModelVersion.VERSION_3, ExceptionSafeIO::readInt32);

        // Version 4 readers
        put(Field.TIMEOUT2, ModelVersion.VERSION_4, ExceptionSafeIO::readInt32);
    }

    private static void put(Field field, ModelVersion version, Reader<?> reader) {
        if (!readers.containsKey(field)) {
            readers.put(field, new HashMap<>());
        }
        readers.get(field).put(version.getVersion(), reader);
    }

    public static <T> Reader<T> getReaderForField(Field field, ModelVersion version) {
        Map<Integer, Reader<?>> Reader = readers.get(field);

        return (Readers.Reader<T>) Reader.keySet()
                .stream()
                .filter(i -> i <= version.getVersion())
                .max(Comparator.comparingInt(i -> i))
                .map(i -> Reader.get(i))
                .orElse(null);
    }
}
