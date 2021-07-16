package org.keycloak.playground.nodowntimeupgrade.infinispan.migration;

import org.infinispan.protostream.TagReader;
import org.keycloak.playground.nodowntimeupgrade.base.model.ModelVersion;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Readers {
    public static Map<Integer, Map<Integer, Reader<?>>> readers = new HashMap<>();

    @FunctionalInterface
    public interface Reader<T> {
        T read(TagReader reader);

        default Reader<T> migrate(Migrators.Migrator<T> migrator) {
            return migrator.migrate(this);
        }
    }

    static {
        // Version 1 readers
        put(Field.ENTITY_VERSION, ModelVersion.VERSION_1, ExceptionSafeReads::readInt32);
        put(Field.ID, ModelVersion.VERSION_1, ExceptionSafeReads::readString);
        put(Field.NAME, ModelVersion.VERSION_1, ExceptionSafeReads::readString);
        put(Field.CLIENT_SCOPE_ID, ModelVersion.VERSION_1, ExceptionSafeReads::readString);
        put(Field.NODE2, ModelVersion.VERSION_1, ExceptionSafeReads::readString);

        // Version 3 readers
        put(Field.TIMEOUT1, ModelVersion.VERSION_3, ExceptionSafeReads::readInt32);

        // Version 4 readers
        put(Field.TIMEOUT2, ModelVersion.VERSION_4, ExceptionSafeReads::readInt32);
    }

    private static void put(Field field, ModelVersion version, Reader<?> reader) {
        if (!readers.containsKey(field.getTagIndex())) {
            readers.put(field.getTagIndex(), new HashMap<>());
        }
        readers.get(field.getTagIndex()).put(version.getVersion(), reader);
    }

    public static <T> Reader<T> getReaderForField(Integer tagIndex, ModelVersion version) {
        Map<Integer, Reader<?>> Reader = readers.get(tagIndex);

        return (Reader<T>) Reader.keySet()
                .stream()
                .filter(i -> i <= version.getVersion())
                .max(Comparator.comparingInt(i -> i))
                .map(i -> Reader.get(i))
                .orElseThrow(() -> new RuntimeException("Cannot find writer for field " + tagIndex));
    }
}
