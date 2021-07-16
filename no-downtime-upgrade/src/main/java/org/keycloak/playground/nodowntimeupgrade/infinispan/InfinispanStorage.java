package org.keycloak.playground.nodowntimeupgrade.infinispan;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.Search;
import org.infinispan.client.hotrod.configuration.ClientIntelligence;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.client.hotrod.marshall.MarshallerUtil;
import org.infinispan.commons.marshall.ProtoStreamMarshaller;
import org.infinispan.protostream.BaseMarshaller;
import org.infinispan.protostream.FileDescriptorSource;
import org.infinispan.protostream.SerializationContext;
import org.infinispan.protostream.annotations.ProtoSchemaBuilder;
import org.infinispan.query.dsl.Expression;
import org.infinispan.query.dsl.QueryFactory;
import org.infinispan.query.remote.client.ProtobufMetadataManagerConstants;
import org.keycloak.playground.nodowntimeupgrade.base.model.HasId;
import org.keycloak.playground.nodowntimeupgrade.base.storage.ModelCriteriaBuilder;
import org.keycloak.playground.nodowntimeupgrade.base.storage.Storage;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class InfinispanStorage<ModelType extends HasId<String>, EntityType> implements Storage<ModelType> {

    private final RemoteCacheManager remoteCacheManager;
    private final RemoteCache<String, EntityType> messageCache;
    private final Class<EntityType> entityClass;
    private final BaseMarshaller<?> marshaller;
    private final Function<ModelType, EntityType> toEntity;
    private final Function<EntityType, ModelType> toModel;

    public InfinispanStorage(Class<EntityType> entityClass, BaseMarshaller<?> marshaller, Function<ModelType, EntityType> toEntity, Function<EntityType, ModelType> toModel) {
        ConfigurationBuilder remoteBuilder = new ConfigurationBuilder();
        remoteBuilder.addServer()
                .host("localhost")
                .port(11222)
                .security()
                .authentication()
                .username("Titus Bramble")
                .password("Shambles")
                .realm("default")
                .clientIntelligence(ClientIntelligence.BASIC);
                //.marshaller(new ProtoStreamMarshaller()); // The Protobuf based marshaller is required for query capabilities

        remoteCacheManager = new RemoteCacheManager(remoteBuilder.build());


        RemoteCache<Object, Object> c = remoteCacheManager.getCache();

        messageCache = remoteCacheManager.administration().getOrCreateCache(InfinispanVersionedStorage.CACHE_NAME, "example.PROTOBUF_DIST");

        if (messageCache == null) {
            throw new RuntimeException("Cache '" + InfinispanVersionedStorage.CACHE_NAME + "' not found. Please make sure the server is properly configured");
        }

        this.entityClass = entityClass;
        this.marshaller = marshaller;
        this.toEntity = toEntity;
        this.toModel = toModel;
        registerSchemasAndMarshallers();
    }

    /**
     * Register the Protobuf schemas and marshallers with the client and then register the schemas with the server too.
     */
    private void registerSchemasAndMarshallers() {
// Register entity marshallers on the client side ProtoStreamMarshaller
        // instance associated with the remote cache manager.
        SerializationContext ctx = MarshallerUtil.getSerializationContext(remoteCacheManager);

        // Cache to register the schemas with the server too
        final RemoteCache<String, String> protoMetadataCache = remoteCacheManager.getCache(ProtobufMetadataManagerConstants.PROTOBUF_METADATA_CACHE_NAME);

        // generate the message protobuf schema file and marshaller based on the annotations on Message class
        // and register it with the SerializationContext of the client
        String msgSchemaFile = null;
        try {
            ProtoSchemaBuilder protoSchemaBuilder = new ProtoSchemaBuilder();
            msgSchemaFile = protoSchemaBuilder.fileName("InfinispanObjectEntity.proto").packageName("nodowntimeupgrade").addClass(entityClass).build(ctx);
            protoMetadataCache.put("InfinispanObjectEntity.proto", msgSchemaFile);
        } catch (Exception e) {
            throw new RuntimeException("Failed to build protobuf definition from 'Message class'", e);
        }

        ctx.registerMarshaller(marshaller);

        // check for definition error for the registered protobuf schemas
        String errors = protoMetadataCache.get(ProtobufMetadataManagerConstants.ERRORS_KEY_SUFFIX);
        if (errors != null) {
            throw new IllegalStateException("Some Protobuf schema files contain errors: " + errors + "\nSchema :\n" + msgSchemaFile);
        }
    }

    @Override
    public String create(ModelType object) {
        String id = object.getId();
        if (id == null) {
            id = UUID.randomUUID().toString();
        }

        messageCache.put(id, toEntity.apply(object));

        return id;
    }

    @Override
    public ModelType read(String id) {
        return toModel.apply(messageCache.get(id));
    }

    @Override
    public ModelCriteriaBuilder getCriteriaBuilder() {
        return new IckleQueryBuilder();
    }
    //FROM nodowntimeupgrade.InfinispanObjectEntity c WHERE c.name = 'model1 # 000001'
    // AND c.name = 'model1 # 000002'
    // AND ( c.name = 'model1 # 000001'
    //    AND c.name = 'model1 # 000002'
    //    AND (
    //       AND ( c.name = 'model1 # 000001'
    //       AND c.name = 'model1 # 000002'
    //          AND ( c.name = 'model1 # 000001'
    //              AND c.name = 'model1 # 000002'
    //              AND ( AND (

    @Override
    public Stream<ModelType> read(ModelCriteriaBuilder criteria) {
        String query = criteria.unwrap(IckleQueryBuilder.class).getIckleQuery();

        System.out.println(query);

        QueryFactory queryFactory = Search.getQueryFactory(messageCache);
        return StreamSupport.stream(queryFactory.<EntityType>create(query).spliterator(), false)
                .map(toModel);
    }

    @Override
    public void delete(String id) {
        messageCache.remove(id);
    }

    @Override
    public Set<String> keys() {
        return new HashSet<>(messageCache.keySet());
    }

    @Override
    public String dump(String id) {
        return null;
    }

    @Override
    public void write(ModelType object) {
        messageCache.put(object.getId(), toEntity.apply(object));
    }
}
