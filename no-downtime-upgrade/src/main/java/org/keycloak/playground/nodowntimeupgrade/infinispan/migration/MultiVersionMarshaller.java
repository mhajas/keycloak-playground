package org.keycloak.playground.nodowntimeupgrade.infinispan.migration;

import org.infinispan.protostream.ProtobufTagMarshaller;
import org.infinispan.protostream.TagReader;
import org.infinispan.protostream.TagWriter;
import org.keycloak.playground.nodowntimeupgrade.base.model.ModelVersion;

import java.io.IOException;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class MultiVersionMarshaller<EntityType> implements ProtobufTagMarshaller<EntityType> {

    public final Class<? extends EntityType> entityClass;
    public final Supplier<EntityType> entitySupplier;
    public final ModelVersion marshallerForVersion;
    public final Field[] fieldsToWrite;
    public final Map<Integer, BiConsumer<EntityType, Object>> setters;
    public final Map<Field, Function<EntityType, Object>> getters;

    public MultiVersionMarshaller(Class<? extends EntityType> entityClass, Supplier<EntityType> entitySupplier, ModelVersion marshallerForVersion, Field[] fieldsToWrite, Map<Integer, BiConsumer<EntityType, Object>> setters, Map<Field, Function<EntityType, Object>> getters) {
        this.entityClass = entityClass;
        this.entitySupplier = entitySupplier;
        this.marshallerForVersion = marshallerForVersion;
        this.fieldsToWrite = fieldsToWrite;
        this.setters = setters;
        this.getters = getters;
    }

    @Override
    public EntityType read(ReadContext ctx) throws IOException {
        TagReader reader = ctx.getReader();

        int tag = reader.readTag();
        if (tag != 8) {
            throw new IllegalStateException("Not able to find entityVersion in infinispan");
        }

        int entityVersion = reader.readInt32();

        if (entityVersion > marshallerForVersion.getVersion() + 1) {
            throw new IllegalStateException("Unable to read version " + entityVersion + " with marshaller for " + marshallerForVersion.getVersion());
        }

        EntityType result = entitySupplier.get();

        tag = reader.readTag();
        while (tag != 0) {
            Readers.Reader<?> readerForField = Readers.getReaderForField(tag, marshallerForVersion);

            if (entityVersion < marshallerForVersion.getVersion()) {
                readerForField = Migrators.updateReaderToVersion(readerForField, tag, entityVersion, marshallerForVersion.getVersion());
            }


            setters.get(tag).accept(result, readerForField.read(reader));
            tag = reader.readTag();
        }

        return result;
    }

    @Override
    public void write(WriteContext ctx, EntityType entityType) throws IOException {
        TagWriter writer = ctx.getWriter();

        try {
            for (Field field : fieldsToWrite) {
                Writers.Writer<Object> w = Writers.getWriterForField(field, marshallerForVersion);
                w.write(writer, field.getNumber(), getters.get(field).apply(entityType));
            }
        } catch (CustomIOException ex) {
            throw ex.getIOException();
        }
    }

    @Override
    public Class<? extends EntityType> getJavaClass() {
        return entityClass;
    }

    @Override
    public String getTypeName() {
        return "nodowntimeupgrade.InfinispanObjectEntity";
    }
}
