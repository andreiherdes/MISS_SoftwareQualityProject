package com.fii.qa;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.fii.qa.service.CRUDService;
import com.fii.qa.service.DatabaseService;
import com.fii.qa.service.ExportService;
import com.fii.qa.service.TableService;

public class ExportServiceTest {

	@Before
	public void before() {
		DatabaseService dbService = new DatabaseService();

		dbService.createDatabase("BD");
		CRUDService crudService = new CRUDService();
		TableService tableService = new TableService();

		List<String> columns = new ArrayList<>();

		columns.add("1");
		columns.add("2");
		columns.add("3");

		tableService.createTable("BD", "test", columns);

		Map<String, String> values = new HashMap<>();

		values.put("1", "unu");
		values.put("2", "doi");
		values.put("3", "trei");

		crudService.insertRow("BD", "test", values);
	}

	@Test(expected = FileNotFoundException.class)
	public void testDatabaseExport_whenDatabaseDoesNotExist()
			throws IOException, ParserConfigurationException, SAXException {
		ExportService.exportDatabase("Inexistent_Database");
	}

	@Test
	public void testDabaseExport_whenDatabaseExists() {
		try {
			ExportService.exportDatabase("BD");
		} catch (IOException | ParserConfigurationException | SAXException e) {
			e.printStackTrace();
		}

		assertTrue(new File("Export_BD.csv").exists());
	}

	@After
	public void after() {
		DatabaseService dbService = new DatabaseService();
		dbService.deleteDatabase("BD");

		// Lists all files in folder
		File folder = new File("./");
		Arrays.stream(folder.listFiles((f, p) -> p.endsWith("csv"))).forEach(File::delete);
	}
}
