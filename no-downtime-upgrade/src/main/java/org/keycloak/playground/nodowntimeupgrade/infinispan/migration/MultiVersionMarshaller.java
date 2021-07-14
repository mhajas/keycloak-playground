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
    public final Map<Field, BiConsumer<EntityType, Object>> setters;
    public final Map<Field, Function<EntityType, Object>> getters;

    public MultiVersionMarshaller(Class<? extends EntityType> entityClass, Supplier<EntityType> entitySupplier, ModelVersion marshallerForVersion, Map<Field, BiConsumer<EntityType, Object>> setters, Map<Field, Function<EntityType, Object>> getters) {
        this.entityClass = entityClass;
        this.entitySupplier = entitySupplier;
        this.marshallerForVersion = marshallerForVersion;
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
            Field field = Field.fromTag(tag);

            Readers.Reader<?> readerForField = Readers.getReaderForField(field, marshallerForVersion);

            if (entityVersion < marshallerForVersion.getVersion()) {
                readerForField = Migrators.updateReaderToVersion(readerForField, field, entityVersion, marshallerForVersion.getVersion());
            }

            if (readerForField != null) {
                Object readFiled = readerForField.read(reader);
                if (readFiled != null) {
                    setters.get(field).accept(result, readFiled);
                }
            } else {
                if (!reader.skipField(tag)) {
                    break;
                }
            }
            tag = reader.readTag();
        }

        if (entityVersion < marshallerForVersion.getVersion()) {
            return Migrators.updateEntityToVersion(ModelVersion.fromVersion(entityVersion), marshallerForVersion, result);
        }

        return result;
    }

    @Override
    public void write(WriteContext ctx, EntityType entityType) throws IOException {
        TagWriter writer = ctx.getWriter();

        try {
            for (Field field : Field.values()) {
                if (getters.containsKey(field)) {
                    Object fieldValue = getters.get(field).apply(entityType);

                    if (fieldValue != null) {
                        Writers.Writer<Object> w = Writers.getWriterForField(field, marshallerForVersion);
                        w.write(writer, field.getNumber(), fieldValue);
                    }
                }
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
