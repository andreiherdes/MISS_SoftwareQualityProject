package com.fii.qa;

import com.fii.qa.service.CRUDService;
import com.fii.qa.service.DatabaseService;
import com.fii.qa.service.TableService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.*;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class CRUDServiceTest {
    private List<String> columns = new ArrayList<>();
    private Map<String,String> values = new HashMap<>();
    @Before
    public void B() {
        DatabaseService dbService = new DatabaseService();
        dbService.createDatabase("BD");
        CRUDService crudService = new CRUDService();
        TableService tableService = new TableService();

        columns.add("1");
        columns.add("2");
        columns.add("3");

        tableService.createTable("BD","test",columns);

        values.put("1","unu");
        values.put("2","doi");
        values.put("3","trei");

        crudService.insertRow("BD","test",values);
    }

    @Test
    public void insertRowTest() {
        CRUDService crudService = new CRUDService();
        assertEquals (Arrays.asList(values),crudService.select("BD","test",values));
    }

    @After
    public void C() {
        DatabaseService dbService = new DatabaseService();
        dbService.deleteDatabase("BD");
    }
}
