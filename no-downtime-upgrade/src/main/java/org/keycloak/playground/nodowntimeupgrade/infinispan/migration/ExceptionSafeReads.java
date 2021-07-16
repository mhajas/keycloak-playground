package org.keycloak.playground.nodowntimeupgrade.infinispan.migration;

import org.infinispan.protostream.TagReader;
import org.infinispan.protostream.TagWriter;

import java.io.IOException;

public class ExceptionSafeReads {

    public static Integer readInt32(TagReader reader) {
        try {
            return reader.readInt32();
        } catch (IOException e) {
            throw new CustomIOException(e);
        }
    }

    public static String readString(TagReader reader) {
        try {
            return reader.readString();
        } catch (IOException e) {
            throw new CustomIOException(e);
        }
    }

    public static void writeString(TagWriter writer, int number, String value) {
        try {
            writer.writeString(number, value);
        } catch (IOException e) {
            throw new CustomIOException(e);
        }
    }

    public static void writeInt32(TagWriter writer, int number, Integer value) {
        try {
            writer.writeInt32(number, value);
        } catch (IOException e) {
            throw new CustomIOException(e);
        }
    }

//    public static void writeString(TagWriter tagWriter, int i, Object o) {
//        try {
//            tagWriter.writeString(i, (String) o);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
