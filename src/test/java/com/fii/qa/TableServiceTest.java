package com.fii.qa;

import com.fii.qa.service.CRUDService;
import com.fii.qa.service.DatabaseService;
import com.fii.qa.service.TableService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class TableServiceTest {

    private String databaseName = "Company";
    private String tableName = "Employee";

    private Map<String, String> params = new HashMap<>();

    @Before
    public void before() {
        params.put("1", "2");

        try {
            DatabaseService databaseService = new DatabaseService();
            databaseService.createDatabase(databaseName);
        } catch (AssertionError ignored) {}
    }

    @After
    public void after() {
        try {
            DatabaseService databaseService = new DatabaseService();
            databaseService.deleteDatabase(databaseName);
        } catch (AssertionError ignored) {}
    }

    @Test
    public void CreateTable_True() {
        TableService tableService = new TableService();
        tableService.createTable(databaseName, tableName, Arrays.asList("Name", "Age", "Phone"));

        assertEquals(Collections.emptyList(), new CRUDService().select(databaseName, tableName, params));
    }

    @Test(expected = AssertionError.class)
    public void CreateTable_False() {
        TableService tableService = new TableService();
        tableService.createTable(databaseName, tableName, Arrays.asList("Name", "Age", "Phone"));

        new CRUDService().select(databaseName, "TableDoesNotExist", params);
    }

    @Test(expected = AssertionError.class)
    public void DeleteTable_whenTableDoesNotExist() {
        TableService tableService = new TableService();
        tableService.deleteTable(databaseName, tableName);

        new CRUDService().select(databaseName, tableName, params);
    }

    @Test(expected = AssertionError.class)
    public void DeleteTable_whenExistMultipleTables() {
        TableService tableService = new TableService();
        tableService.createTable(databaseName, "Table1", Arrays.asList("Col1", "Col2", "Col3"));
        tableService.createTable(databaseName, "Table2", Arrays.asList("Col1", "Col2", "Col3"));

        tableService.deleteTable(databaseName, "Table1");

        new CRUDService().select(databaseName, "Table1", params);
    }

    @Test
    public void ChangeTable() {
        TableService tableService = new TableService();
        tableService.createTable(databaseName, tableName, Arrays.asList("Name", "Age", "Phone"));

        tableService.changeTable(databaseName, tableName, Arrays.asList("FirstName", "LastName", "Age"));
    }
}
