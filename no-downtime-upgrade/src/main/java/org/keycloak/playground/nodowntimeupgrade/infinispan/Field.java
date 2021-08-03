package org.keycloak.playground.nodowntimeupgrade.infinispan;

import org.infinispan.protostream.descriptors.WireType;

import java.util.Arrays;

public enum Field {
    ENTITY_VERSION(1, WireType.WIRETYPE_VARINT),
    ID(2, WireType.WIRETYPE_LENGTH_DELIMITED),
    NAME(3, WireType.WIRETYPE_LENGTH_DELIMITED),
    CLIENT_TEMPLATE_ID(4, WireType.WIRETYPE_LENGTH_DELIMITED),
    NODE2(5, WireType.WIRETYPE_LENGTH_DELIMITED),
    CLIENT_SCOPE_ID(6, WireType.WIRETYPE_LENGTH_DELIMITED),
    TIMEOUT(7, WireType.WIRETYPE_VARINT),
    TIMEOUT1(8, WireType.WIRETYPE_VARINT),
    TIMEOUT2(9, WireType.WIRETYPE_VARINT)
    ;

    private final int number;
    private final int tagIndex;

    Field(int number, int wireType) {
        this.number = number;
        this.tagIndex = WireType.makeTag(number, wireType);
    }

    public int getNumber() {
        return number;
    }

    public int getTagIndex() {
        return tagIndex;
    }

    public static Field fromTag(Integer tagIndex) {
        return Arrays.stream(Field.values()).filter(f -> f.getTagIndex() == tagIndex).findFirst().orElse(null);
    }
}
