package org.keycloak.playground.nodowntimeupgrade.infinispan.v2;

import org.keycloak.playground.nodowntimeupgrade.base.storage.ModelCriteriaBuilder;
import org.keycloak.playground.nodowntimeupgrade.infinispan.IckleQueryBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import static org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V1.SearchableFields.CLIENT_TEMPLATE_ID;
import static org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V2.SearchableFields.CLIENT_SCOPE_ID;
import static org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V2.TEMPLATE_PREFIX;

public class IckleQueryBuilderV2 extends IckleQueryBuilder {

    @Override
    public ModelCriteriaBuilder compare(String modelField, Operator op, Object value) {
        StringBuilder newBuilder = new StringBuilder(whereClauseBuilder);

        if (whereClauseBuilder.length() != 0) {
            newBuilder.append(" AND ");
        }

        if (whereClauseBuilder.length() != 0) {
            newBuilder.append("(");
        }

        if (modelField.equals(CLIENT_SCOPE_ID) && ((String) value).startsWith(TEMPLATE_PREFIX)) { // Migration so that we include elements from V1
            String version1RequiredTemplate = ((String)value).substring(TEMPLATE_PREFIX.length());

            newBuilder.append("(")
                        .append(C).append(".entityVersion <= 2 AND ")
                            .append(opToClauseMapping.get(op).apply(CLIENT_TEMPLATE_ID, version1RequiredTemplate))
                        .append(") OR (")
                        .append(C).append(".entityVersion >= 2 AND ").append(C).append(".entityVersion <= 3 AND ")
                            .append(opToClauseMapping.get(op).apply(modelField, value))
                    .append(")");
        } else {
            newBuilder.append(opToClauseMapping.get(op).apply(modelField, value));
        }

        if (whereClauseBuilder.length() != 0) {
            newBuilder.append(")");
        }

        return new IckleQueryBuilder(newBuilder.insert(0, "(").append(")"));
    }
}
