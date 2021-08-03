package org.keycloak.playground.nodowntimeupgrade;

import org.junit.Before;
import org.junit.Test;
import org.keycloak.playground.nodowntimeupgrade.base.model.HasId;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V2;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V3;
import org.keycloak.playground.nodowntimeupgrade.base.storage.ModelCriteriaBuilder;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.keycloak.playground.nodowntimeupgrade.VersionUtil.Constants.INITIAL_INDEX_V2;
import static org.keycloak.playground.nodowntimeupgrade.VersionUtil_V3.getTimeout;
import static org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V2.SearchableFields.CLIENT_SCOPE_ID;
import static org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V2.TEMPLATE_PREFIX;
import static org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V3.SearchableFields.TIMEOUT;

public class CriteriaOnTemplateScopeMigration extends AbstractNoDowntimeUpgradeTest {
    private static final int INITIAL_COUNT_V1 = 100;
    private static final int INITIAL_COUNT_V2 = 100;
    private static final int INITIAL_COUNT_V3 = 1;
    private static final int INITIAL_COUNT_V4 = 0;

    @Before
    public void init() {
        createInstances(INITIAL_COUNT_V1, INITIAL_COUNT_V2, INITIAL_COUNT_V3, INITIAL_COUNT_V4);
    }

    @Test
    public void testIckleQueryVersion2() {
        Integer key = INITIAL_INDEX_V2 + 4;

        List<ObjectModel_V2> objects;

        ModelCriteriaBuilder cb = storageV2.getCriteriaBuilder()
                .compare(CLIENT_SCOPE_ID, ModelCriteriaBuilder.Operator.EQ, TEMPLATE_PREFIX + "ct7");

        objects = storageV2.read(cb).collect(Collectors.toList());

       objects.forEach(o -> System.out.println(o.getId()));

    }
}
