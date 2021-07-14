package org.keycloak.playground.nodowntimeupgrade.infinispan;

import org.keycloak.playground.nodowntimeupgrade.base.storage.ModelCriteriaBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class IckleQueryBuilder implements ModelCriteriaBuilder {

    protected static final String C = "c";
    protected final StringBuilder whereClauseBuilder = new StringBuilder();
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
        StringBuilder newBuilder = new StringBuilder(whereClauseBuilder);

        if (whereClauseBuilder.length() != 0) {
            newBuilder.append(" AND ");
        }

        if (whereClauseBuilder.length() != 0) {
            newBuilder.append("(");
        }
        newBuilder.append(opToClauseMapping.get(op).apply(modelField, value));
        if (whereClauseBuilder.length() != 0) {
            newBuilder.append(")");
        }

        return new IckleQueryBuilder(newBuilder.insert(0, "(").append(")"));
    }

    @Override
    public ModelCriteriaBuilder and(ModelCriteriaBuilder... builders) {
        StringBuilder newBuilder = new StringBuilder();

        for (ModelCriteriaBuilder b : builders) {
            IckleQueryBuilder ickle = b.unwrap(IckleQueryBuilder.class);

            if (newBuilder.length() != 0 && ickle.getWhereClauseBuilder().length() != 0) {
                newBuilder.append(" AND ");
            }

            if (ickle.getWhereClauseBuilder().length() != 0) {
                newBuilder.append(ickle.getWhereClauseBuilder());
            }
        }

        return new IckleQueryBuilder(newBuilder.insert(0, " (").append(")"));
    }

    @Override
    public ModelCriteriaBuilder or(ModelCriteriaBuilder... builders) {
        StringBuilder newBuilder = new StringBuilder();

        for (ModelCriteriaBuilder b : builders) {
            IckleQueryBuilder ickle = b.unwrap(IckleQueryBuilder.class);

            if (newBuilder.length() != 0 && ickle.getWhereClauseBuilder().length() != 0) {
                newBuilder.append(" OR ");
            }

            if (ickle.getWhereClauseBuilder().length() != 0) {
                newBuilder.append(ickle.getWhereClauseBuilder());
            }
        }

        return new IckleQueryBuilder(newBuilder.insert(0, "(").append(")"));
    }

    @Override
    public ModelCriteriaBuilder not(ModelCriteriaBuilder builder) {
        StringBuilder newBuilder = new StringBuilder();
        StringBuilder originalBuilder = builder.unwrap(IckleQueryBuilder.class).getWhereClauseBuilder();

        if (originalBuilder.length() != 0) {
            newBuilder.append(originalBuilder).insert(0, "not");
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
