package com.fii.qa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fii.qa.service.CRUDService;
import com.fii.qa.service.DatabaseService;
import com.fii.qa.service.TableService;

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

    @After
    public void C() {
        DatabaseService dbService = new DatabaseService();
        dbService.deleteDatabase("BD");
    }

    @Test
    public void selectRowTest_whenValuesExist() {
        CRUDService crudService = new CRUDService();
        assertEquals (Arrays.asList(values),crudService.select("BD","test",values));
    }

    @Test
    public void selectRowTest_whenDifferentValuesExist() {
        CRUDService crudService = new CRUDService();
        values.replace("3","trei","patru");
        assertEquals (new ArrayList<>(),crudService.select("BD","test",values));
    }

    @Test
    public void selectRowTest_whenValuesDoNotExist() {
        CRUDService crudService = new CRUDService();
        values.put("4","patru");
        assertEquals (new ArrayList<>(),crudService.select("BD","test",values));
    }

    @Test(expected = AssertionError.class)
    public void selectRowTest_whenTableDoesNotExist() {
        CRUDService crudService = new CRUDService();
        crudService.select("BD","Gresit",values);
    }

    @Test
    public void insertRowTest_whenValuesExist() {
        CRUDService crudService = new CRUDService();
        Map<String,String> values = new HashMap<>();
        values.put("5","cinci");
        values.put("6","sase");
        values.put("7","sapte");
        crudService.insertRow("BD","test",values);
        assertEquals (Arrays.asList(values),crudService.select("BD","test",values));
    }

    @Test
    public void insertRowTest_whenValuesDoNotExist() {
        CRUDService crudService = new CRUDService();
        Map<String,String> values = new HashMap<>();
        values.put("5","cinci");
        values.put("6","sase");
        values.put("7","sapte");
        crudService.insertRow("BD","test",values);
        values.put("8","opt");
        assertEquals (new ArrayList<>(),crudService.select("BD","test",values));
    }

    @Test
    public void deleteRowTest_whenValuesExist() {
        CRUDService crudService = new CRUDService();
        crudService.deleteRow("BD","test",values);
        assertEquals (new ArrayList<>(),crudService.select("BD","test",values));
    }

    @Test
    public void deleteRowTest_whenValuesDoNotExist() {
        CRUDService crudService = new CRUDService();
        Map<String,String> values2 = new HashMap<>();
        values2.put("5","cinci");
        values2.put("6","sase");
        values2.put("7","sapte");
        crudService.deleteRow("BD","test",values2);
        assertEquals (Arrays.asList(values),crudService.select("BD","test",values));
    }

    @Test
    public void deleteRowTest_whenDifferentValuesExist() {
        CRUDService crudService = new CRUDService();
        values.replace("3","trei","patru");
        crudService.deleteRow("BD","test",values);
        values.replace("3","patru","trei");
        assertEquals (Arrays.asList(values),crudService.select("BD","test",values));
    }

    @Test
    public void updateRowTest_whenValuesExist() {
        CRUDService crudService = new CRUDService();
        Map<String,String> newValues = new HashMap<>();
        newValues.put("1","doi");
        newValues.put("2","trei");
        newValues.put("3","patru");
        crudService.update("BD","test",values,newValues);
        assertEquals (Arrays.asList(newValues),crudService.select("BD","test",newValues));
    }

    @Test
    public void updateRowTest_whenValuesDoNotExist() {
        CRUDService crudService = new CRUDService();
        Map<String,String> newValues = new HashMap<>();
        newValues.put("5","cinci");
        newValues.put("6","sase");
        newValues.put("7","sapte");
        crudService.update("BD","test",values,newValues);
        assertEquals (Arrays.asList(values),crudService.select("BD","test",newValues));
    }
}
