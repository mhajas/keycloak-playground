package org.keycloak.playground.nodowntimeupgrade;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V1;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V2;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V3;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V4;
import org.keycloak.playground.nodowntimeupgrade.base.storage.ModelCriteriaBuilder;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeThat;
import static org.keycloak.playground.nodowntimeupgrade.VersionUtil.Constants.INITIAL_INDEX_V1;
import static org.keycloak.playground.nodowntimeupgrade.VersionUtil.Constants.INITIAL_INDEX_V2;
import static org.keycloak.playground.nodowntimeupgrade.VersionUtil.Constants.INITIAL_INDEX_V3;
import static org.keycloak.playground.nodowntimeupgrade.VersionUtil.Constants.INITIAL_INDEX_V4;
import static org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V2.SearchableFields.CLIENT_SCOPE_ID;
import static org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V2.TEMPLATE_PREFIX;

public class CriteriaOnTemplateScopeMigration extends AbstractNoDowntimeUpgradeTest {
    private static final int INITIAL_COUNT_V1 = 100;
    private static final int INITIAL_COUNT_V2 = 100;
    private static final int INITIAL_COUNT_V3 = 0;
    private static final int INITIAL_COUNT_V4 = 0;

    @Before
    public void init() {
        createInstances(INITIAL_COUNT_V1, INITIAL_COUNT_V2, INITIAL_COUNT_V3, INITIAL_COUNT_V4);

        {
            ObjectModel_V3 newObject = VersionUtil_V3.V3_UTIL.newInstance(1);
            newObject.setClientScopeId(TEMPLATE_PREFIX + "ct7");
            storageV3.create(newObject);
        }

        {
            ObjectModel_V4 newObject = VersionUtil_V4.V4_UTIL.newInstance(1);
            newObject.setClientScopeId(TEMPLATE_PREFIX + "ct7");
            storageV4.create(newObject);
        }
    }

    @Test
    public void testSearchUsingStoreVersion1() {
        Assume.assumeFalse("Version number 1 cannot read store using MCB", isNaive);

        List<ObjectModel_V1> objects;

        ModelCriteriaBuilder cb = storageV1.getCriteriaBuilder()
                .compare("clientTemplateId", ModelCriteriaBuilder.Operator.EQ, "ct7");

        objects = storageV1.read(cb).collect(Collectors.toList());

        assertThat(objects, hasSize(15)); // 10 from version 1, 5 from version 2

        Map<Integer, List<ObjectModel_V1>> groupedByVersion = objects.stream().collect(Collectors.groupingBy(m -> Integer.parseInt(m.getId().substring(0, 1))));
        assertThat(groupedByVersion.keySet(), containsInAnyOrder(1, 2));

        assertThat(groupedByVersion.get(1), hasSize(10));
        assertThat(groupedByVersion.get(1).stream().map(ObjectModel_V1::getId).map(Integer::valueOf).map(i -> i - INITIAL_INDEX_V1).collect(Collectors.toList()), hasItems(71, 72, 73, 74, 75, 76, 77, 78, 79));

        assertThat(groupedByVersion.get(2), hasSize(5));
        assertThat(groupedByVersion.get(2).stream().map(ObjectModel_V1::getId).map(Integer::valueOf).map(i -> i - INITIAL_INDEX_V2).collect(Collectors.toList()), hasItems(71, 73, 75, 77, 79));
    }

    @Test
    public void testSearchUsingStoreVersion2() {
        Assume.assumeFalse("Version number 2 cannot read store using MCB", isNaive);

        List<ObjectModel_V2> objects;

        ModelCriteriaBuilder cb = storageV2.getCriteriaBuilder()
                .compare(CLIENT_SCOPE_ID, ModelCriteriaBuilder.Operator.EQ, TEMPLATE_PREFIX + "ct7");

        objects = storageV2.read(cb).collect(Collectors.toList());

        assertThat(objects, hasSize(16)); // 10 from version 1, 5 from version 2 and 1 from version 3

        Map<Integer, List<ObjectModel_V2>> groupedByVersion = objects.stream().collect(Collectors.groupingBy(m -> Integer.parseInt(m.getId().substring(0, 1))));
        assertThat(groupedByVersion.keySet(), containsInAnyOrder(1, 2, 3));

        assertThat(groupedByVersion.get(1), hasSize(10));
        assertThat(groupedByVersion.get(1).stream().map(ObjectModel_V2::getId).map(Integer::valueOf).map(i -> i - INITIAL_INDEX_V1).collect(Collectors.toList()), hasItems(71, 72, 73, 74, 75, 76, 77, 78, 79));

        assertThat(groupedByVersion.get(2), hasSize(5));
        assertThat(groupedByVersion.get(2).stream().map(ObjectModel_V2::getId).map(Integer::valueOf).map(i -> i - INITIAL_INDEX_V2).collect(Collectors.toList()), hasItems(71, 73, 75, 77, 79));

        assertThat(groupedByVersion.get(3), hasSize(1));
        assertThat(groupedByVersion.get(3).stream().map(ObjectModel_V2::getId).map(Integer::valueOf).map(i -> i - INITIAL_INDEX_V3).collect(Collectors.toList()), hasItems(1));
    }

    @Test
    public void testSearchUsingStoreVersion3() {
        Assume.assumeFalse(" Naive store does migration before searching, this is not possible in infinispan therefore we cannot run this test there", isNaive);

        List<ObjectModel_V3> objects;

        ModelCriteriaBuilder cb = storageV3.getCriteriaBuilder()
                .compare(CLIENT_SCOPE_ID, ModelCriteriaBuilder.Operator.EQ, TEMPLATE_PREFIX + "ct7");

        objects = storageV3.read(cb).collect(Collectors.toList());
        objects.forEach(o -> System.out.println(o.getId()));

        assertThat(objects, hasSize(7)); // 5 from version 2 and 1 from version 3

        Map<Integer, List<ObjectModel_V3>> groupedByVersion = objects.stream().collect(Collectors.groupingBy(m -> Integer.parseInt(m.getId().substring(0, 1))));
        assertThat(groupedByVersion.keySet(), containsInAnyOrder(2, 3, 4));
        assertThat(groupedByVersion.get(2), hasSize(5));
        assertThat(groupedByVersion.get(2).stream().map(ObjectModel_V3::getId).map(Integer::valueOf).map(i -> i - INITIAL_INDEX_V2).collect(Collectors.toList()), hasItems(71, 73, 75, 77, 79));

        assertThat(groupedByVersion.get(3), hasSize(1));
        assertThat(groupedByVersion.get(3).stream().map(ObjectModel_V3::getId).map(Integer::valueOf).map(i -> i - INITIAL_INDEX_V3).collect(Collectors.toList()), hasItems(1));

        assertThat(groupedByVersion.get(4), hasSize(1));
        assertThat(groupedByVersion.get(4).stream().map(ObjectModel_V3::getId).map(Integer::valueOf).map(i -> i - INITIAL_INDEX_V4).collect(Collectors.toList()), hasItems(1));

        // Migrate version 1 to version 2
        storageV2.write(storageV2.read(String.valueOf(INITIAL_INDEX_V1 + 71)));
        objects = storageV3.read(cb).collect(Collectors.toList());

        assertThat(objects, hasSize(8)); // 5 from version 2 and 1 from version 3

        groupedByVersion = objects.stream().collect(Collectors.groupingBy(m -> Integer.parseInt(m.getId().substring(0, 1))));
        assertThat(groupedByVersion.keySet(), containsInAnyOrder(1, 2, 3, 4));

        assertThat(groupedByVersion.get(1), hasSize(1));
        assertThat(groupedByVersion.get(1).stream().map(ObjectModel_V3::getId).map(Integer::valueOf).map(i -> i - INITIAL_INDEX_V1).collect(Collectors.toList()), hasItems(71));

        assertThat(groupedByVersion.get(2), hasSize(5));
        assertThat(groupedByVersion.get(2).stream().map(ObjectModel_V3::getId).map(Integer::valueOf).map(i -> i - INITIAL_INDEX_V2).collect(Collectors.toList()), hasItems(71, 73, 75, 77, 79));

        assertThat(groupedByVersion.get(3), hasSize(1));
        assertThat(groupedByVersion.get(3).stream().map(ObjectModel_V3::getId).map(Integer::valueOf).map(i -> i - INITIAL_INDEX_V3).collect(Collectors.toList()), hasItems(1));

        assertThat(groupedByVersion.get(4), hasSize(1));
        assertThat(groupedByVersion.get(4).stream().map(ObjectModel_V3::getId).map(Integer::valueOf).map(i -> i - INITIAL_INDEX_V4).collect(Collectors.toList()), hasItems(1));
    }
}
