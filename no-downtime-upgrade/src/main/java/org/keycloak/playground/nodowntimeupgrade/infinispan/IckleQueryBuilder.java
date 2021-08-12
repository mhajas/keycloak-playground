package org.keycloak.playground.nodowntimeupgrade.infinispan;

import org.keycloak.playground.nodowntimeupgrade.base.storage.ModelCriteriaBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class IckleQueryBuilder implements ModelCriteriaBuilder {

    protected static final String C = "c";
    private static final int INITIAL_BUILDER_CAPACITY = 250;
    protected final StringBuilder whereClauseBuilder = new StringBuilder(INITIAL_BUILDER_CAPACITY);
    protected static final Map<Operator, BiFunction<String, Object, String>> opToClauseMapping = new HashMap<>();

    static {
        opToClauseMapping.put(Operator.EQ, functionForOperator("="));
        opToClauseMapping.put(Operator.NE, functionForOperator("!="));
        opToClauseMapping.put(Operator.LT, functionForOperator("<"));
        opToClauseMapping.put(Operator.LE, functionForOperator("<="));
        opToClauseMapping.put(Operator.GT, functionForOperator(">"));
        opToClauseMapping.put(Operator.GE, functionForOperator(">="));
        opToClauseMapping.put(Operator.LIKE, functionForOperator("LIKE"));
        opToClauseMapping.put(Operator.ILIKE, functionForOperator("LIKE")); // TODO: Replace with some equivalent of ILIKE from SQL
    }

    private static String escapeIfNecesssary(Object o) {
        if (o instanceof String) {
            return "'" + o + "'";
        }

        if (o instanceof Integer) {
            return o.toString();
        }

        throw new IllegalArgumentException("Wrong argument of type " + o.getClass().getName());
    }

    public static BiFunction<String, Object, String> functionForOperator(String op) {
        return (f, o) -> C + "." + f + " " + op + " " + escapeIfNecesssary(o);
    }

    public IckleQueryBuilder(StringBuilder whereClauseBuilder) {
        this.whereClauseBuilder.append(whereClauseBuilder);
    }

    public IckleQueryBuilder() {
    }

    @Override
    public ModelCriteriaBuilder compare(String modelField, Operator op, Object value) {
        StringBuilder newBuilder = new StringBuilder(INITIAL_BUILDER_CAPACITY);
        newBuilder.append("(");

        if (whereClauseBuilder.length() != 0) {
            newBuilder.append(whereClauseBuilder).append(" AND (");
        }

        newBuilder.append(opToClauseMapping.get(op).apply(modelField, value));

        if (whereClauseBuilder.length() != 0) {
            newBuilder.append(")");
        }

        return new IckleQueryBuilder(newBuilder.append(")"));
    }

    @Override
    public ModelCriteriaBuilder and(ModelCriteriaBuilder... builders) {
        return new IckleQueryBuilder(new StringBuilder(INITIAL_BUILDER_CAPACITY).append("(").append(joinBuilders(builders, " AND ")).append(")"));
    }

    private String joinBuilders(ModelCriteriaBuilder[] builders, String delimiter) {
        return Arrays.stream(builders).map(mcb -> mcb.unwrap(IckleQueryBuilder.class)).map(IckleQueryBuilder::getWhereClauseBuilder).collect(Collectors.joining(delimiter));
    }

    @Override
    public ModelCriteriaBuilder or(ModelCriteriaBuilder... builders) {
        return new IckleQueryBuilder(new StringBuilder(INITIAL_BUILDER_CAPACITY).append("(").append(joinBuilders(builders, " OR ")).append(")"));
    }

    @Override
    public ModelCriteriaBuilder not(ModelCriteriaBuilder builder) {
        StringBuilder newBuilder = new StringBuilder(INITIAL_BUILDER_CAPACITY);
        StringBuilder originalBuilder = builder.unwrap(IckleQueryBuilder.class).getWhereClauseBuilder();

        if (originalBuilder.length() != 0) {
            newBuilder.append("not").append(originalBuilder);
        }

        return new IckleQueryBuilder(newBuilder);
    }

    @Override
    public <T extends ModelCriteriaBuilder> T unwrap(Class<T> clazz) {
        if (clazz.isInstance(this)) {
            return clazz.cast(this);
        } else {
            throw new ClassCastException("Incompatible class: " + clazz);
        }
    }

    public StringBuilder getWhereClauseBuilder() {
        return whereClauseBuilder;
    }

    public String getIckleQuery() {
        return "FROM nodowntimeupgrade.InfinispanObjectEntity " + C + ((whereClauseBuilder.length() != 0) ? " WHERE " + whereClauseBuilder : "");
    }
}
