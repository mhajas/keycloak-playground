package org.keycloak.playground.nodowntimeupgrade;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V1;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V3;
import org.keycloak.playground.nodowntimeupgrade.base.model.ObjectModel_V4;
import org.keycloak.playground.nodowntimeupgrade.infinispan.InfinispanStorage;
import org.keycloak.playground.nodowntimeupgrade.infinispan.v3.InfinispanObjectEntity;

import javax.transaction.TransactionManager;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.keycloak.playground.nodowntimeupgrade.VersionUtil.Constants.INITIAL_INDEX_V3;
import static org.keycloak.playground.nodowntimeupgrade.VersionUtil_V3.V3_UTIL;

public class TestStorageWithTransactionManager extends AbstractNoDowntimeUpgradeTest {

    private static final int INITIAL_COUNT_V1 = 100;
    private static final int INITIAL_COUNT_V3 = 200;
    private static final int INITIAL_COUNT_V4 = 300;

    InfinispanStorage<ObjectModel_V1, org.keycloak.playground.nodowntimeupgrade.infinispan.v1.InfinispanObjectEntity> iStorageV1;
    InfinispanStorage<ObjectModel_V3, InfinispanObjectEntity> iStorageV3;
    InfinispanStorage<ObjectModel_V4, org.keycloak.playground.nodowntimeupgrade.infinispan.v4.InfinispanObjectEntity> iStorageV4;

    @Before
    public void init() {
        createInstances(INITIAL_COUNT_V1, INITIAL_COUNT_V3, INITIAL_COUNT_V4);

        if (storageV1 instanceof InfinispanStorage) {
            iStorageV1 = (InfinispanStorage<ObjectModel_V1, org.keycloak.playground.nodowntimeupgrade.infinispan.v1.InfinispanObjectEntity>) storageV1;
        }

        if (storageV3 instanceof InfinispanStorage) {
            iStorageV3 = (InfinispanStorage<ObjectModel_V3, org.keycloak.playground.nodowntimeupgrade.infinispan.v3.InfinispanObjectEntity>) storageV3;
        }

        if (storageV4 instanceof InfinispanStorage) {
            iStorageV4 = (InfinispanStorage<ObjectModel_V4, org.keycloak.playground.nodowntimeupgrade.infinispan.v4.InfinispanObjectEntity>) storageV4;
        }

        Assume.assumeNotNull(iStorageV1);
        Assume.assumeNotNull(iStorageV3);
        Assume.assumeNotNull(iStorageV4);
    }

    @Test
    public void testCommit() throws Exception {
        TransactionManager transactionManager = iStorageV3.getTransactionManager();
        String id = String.valueOf(INITIAL_INDEX_V3 + 4);

        transactionManager.begin();

        {
            ObjectModel_V3 read = iStorageV3.read(id);
            V3_UTIL.assertValid(read);
            read.setName("MyCustomName");
            iStorageV3.write(read);
        }

        {
            ObjectModel_V4 read = iStorageV4.read(id);
            assertThat(read.getName(), is(not(equalTo("MyCustomName"))));
        }

        {
            ObjectModel_V3 read = iStorageV3.read(id);
            assertThat(read.getName(), is(equalTo("MyCustomName")));
        }

        transactionManager.commit();

        {
            ObjectModel_V4 read = iStorageV4.read(id);
            assertThat(read.getName(), is(equalTo("MyCustomName")));
        }
    }

    @Test
    public void testRollback() throws Exception {
        TransactionManager transactionManager = iStorageV3.getTransactionManager();
        String id = String.valueOf(INITIAL_INDEX_V3 + 4);

        transactionManager.begin();
        {
            ObjectModel_V3 read = iStorageV3.read(id);
            V3_UTIL.assertValid(read);

            read.setName("MyCustomName");
            iStorageV3.write(read);
        }

        {
            ObjectModel_V3 read = iStorageV3.read(id);
            assertThat(read.getName(), is(equalTo("MyCustomName")));
        }

        transactionManager.rollback();

        {
            ObjectModel_V3 read = iStorageV3.read(id);
            V3_UTIL.assertValid(read);
        }
    }

    @Test
    public void testChangesInEntityAfterEnlistedInTransaction() throws Exception {
        TransactionManager transactionManager = iStorageV3.getTransactionManager();
        String id = String.valueOf(INITIAL_INDEX_V3 + 4);

        transactionManager.begin();
        {
            ObjectModel_V3 read = iStorageV3.readAndRegisterToTransaction(id);
            V3_UTIL.assertValid(read);

            read.setName("MyCustomName");
        }

        transactionManager.commit();

        {
            ObjectModel_V4 read = iStorageV4.read(id);
            assertThat(read.getName(), is(equalTo("MyCustomName")));
        }
    }
}
