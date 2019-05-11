package com.fii.qa.service;
import com.opencsv.CSVWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ExportService {
    private static final String databasesDirectory ="databases/";

    private static List<List<String>> getData(String databaseName, String tableName) {
        final String databaseFilePath = databasesDirectory + databaseName + ".xml";
        List<List<String>> data = new ArrayList<>();
        data.add(Collections.singletonList(tableName));

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(databaseFilePath));
            document.getDocumentElement().normalize();

            Node table = getTable(document, tableName);
            if(table == null) {
                System.out.println("Table " + tableName + " does not exist!");
                return null;
            }

            // get all <row> from <rows> tag
            NodeList rows = table.getChildNodes();
            for(int i = 0; i < rows.getLength(); i++) {
                // get schema
                if(rows.item(i).getNodeName().equals("schema")) {
                    List<String> schemaData = new ArrayList<>();
                    NodeList cols = rows.item(i).getChildNodes();
                    for(int j = 0; j < cols.getLength(); j++) {
                        if(cols.item(j).getNodeName().equals("col")) {
                            schemaData.add(cols.item(j).getTextContent());
                        }
                    }
                    data.add(schemaData);
                }
                // get rows
                if(rows.item(i).getNodeName().equals("rows")) {
                    rows = rows.item(i).getChildNodes();
                    break; // i++
                }
            }

            for(int i = 0; i < rows.getLength(); i++) {
                NodeList records = rows.item(i).getChildNodes();

                // create a map with all records from <row>
                Map<String, String> rowRecord = new HashMap<>();
                for(int j = 0; j < records.getLength(); j++) {
                    if(records.item(j).getNodeName().equals("record")) {
                        String columName = records.item(j).getAttributes().getNamedItem("name").getNodeValue();
                        String value = records.item(j).getTextContent();

                            rowRecord.put(columName, value);
                    }
                }

                // get row data by column name. We find schema in data[1]
                List<String> rowData = new ArrayList<>();
                for(int j = 0; j < data.get(1).size(); j++) {
                    if(rowRecord.get(data.get(1).get(j)) != null) {
                        rowData.add(rowRecord.get(data.get(1).get(j)));
                    }
                }
                if(rowData.size() != 0) {
                    data.add(rowData);
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static void exportTable(String databasename, String tableName) {
        List<List<String>> tableData = getData(databasename, tableName);

        File csvFile = new File("Export " + tableName + ".csv");
        try {
            FileWriter outputfile = new FileWriter(csvFile);
            CSVWriter writer = new CSVWriter(outputfile);

            for(List<String> data : tableData) {
                writer.writeNext(data.toArray(new String[0]));
            }

            // closing writer connection
            writer.close();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void exportDatabase(String databaseName) {
        final String databaseFilePath = databasesDirectory + databaseName + ".xml";

        try {
            File csvFile = new File("Export " + databaseName + ".csv");
            CSVWriter writer = new CSVWriter(new FileWriter(csvFile));

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(databaseFilePath));
            document.getDocumentElement().normalize();

            NodeList tables = document.getElementsByTagName("table");
            for(int i = 0; i < tables.getLength(); i++) {
                Node table = tables.item(i);
                String tableName = table.getAttributes().getNamedItem("name").getNodeValue();

                for(List<String> data : Objects.requireNonNull(getData(databaseName, tableName))) {
                    writer.writeNext(data.toArray(new String[0]));
                }

                writer.writeNext(new String[0]);
            }
            // closing writer connection
            writer.close();

    } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    private static Node getTable(Document document, String tableName) {
        NodeList tables = document.getElementsByTagName("table");
        for(int i = 0; i < tables.getLength(); i++) {
            Node table = tables.item(i);
            if(table.getAttributes().getNamedItem("name").getNodeValue().equals(tableName)) {
                return table;
            }
        }
        return null;
    }
}
