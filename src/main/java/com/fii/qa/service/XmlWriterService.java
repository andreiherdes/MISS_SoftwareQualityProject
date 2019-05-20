package com.fii.qa.service;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlWriterService {
    private static final String databasesDirectory = "databases/";

	static void writeXMLFile(Document doc, String filePath) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transf = transformerFactory.newTransformer();

            transf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transf.setOutputProperty(OutputKeys.INDENT, "yes");
            transf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            DOMSource source = new DOMSource(doc);
            File xmlFile = new File(filePath);
            xmlFile.createNewFile();

            StreamResult file = new StreamResult(xmlFile);
            transf.transform(source, file);

        } catch (TransformerException | IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean databaseExists(String name) {
        final String databaseFilePath = databasesDirectory + name + ".xml";

        return new File(databaseFilePath).exists();

    }

    public static boolean tableExists(String databaseName, String tableName) {
        final String databaseFilePath = databasesDirectory + databaseName + ".xml";

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(databaseFilePath));
            document.getDocumentElement().normalize();

            NodeList tableList = document.getElementsByTagName("table");
            for (int j = 0; j < tableList.getLength(); j++) {
                String dbTableName = tableList.item(j).getAttributes().item(0).getNodeValue();
                if(dbTableName.equals(tableName)) {
                    return true;
                }
            }
            XmlWriterService.writeXMLFile(document,databaseFilePath);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
