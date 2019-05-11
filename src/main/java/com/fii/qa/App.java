package com.fii.qa;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.fii.qa.service.CRUDService;
import com.fii.qa.service.DatabaseService;
import com.fii.qa.service.TableService;

/**
 * Hello world!
 *
 */
public class App extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Name-constants to define the various dimensions
	public static final int WINDOW_WIDTH = 500;
	public static final int WINDOW_HEIGHT = 300;

	private DatabaseService databaseService = new DatabaseService();
	private CRUDService crudService = new CRUDService();
	private TableService tableService = new TableService();

	private JButton createDbButton;
	private JButton deleteDbButton;

	private JTextField databaseName = new JTextField(20);
	private JTextField columnNames = new JTextField(20);

	private JButton createTableButton;
	private JButton updateTableButton;
	private JButton readTableButton;
	private JButton deleteTableButton;

	private JButton createEntryButton;
	private JButton updateEntryButton;
	private JButton readEntryButton;
	private JButton deleteEntryButton;

	private JTextField tableName = new JTextField(20);
	private JTextField entryValue = new JTextField(20);

	private JPanel databasePanel = new JPanel();
	private JPanel crudPanel = new JPanel();
	private JPanel fullPanel = new JPanel();

	/** Constructor to setup the UI components */
	public App() {

		JLabel databaseNameLabel = new JLabel("Dabase name: ");
		JLabel columnNamesLabel = new JLabel("Column names: ");

		JPanel buttonPanel = new JPanel();
		JPanel textFieldPanel = new JPanel();

		createDbButton = new JButton("Create DB");
		deleteDbButton = new JButton("Delete DB");

		textFieldPanel.add(databaseNameLabel);
		textFieldPanel.add(databaseName);
		textFieldPanel.add(columnNamesLabel);
		textFieldPanel.add(columnNames);

		buttonPanel.add(createDbButton);
		buttonPanel.add(deleteDbButton);

		databasePanel.add(textFieldPanel, BorderLayout.CENTER);
		databasePanel.add(buttonPanel, BorderLayout.PAGE_END);

		fullPanel.add(databasePanel);

		JLabel tableNameLabel = new JLabel("Table name: ");
		JLabel columnLabel = new JLabel("Column name: ");
		JLabel entryLabel = new JLabel("Entry value: ");

		JPanel buttonPanelCRUD = new JPanel();
		JPanel textFieldPanelCRUD = new JPanel();

		textFieldPanelCRUD.add(tableNameLabel);
		textFieldPanelCRUD.add(tableName);
		textFieldPanelCRUD.add(entryLabel);
		textFieldPanelCRUD.add(entryValue);
		
		createTableButton = new JButton("Create table");
		updateTableButton = new JButton("Update table");
		readTableButton = new JButton("Read table");
		deleteTableButton = new JButton("Delete table");

		deleteEntryButton = new JButton("Delete entry");
		readEntryButton = new JButton("Read entry");
		updateEntryButton = new JButton("Update entry");
		createEntryButton = new JButton("Create entry");

		buttonPanelCRUD.add(createTableButton);
		buttonPanelCRUD.add(updateTableButton);
		buttonPanelCRUD.add(readTableButton);
		buttonPanelCRUD.add(deleteTableButton);

		buttonPanelCRUD.add(deleteEntryButton);
		buttonPanelCRUD.add(readEntryButton);
		buttonPanelCRUD.add(updateEntryButton);
		buttonPanelCRUD.add(createEntryButton);

		crudPanel.add(textFieldPanelCRUD);
		crudPanel.add(buttonPanelCRUD, BorderLayout.PAGE_END);

		fullPanel.add(crudPanel, BorderLayout.PAGE_END);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit when close button clicked
		setTitle("DB Administrator");
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setVisible(true); // show it

		this.add(fullPanel);
		this.pack();

		createDbButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				databaseService.createDatabase(databaseName.getText());
			}
		});

		deleteDbButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				databaseService.deleteDatabase(databaseName.getText());
			}
		});

		createTableButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String[] columnNamesArray = columnNames.getText().split(",");

				tableService.createTable(databaseName.getText(), tableName.getText(), Arrays.asList(columnNamesArray));
			}
		});

		createEntryButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String[] entryNamesArray = entryValue.getText().split(",");
				String[] columnNamesArray = columnNames.getText().split(",");

				Map<String, String> values = IntStream.range(0, columnNamesArray.length).boxed()
						.collect(Collectors.toMap(i -> columnNamesArray[i], i -> entryNamesArray[i]));

				crudService.insertRow(databaseName.getText(), tableName.getText(), values);

			}
		});

		deleteEntryButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String[] entryNamesArray = entryValue.getText().split(",");
				String[] columnNamesArray = columnNames.getText().split(",");

				Map<String, String> values = IntStream.range(0, columnNamesArray.length).boxed()
						.collect(Collectors.toMap(i -> columnNamesArray[i], i -> entryNamesArray[i]));
				crudService.deleteRow(databaseName.getText(), tableName.getText(), values);

			}
		});
	}

	/** The entry main() method */
	public static void main(String[] args) {
		// Run GUI codes in the Event-Dispatching thread for thread safety
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new App(); // Let the constructor do the job
			}
		});
	}
}
