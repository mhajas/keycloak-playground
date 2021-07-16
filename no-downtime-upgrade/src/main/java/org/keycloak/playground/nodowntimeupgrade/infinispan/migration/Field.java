package org.keycloak.playground.nodowntimeupgrade.infinispan.migration;

public enum Field {
    ENTITY_VERSION(1, 8),
    ID(2, 18),
    NAME(3, 26),
    CLIENT_SCOPE_ID(4, 34),
    NODE2(5, 42),
    TIMEOUT1(6, 48),
    TIMEOUT2(7, 56); // TODO

    private final int number;
    private final int tagIndex;

    Field(int number, int tagIndex) {
        this.number = number;
        this.tagIndex = tagIndex;
    }

    public int getNumber() {
        return number;
    }

    public int getTagIndex() {
        return tagIndex;
    }
}
