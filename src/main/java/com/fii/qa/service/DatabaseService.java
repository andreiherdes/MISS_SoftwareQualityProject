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

	public void createDatabase(String name) {
		// precondition
		assert !XmlWriterService.databaseExists(name) : "Database '" + name + "' already exists!";

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
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

		//post condition
		assert XmlWriterService.databaseExists(name) : "Can't create database with name: " + name;
	}

	public void deleteDatabase(String name) {
		assert XmlWriterService.databaseExists(name) : "Database '" + name + "' does not exist";

		File xmlFile = new File(databasesDirectory + name + ".xml");
		xmlFile.delete();

		assert !XmlWriterService.databaseExists(name) : "Can't delete database with name: " + name;
	}

	public void changeDatabase(String name, String newName) {
		assert XmlWriterService.databaseExists(name) : "Database '" + name + "' does not exist";
		assert !name.equals(newName) : "Can't change database because new name is the same with old name";

		final String databaseFilePath = databasesDirectory + name + ".xml";
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

		assert XmlWriterService.databaseExists(newName) : "Can't change database from '" + name + "' to '" + newName + "'";
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

}
