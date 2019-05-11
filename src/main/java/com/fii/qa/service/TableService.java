package com.fii.qa.service;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class TableService {
    private static final String databasesDirectory ="databases/";

    public void createTable(String databaseName, String tableName, List<String> columns) {
        final String databaseFilePath = databasesDirectory + databaseName + ".xml";

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(databaseFilePath));
            doc.getDocumentElement().normalize();

            Node tables = doc.getElementsByTagName("tables").item(0);
            Element newTable = doc.createElement("table");
            newTable.setAttribute("name", tableName);

            // add columns in schema
            Element schema = doc.createElement("schema");
            for(String columnName : columns) {
                Element col = doc.createElement("col");
                col.appendChild(doc.createTextNode(columnName));

                schema.appendChild(col);
            }
            newTable.appendChild(schema);
            newTable.appendChild(doc.createElement("rows"));

            tables.appendChild(newTable);

            XmlWriterService.writeXMLFile(doc, databaseFilePath);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteTable(String databaseName, String tableName) {
        final String databaseFilePath = databasesDirectory + databaseName + ".xml";

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(databaseFilePath));
            doc.getDocumentElement().normalize();

            NodeList tables = doc.getElementsByTagName("table");
            for(int i = 0; i < tables.getLength(); i++) {
                Node table = tables.item(i);
                if(table.getAttributes().getNamedItem("name").getNodeValue().equals(tableName)) {
                    table.getParentNode().removeChild(table);
                    break;
                }
            }

            XmlWriterService.writeXMLFile(doc, databaseFilePath);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    public void changeTable(String databaseName, String tableName, List<String> columns) {
        final String databaseFilePath = databasesDirectory + databaseName + ".xml";

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(databaseFilePath));
            doc.getDocumentElement().normalize();

            // create new schema
            Element schema = doc.createElement("schema");
            for(String columnName : columns) {
                Element col = doc.createElement("col");
                col.appendChild(doc.createTextNode(columnName));

                schema.appendChild(col);
            }

            // find table and replace schema
            NodeList tables = doc.getElementsByTagName("table");
            for(int i = 0; i < tables.getLength(); i++) {
                Node table = tables.item(i);
                if(table.getAttributes().getNamedItem("name").getNodeValue().equals(tableName)) {
                    NodeList tableChilds = table.getChildNodes();

                    // find schema and replace it
                    for(int j = 0; j < tableChilds.getLength(); j++) {
                        Node child = tableChilds.item(j);
                        if (child.getNodeName().equals("schema")) {
                            child.getParentNode().replaceChild(schema, child);
                            break;
                        }
                    }
                    break;
                }
            }

            XmlWriterService.writeXMLFile(doc, databaseFilePath);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }
}
