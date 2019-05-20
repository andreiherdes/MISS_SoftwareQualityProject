package com.fii.qa;

import com.fii.qa.service.DatabaseService;
import org.junit.After;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DatabaseServiceTest {

    private static final String databasesDirectory ="databases/";
    private String databaseName = "Company";
    private String newDatabaseName = "Company2";

    @After
    public void after() {
        DatabaseService databaseService = new DatabaseService();
        try {
            databaseService.deleteDatabase(databaseName);
            databaseService.deleteDatabase(newDatabaseName);
        } catch (AssertionError ignored) {}
    }

    @Test
    public void CreateDatabase_True() {
        final String databaseFilePath = databasesDirectory + databaseName + ".xml";

        DatabaseService databaseService = new DatabaseService();
        databaseService.createDatabase(databaseName);

        assertTrue(new File(databaseFilePath).exists());
    }

    @Test
    public void DeleteDatabase_True() {
        final String databaseFilePath = databasesDirectory + databaseName + ".xml";

        DatabaseService databaseService = new DatabaseService();
        databaseService.createDatabase(databaseName);

        databaseService.deleteDatabase(databaseName);

        assertTrue(!(new File(databaseFilePath).exists()));
    }

    @Test
    public void ChangeDatabase_True() {
        final String databaseFilePath = databasesDirectory + newDatabaseName + ".xml";

        DatabaseService databaseService = new DatabaseService();
        databaseService.createDatabase(databaseName);
        databaseService.changeDatabase(databaseName, newDatabaseName);

        assertTrue(new File(databaseFilePath).exists());
    }

    @Test(expected = AssertionError.class)
    public void ChangeDatabase_whenDatabaseDoesNotExists() {
        DatabaseService databaseService = new DatabaseService();
        databaseService.createDatabase(databaseName);
        databaseService.changeDatabase("DatabaseDoesNotExists", newDatabaseName);
    }

    @Test(expected = AssertionError.class)
    public void ChangeDatabase_whenNewNameIsTheSame() {
        DatabaseService databaseService = new DatabaseService();
        databaseService.createDatabase(databaseName);
        databaseService.changeDatabase(databaseName, databaseName);
    }

    @Test
    public void GetAllDatabasesTest() {
        DatabaseService databaseService = new DatabaseService();
        String[] allDatabases = {"Students"};

        assertEquals(allDatabases, databaseService.getAllDatabases());
    }

}
