package org.keycloak.playground.nodowntimeupgrade.infinispan;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.Search;
import org.infinispan.client.hotrod.configuration.ClientIntelligence;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.client.hotrod.configuration.TransactionMode;
import org.infinispan.client.hotrod.marshall.MarshallerUtil;
import org.infinispan.client.hotrod.transaction.lookup.GenericTransactionManagerLookup;
import org.infinispan.commons.configuration.XMLStringConfiguration;
import org.infinispan.protostream.BaseMarshaller;
import org.infinispan.protostream.SerializationContext;
import org.infinispan.protostream.annotations.ProtoSchemaBuilder;
import org.infinispan.query.dsl.QueryFactory;
import org.infinispan.query.remote.client.ProtobufMetadataManagerConstants;
import org.keycloak.playground.nodowntimeupgrade.base.model.HasId;
import org.keycloak.playground.nodowntimeupgrade.base.storage.ModelCriteriaBuilder;
import org.keycloak.playground.nodowntimeupgrade.base.storage.Storage;
import org.keycloak.playground.nodowntimeupgrade.infinispan.v2.IckleQueryBuilderV2;
import org.keycloak.playground.nodowntimeupgrade.infinispan.v2.InfinispanObjectEntity;

import javax.transaction.TransactionManager;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.keycloak.playground.nodowntimeupgrade.infinispan.InfinispanVersionedStorage.CACHE_NAME;

public class InfinispanStorage<ModelType extends HasId<String>, EntityType> implements Storage<ModelType> {

    private final RemoteCacheManager remoteCacheManager;
    private final RemoteCache<String, ModelType> messageCache;
    private final Class<EntityType> entityClass;
    private final BaseMarshaller<?> marshaller;
    private final UnaryOperator<ModelType> marshallableModelFunction;

    public InfinispanStorage(Class<EntityType> entityClass, BaseMarshaller<?> marshaller, UnaryOperator<ModelType> marshallableModelFunction) {
        ConfigurationBuilder remoteBuilder = new ConfigurationBuilder();
        remoteBuilder.addServer()
                .host("localhost")
                .port(11222)
                .security()
                .authentication()
                .username("Titus Bramble")
                .password("Shambles")
                .realm("default")
                .clientIntelligence(ClientIntelligence.BASIC)
                .remoteCache(CACHE_NAME)
                    .transactionManagerLookup(GenericTransactionManagerLookup.getInstance())
                    .transactionMode(TransactionMode.NON_XA);

        //.marshaller(new ProtoStreamMarshaller()); // The Protobuf based marshaller is required for query capabilities

        remoteCacheManager = new RemoteCacheManager(remoteBuilder.build());

        String xml = String.format("<distributed-cache name=\"%s\" mode=\"SYNC\">" +
                "<encoding media-type=\"application/x-protostream\"/>" +
                "<locking isolation=\"REPEATABLE_READ\"/>" +
                "<transaction mode=\"NON_XA\"/>" +
                "<expiration lifespan=\"60000\" interval=\"20000\"/>" +
                "</distributed-cache>" , CACHE_NAME);

        messageCache = remoteCacheManager.administration().getOrCreateCache(CACHE_NAME, new XMLStringConfiguration(xml));

        if (messageCache == null) {
            throw new RuntimeException("Cache '" + CACHE_NAME + "' not found. Please make sure the server is properly configured");
        }

        this.entityClass = entityClass;
        this.marshaller = marshaller;
        this.marshallableModelFunction = marshallableModelFunction;
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

        messageCache.put(id, marshallableModelFunction.apply(object));

        return id;
    }

    @Override
    public ModelType read(String id) {
        return messageCache.get(id);
    }

    @Override
    public ModelCriteriaBuilder getCriteriaBuilder() {
        if (entityClass == InfinispanObjectEntity.class) return new IckleQueryBuilderV2();
        return new IckleQueryBuilder();
    }

    @Override
    public Stream<ModelType> read(ModelCriteriaBuilder criteria) {
        String query = criteria.unwrap(IckleQueryBuilder.class).getIckleQuery();

        System.out.println(query);

        QueryFactory queryFactory = Search.getQueryFactory(messageCache);
        return StreamSupport.stream(queryFactory.<ModelType>create(query).spliterator(), false);
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
        messageCache.put(object.getId(), marshallableModelFunction.apply(object));
    }

    public ModelType readAndRegisterToTransaction(String id) {
        ModelType entity = messageCache.get(id);

        if (entity == null) {
            return null;
        }

        messageCache.put(id, marshallableModelFunction.apply(entity));

        return entity;
    }

    public TransactionManager getTransactionManager() {
        return messageCache.getTransactionManager();
    }
}
