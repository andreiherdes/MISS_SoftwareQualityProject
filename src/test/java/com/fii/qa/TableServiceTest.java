package com.fii.qa;

import com.fii.qa.service.CRUDService;
import com.fii.qa.service.DatabaseService;
import com.fii.qa.service.TableService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class TableServiceTest {

    private String databaseName = "Company";
    private String tableName = "Employee";

    @Before
    public void before() {
        DatabaseService databaseService = new DatabaseService();
        databaseService.createDatabase(databaseName);
    }

    @After
    public void after() {
        DatabaseService databaseService = new DatabaseService();
        databaseService.deleteDatabase(databaseName);
    }

    @Test
    public void CreateTable_True() {
        TableService tableService = new TableService();
        tableService.createTable(databaseName, tableName, Arrays.asList("Name", "Age", "Phone"));

        assertNotNull(new CRUDService().select(databaseName, tableName, null));
    }

    @Test
    public void CreateTable_False() {
        TableService tableService = new TableService();
        tableService.createTable(databaseName, tableName, Arrays.asList("Name", "Age", "Phone"));

        assertNull(new CRUDService().select(databaseName, "TableDoesNotExist", null));
    }

    @Test
    public void DeleteTable_True() {
        TableService tableService = new TableService();
        tableService.deleteTable(databaseName, tableName);

        assertNull(new CRUDService().select(databaseName, tableName, null));
    }

    @Test
    public void DeleteTable_whenExistMultipleTables() {
        TableService tableService = new TableService();
        tableService.createTable(databaseName, "Table1", Arrays.asList("Col1", "Col2", "Col3"));
        tableService.createTable(databaseName, "Table2", Arrays.asList("Col1", "Col2", "Col3"));

        tableService.deleteTable(databaseName, "Table1");

        assertNull(new CRUDService().select(databaseName, "Table1", null));
    }

    @Test
    public void ChangeTable() {
        TableService tableService = new TableService();
        tableService.createTable(databaseName, tableName, Arrays.asList("Name", "Age", "Phone"));

        tableService.changeTable(databaseName, tableName, Arrays.asList("FirstName", "LastName", "Age"));
    }
}
