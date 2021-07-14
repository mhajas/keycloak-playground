package org.keycloak.playground.nodowntimeupgrade.infinispan;

import org.infinispan.protostream.TagWriter;

import java.io.IOException;

public class WriteUtils {

    public static void writeField(TagWriter writer, Field field, Object value) throws IOException {
        if (value instanceof Integer) {
            writer.writeInt32(field.getNumber(), (Integer) value);
        } else if (value instanceof String) {
            writer.writeString(field.getNumber(), (String) value);
        } else if (value != null) {
            throw new IllegalArgumentException("Cannot write argument different from Integer and String");
        }
    }
}
