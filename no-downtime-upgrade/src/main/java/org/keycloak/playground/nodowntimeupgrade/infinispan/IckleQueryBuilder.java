package org.keycloak.playground.nodowntimeupgrade.infinispan;

import org.keycloak.playground.nodowntimeupgrade.base.storage.ModelCriteriaBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class IckleQueryBuilder implements ModelCriteriaBuilder {

    private static final String C = "c";
    private final StringBuilder whereClauseBuilder = new StringBuilder();
    private static final Map<Operator, BiFunction<String, Object, String>> opToClauseMapping = new HashMap<>();

    static {
        opToClauseMapping.put(Operator.EQ, functionForOperator("="));
        opToClauseMapping.put(Operator.NE, functionForOperator("!="));
        opToClauseMapping.put(Operator.LT, functionForOperator("<"));
        opToClauseMapping.put(Operator.LE, functionForOperator("<="));
        opToClauseMapping.put(Operator.GT, functionForOperator(">"));
        opToClauseMapping.put(Operator.GE, functionForOperator(">="));
        opToClauseMapping.put(Operator.LIKE, functionForOperator("LIKE"));
        opToClauseMapping.put(Operator.ILIKE, functionForOperator("ILIKE"));
    }

    private static String escapeIfNecesssary(Object o) {
        if (o instanceof String) {
            return "'" + o + "'";
        }

        if (o instanceof Integer) {
            return o.toString();
        }

        throw new IllegalArgumentException("Wrong argument of type " + o.getClass().getName())
    }

    public static BiFunction<String, Object, String> functionForOperator(String op) {
        return (f, o) -> C + "." + f + " " + op + " " + escapeIfNecesssary(o);
    }

    public IckleQueryBuilder(StringBuilder whereClauseBuilder) {
        this.whereClauseBuilder.append(whereClauseBuilder);
    }

    @Override
    public ModelCriteriaBuilder compare(String modelField, Operator op, Object value) {
        if (whereClauseBuilder.length() != 0) {
            whereClauseBuilder.append(" AND");
        }

        whereClauseBuilder.append(" ").append(opToClauseMapping.get(op).apply(modelField, value));

        return this;
    }

    @Override
    public ModelCriteriaBuilder and(ModelCriteriaBuilder... builders) {
        for (ModelCriteriaBuilder b : builders) {
            IckleQueryBuilder ickle = b.unwrap(IckleQueryBuilder.class);

            if (whereClauseBuilder.length() != 0 && ickle.getWhereClauseBuilder().length() != 0) {
                whereClauseBuilder.append(" AND");
            }

            if (ickle.getWhereClauseBuilder().length() != 0) {
                whereClauseBuilder.append(" (").append(ickle.getWhereClauseBuilder());
            }
        }

        return this;
    }

    @Override
    public ModelCriteriaBuilder or(ModelCriteriaBuilder... builders) {
        for (ModelCriteriaBuilder b : builders) {
            IckleQueryBuilder ickle = b.unwrap(IckleQueryBuilder.class);

            if (whereClauseBuilder.length() != 0 && ickle.getWhereClauseBuilder().length() != 0) {
                whereClauseBuilder.append(" OR");
            }

            if (ickle.getWhereClauseBuilder().length() != 0) {
                whereClauseBuilder.append(" (").append(ickle.getWhereClauseBuilder().append(")"));
            }
        }

        return this;
    }

    @Override
    public ModelCriteriaBuilder not(ModelCriteriaBuilder builder) {
        whereClauseBuilder.append(builder.unwrap(IckleQueryBuilder.class).getWhereClauseBuilder().insert(0, "!(").append(")"));

        return this;
    }

    @Override
    public <T extends ModelCriteriaBuilder> T unwrap(Class<T> clazz) {
        return null;
    }

    public StringBuilder getWhereClauseBuilder() {
        return whereClauseBuilder;
    }

    public String getIckleQuery() {
        return "FROM nodowntimeupgrade.InfinispanObjectEntity " + C +
                " WHERE " + whereClauseBuilder;
    }
}
