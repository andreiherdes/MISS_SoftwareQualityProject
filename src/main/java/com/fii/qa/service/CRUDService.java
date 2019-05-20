package com.fii.qa.service;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class CRUDService {
    private static final String databasesDirectory ="databases/";

    public void insertRow(String dbName, String tableName, Map<String ,String> values){
        final String databaseFilePath = databasesDirectory + dbName + ".xml";
        assert values != null &&  values.size() > 0 : "\"Values\" is empty";
        assert XmlWriterService.tableExists(dbName, tableName) : "Table '" + tableName + "' does not exist!";
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(databaseFilePath));

            assert document != null : "Document is null";

            document.getDocumentElement().normalize();
            //get all the table tags
            NodeList tableList = document.getElementsByTagName("table");

            for (int j = 0; j < tableList.getLength(); j++) {
                String dbTableName = tableList.item(j).getAttributes().item(0).getNodeValue();
                //find out where to insert the row
                if(dbTableName.equals(tableName)) {

                    Element row = document.createElement("row");
                    for(String key : values.keySet()) {
                        Element record = document.createElement("record");
                        record.appendChild(document.createTextNode(values.get(key)));
                        record.setAttribute("name",key);
                        row.appendChild(record);
                    }
                    NodeList tableChild = tableList.item(j).getChildNodes();
                    for(int i = 0; i < tableChild.getLength(); i++) {
                        if(tableChild.item(i).getNodeName().equals("rows")) {
                            tableChild.item(i).appendChild(row);
                            break;
                        }
                    }
                }
            }
            XmlWriterService.writeXMLFile(document,databaseFilePath);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteRow(String dbName, String tableName, Map<String ,String> values){
        final String databaseFilePath = databasesDirectory + dbName + ".xml";

        assert values != null &&  values.size() > 0 : "\"Values\" is empty";
        assert XmlWriterService.tableExists(dbName, tableName) : "Table '" + tableName + "' does not exist!";
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(databaseFilePath));

            assert document != null : "Document is null";

            document.getDocumentElement().normalize();
            //get all the table tags
            NodeList tableList = document.getElementsByTagName("table");

            for (int j = 0; j < tableList.getLength(); j++) {
                String dbTableName = tableList.item(j).getAttributes().item(0).getNodeValue();

                if(dbTableName.equals(tableName)) { //find out the right table
                    Node rightTable = tableList.item(j);
                    NodeList rowList = rightTable.getChildNodes();

                    for(int i = 0; i < rowList.getLength(); i++) {
                        if(rowList.item(i).getNodeName().equals("rows")) {
                            rowList = rowList.item(i).getChildNodes();
                            break;
                        }
                    }

                    for (int i=0;i<rowList.getLength();i++) { //for each row
                        if(rowList.item(i).getNodeName().equals("row")){
                            NodeList recordList = rowList.item(i).getChildNodes();

                            boolean shouldDelete=true;
                            for(int x=0;x<recordList.getLength();x++){ //for each record
                                if(recordList.item(x).getNodeName().equals("record")) {
                                    String value = recordList.item(x).getTextContent();
                                    String column = recordList.item(x).getAttributes().getNamedItem("name").getNodeValue();
                                    if(values.get(column) == null) {
                                        shouldDelete=false;//nu trebuie sters
                                        break;
                                    }
                                    if(!values.get(column).equals(value)){
                                        shouldDelete=false;//nu trebuie sters
                                        break;
                                    }
                                }
                            }
                            if(shouldDelete){
                                rowList.item(i).getParentNode().removeChild(rowList.item(i));
                            }
                        }
                    }
                }
            }
            XmlWriterService.writeXMLFile(document,databaseFilePath);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    public List<Map<String, String>> select(String dbName, String tableName, Map<String, String> values) {
        final String databaseFilePath = databasesDirectory + dbName + ".xml";
        List<Map<String, String>> selectedRows = new ArrayList<>();

        assert values != null &&  values.size() > 0 : "\"Values\" is empty";
        assert XmlWriterService.tableExists(dbName, tableName) : "Table '" + tableName + "' does not exist!";

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(databaseFilePath));

            assert document != null : "Document is null";

            document.getDocumentElement().normalize();

            Node table = getTable(document, tableName);
            assert table != null : "Table " + tableName + " does not exist!";
//            if(table == null) {
//                System.out.println("Table " + tableName + " does not exist!");
//                return null;
//            }

            // get all <row> from <rows> tag
            NodeList rows = table.getChildNodes();
            for(int i = 0; i < rows.getLength(); i++) {
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
                // if all <row> contains all keys from values and they are same values then return row
                boolean shouldSelect = true;
                for(String key : values.keySet()) {
                    if(rowRecord.get(key) == null) {
                        shouldSelect = false;
                        break;
                    }
                    if(!rowRecord.get(key).equals(values.get(key))) {
                        shouldSelect = false;
                        break;
                    }
                }

                if(shouldSelect && !rowRecord.keySet().isEmpty()) {
                    selectedRows.add(rowRecord);
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return selectedRows;
    }

    public void update(String dbName, String tableName, Map<String, String> params, Map<String, String> values) {
        final String databaseFilePath = databasesDirectory + dbName + ".xml";

        assert values != null &&  values.size() > 0 : "\"Values\" is empty";
        assert XmlWriterService.tableExists(dbName, tableName) : "Table '" + tableName + "' does not exist!";
        // select rows need to be updated
        List<Map<String, String>> selectedRows = select(dbName, tableName, params);

        // delete rows from file
        for(int i = 0; i < selectedRows.size(); i++) {
            this.deleteRow(dbName, tableName, selectedRows.get(i));
        }

        // insert rows with new values
        for(int i = 0; i < selectedRows.size(); i++) {
            Map<String, String> newValues = selectedRows.get(i);
            for(String key : values.keySet()) {
                newValues.put(key, values.get(key));
            }
            this.insertRow(dbName, tableName, newValues);
        }
    }

    private Node getTable(Document document, String tableName) {
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
