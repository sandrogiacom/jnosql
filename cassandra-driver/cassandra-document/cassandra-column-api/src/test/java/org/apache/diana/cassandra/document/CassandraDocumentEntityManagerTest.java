package org.apache.diana.cassandra.document;

import com.datastax.driver.core.Session;
import org.apache.diana.api.Value;
import org.apache.diana.api.column.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

import static org.apache.diana.cassandra.document.Constants.COLUMN_FAMILY;
import static org.apache.diana.cassandra.document.Constants.KEY_SPACE;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.*;


public class CassandraDocumentEntityManagerTest {

    private ColumnEntityManager columnEntityManager;

    @Before
    public void setUp() {
        CassandraConfiguration cassandraConfiguration = new CassandraConfiguration();
        ColumnEntityManagerFactory entityManagerFactory = cassandraConfiguration.getManagerFactory();
        columnEntityManager = entityManagerFactory.getColumnEntityManager(KEY_SPACE);
    }

    @Test
    public void shouldClose() throws Exception {
        columnEntityManager.close();
        CassandraDocumentEntityManager cassandraDocumentEntityManager = CassandraDocumentEntityManager.class.cast(columnEntityManager);
        Session session = cassandraDocumentEntityManager.getSession();
        assertTrue(session.isClosed());
    }

    @Test
    public void shouldInsertJustKey() {
        Column key = Columns.of("id", 10L);
        ColumnEntity columnEntity = ColumnEntity.of(COLUMN_FAMILY);
        columnEntity.add(key);
        columnEntityManager.save(columnEntity);
    }

    @Test
    public void shouldInsertColumns() {
        ColumnEntity columnEntity = getColumnEntity();
        columnEntityManager.save(columnEntity);
    }



    @Test
    public void shouldFindById() {

        columnEntityManager.save(getColumnEntity());
        ColumnEntity entity = ColumnEntity.of(COLUMN_FAMILY, Columns.of("id", 10L));
        List<ColumnEntity> columnEntity = columnEntityManager.find(entity);
        assertFalse(columnEntity.isEmpty());
        List<Column> columns = columnEntity.get(0).getColumns();
        assertThat(columns.stream().map(Column::getName).collect(toList()), containsInAnyOrder("name", "version", "options", "id"));
        assertThat(columns.stream().map(Column::getValue).map(Value::get).collect(toList()), containsInAnyOrder("Cassandra", 3.2, Arrays.asList(1,2,3), 10L));

    }

    @Test
    public void shouldRunNativeQuery() {
        columnEntityManager.save(getColumnEntity());
        List<ColumnEntity> entities = columnEntityManager.nativeQuery("select * from newKeySpace.newColumnFamily where id=10;");
        assertFalse(entities.isEmpty());
        List<Column> columns = entities.get(0).getColumns();
        assertThat(columns.stream().map(Column::getName).collect(toList()), containsInAnyOrder("name", "version", "options", "id"));
        assertThat(columns.stream().map(Column::getValue).map(Value::get).collect(toList()), containsInAnyOrder("Cassandra", 3.2, Arrays.asList(1,2,3), 10L));
    }

    @Test
    public void shouldDeleteColumnFamiliy() {
        columnEntityManager.save(getColumnEntity());
        ColumnEntity.of(COLUMN_FAMILY, Columns.of("id", 10L));
        columnEntityManager.delete(ColumnEntity.of(COLUMN_FAMILY, Columns.of("id", 10L)));
        List<ColumnEntity> entities = columnEntityManager.nativeQuery("select * from newKeySpace.newColumnFamily where id=10;");
        Assert.assertTrue(entities.isEmpty());
    }

    private ColumnEntity getColumnEntity() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("name", "Cassandra");
        fields.put("version", 3.2);
        fields.put("options", Arrays.asList(1,2,3));
        List<Column> columns = Columns.of(fields);
        ColumnEntity columnEntity = ColumnEntity.of(COLUMN_FAMILY, Columns.of("id", 10L));
        columns.forEach(columnEntity::add);
        return columnEntity;
    }

}