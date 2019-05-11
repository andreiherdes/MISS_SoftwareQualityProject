package com.fii.qa;

import com.fii.qa.service.DatabaseService;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import static org.junit.Assert.assertTrue;

public class DatabaseServiceTest {

    private static final String databasesDirectory ="databases/";
    private String databaseName = "Employee";
    private String newDatabaseName = "Employee2";

    @After
    public void after() {
        DatabaseService databaseService = new DatabaseService();
        databaseService.deleteDatabase(databaseName);
        databaseService.deleteDatabase(newDatabaseName);
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

}
