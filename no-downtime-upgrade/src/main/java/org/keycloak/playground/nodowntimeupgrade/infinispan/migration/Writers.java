package org.keycloak.playground.nodowntimeupgrade.infinispan.migration;

import org.infinispan.protostream.TagWriter;
import org.keycloak.playground.nodowntimeupgrade.base.model.ModelVersion;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class Writers {

    public static Map<Field, Map<Integer, Writer<?>>> writers = new EnumMap<>(Field.class);

    static {
        // Version 1 writers
        put(Field.ENTITY_VERSION, ModelVersion.VERSION_1, ExceptionSafeIO::writeInt32);
        put(Field.ID, ModelVersion.VERSION_1, ExceptionSafeIO::writeString);
        put(Field.NAME, ModelVersion.VERSION_1, ExceptionSafeIO::writeString);
        put(Field.CLIENT_SCOPE_ID, ModelVersion.VERSION_1, ExceptionSafeIO::writeString);
        put(Field.NODE2, ModelVersion.VERSION_1, ExceptionSafeIO::writeString);

        // Version 3 writers
        put(Field.TIMEOUT1, ModelVersion.VERSION_3, ExceptionSafeIO::writeInt32);

        // Version 4 writers
        put(Field.TIMEOUT2, ModelVersion.VERSION_4, ExceptionSafeIO::writeInt32);
    }

    private static <T> void put(Field field, ModelVersion version, Writer<T> writer) {
        if (!writers.containsKey(field)) {
            writers.put(field, new HashMap<>());
        }

        writers.get(field).put(version.getVersion(), writer);
    }

    @FunctionalInterface
    public interface Writer<T> {
        void write(TagWriter writer, int number, T value);
    }

    public static <T> Writer<T> getWriterForField(Field field, ModelVersion version) {
        Map<Integer, Writer<?>> writersForField = writers.get(field);

        return (Writer<T>) writersForField.keySet()
                .stream()
                .filter(i -> i <= version.getVersion())
                .max(Comparator.comparingInt(i -> i))
                .map(i -> writersForField.get(i))
                .orElseThrow(() -> new RuntimeException("Cannot find writer for field " + field.name()));

    }

}
