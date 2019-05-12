package com.fii.qa.service;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class DatabaseService {
	private static final String DATABASE = "database";
	private static final String databasesDirectory = "databases/";

	public boolean createDatabase(String name) {
		final String databaseFilePath = databasesDirectory + name + ".xml";

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.newDocument();

			Element root = doc.createElement(DATABASE);
			root.setAttribute("name", name);
			doc.appendChild(root);

			root.appendChild(doc.createElement("tables"));

			XmlWriterService.writeXMLFile(doc, databaseFilePath);
			return true;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void deleteDatabase(String name) {
		final String databaseFilePath = databasesDirectory + name + ".xml";

		if (!databaseExists(name)) {
			System.out.println("Database does not exist!");
			return;
		}

		File xmlFile = new File(databaseFilePath);
		xmlFile.delete();
	}

	public void changeDatabase(String name, String newName) {
		final String databaseFilePath = databasesDirectory + name + ".xml";

		if (!databaseExists(name)) {
			System.out.println("Database does not exist!");
			return;
		}

		if (name.equals(newName)) {
			return;
		}

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new File(databaseFilePath));
			doc.getDocumentElement().normalize();

			Element database = (Element) doc.getElementsByTagName(DATABASE).item(0);
			database.setAttribute("name", newName);
			XmlWriterService.writeXMLFile(doc, databaseFilePath);

			File file = new File(databaseFilePath);
			file.renameTo(new File(databasesDirectory + newName + ".xml"));

		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}

	public String[] getAllDatabases() {
		File[] files = new File(databasesDirectory).listFiles();
		String[] databaseNames = new String[files.length];
		
		int counter = 0;
		for (File file : files) {
			if (file.isFile()) {
				databaseNames[counter] = file.getName().substring(0, file.getName().indexOf("."));
				counter++;
			}
		}

		return databaseNames;
	}

	private boolean databaseExists(String name) {
		final String databaseFilePath = databasesDirectory + name + ".xml";

		return new File(databaseFilePath).exists();

	}

}
