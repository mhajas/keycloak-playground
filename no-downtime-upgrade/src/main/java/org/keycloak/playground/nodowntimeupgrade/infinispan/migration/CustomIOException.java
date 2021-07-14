package org.keycloak.playground.nodowntimeupgrade.infinispan.migration;

import java.io.IOException;

public class CustomIOException extends RuntimeException {
    public CustomIOException(IOException cause) {
        super(cause);
    }

    public IOException getIOException() {
        return (IOException) getCause();
    }
}
