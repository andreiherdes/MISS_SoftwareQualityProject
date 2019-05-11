package com.fii.qa;

import com.fii.qa.service.CRUDService;
import com.fii.qa.service.DatabaseService;
import com.fii.qa.service.TableService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class CRUDServiceTest {
    @Before
    public void B() {
        DatabaseService dbService = new DatabaseService();
        dbService.createDatabase("BD");
    }

    @Test
    public void insertRowTest() {
        CRUDService crudService = new CRUDService();
        TableService tableService = new TableService();

        List<String> columns = new ArrayList<>();
        columns.add("1");
        columns.add("2");
        columns.add("3");
        tableService.createTable("BD","test",columns);

        Map<String,String> values = new HashMap<>();
        values.put("1","unu");
        values.put("2","doi");
        values.put("3","trei");
        crudService.insertRow("BD","test",values);

        assertEquals (values,crudService.select("BD","test",values).get(0));
    }

    @After
    public void C() {
        DatabaseService dbService = new DatabaseService();
        dbService.deleteDatabase("BD");
    }
}
