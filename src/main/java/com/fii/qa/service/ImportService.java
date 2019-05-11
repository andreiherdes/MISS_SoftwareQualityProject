package com.fii.qa.service;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ImportService {
    private static final String databasesDirectory ="databases/";

    public static void importDatabase(String filePath, String databaseName) {
        DatabaseService databaseService = new DatabaseService();
        TableService tableService = new TableService();
        CRUDService crudService = new CRUDService();

        // create database
        databaseService.createDatabase(databaseName);

        List<String> columns = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean newTable = true;
            String tableName = null;
            boolean tableNameChecked = false;

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                List<String> v = new ArrayList<>();
                for(int i = 0; i < values.length; i++) {
                    if(!values[i].equals("")) {
                        v.add(values[i]);
                    }
                }
                values = Arrays.copyOf(v.toArray(), v.size(), String[].class);

                if(values.length == 0) {
                    newTable = true;
                    continue;
                }

                if(newTable) {
                    newTable = false;
                    tableName = values[0].replaceAll("\"", "");
                    tableNameChecked = true;
                    continue;
                }

                if(tableNameChecked) {
                    for(int i = 0; i < values.length; i++) {
                        values[i] = values[i].replaceAll("\"", "");
                    }
                    columns = Arrays.asList(values);
                    tableService.createTable(databaseName, tableName, columns);
                    tableNameChecked = false;
                    continue;
                }

                Map<String, String> valuesMap = new HashMap<>();
                List<String> valuesList = Arrays.asList(values);
                if(columns.size() != valuesList.size()) {
                    continue;
                }

                for(int i = 0; i < columns.size(); i++) {
                    valuesMap.put(columns.get(i), valuesList.get(i).replaceAll("\"", ""));
                }

                crudService.insertRow(databaseName, tableName, valuesMap);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
