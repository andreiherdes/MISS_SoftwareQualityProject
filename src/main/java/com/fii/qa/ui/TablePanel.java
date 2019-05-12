package com.fii.qa.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.fii.qa.service.DatabaseService;
import com.fii.qa.service.TableService;

public class TablePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TableService tableService = new TableService();
	private DatabaseService databaseService = new DatabaseService();
	// private CRUDService crudService = new CRUDService();

	private JTextField tableName = new JTextField(20);
	private JTextField columnNames = new JTextField(50);

	private JButton createTableButton;
	private JButton updateTableButton;
	private JButton deleteTableButton;
	
	private DropdownComponent dropdownComponent = DropdownComponent.getInstance();

	public TablePanel() {
		JPanel textFieldPanel = new JPanel();
		JPanel buttonPanel = new JPanel();

		JLabel databaseNameLabel = new JLabel("Database: ");
		JLabel tableNameLabel = new JLabel("Table name: ");
		JLabel columnNameLabel = new JLabel("Column names: ");

		textFieldPanel.add(databaseNameLabel);
		textFieldPanel.add(dropdownComponent.getDropdown());
		textFieldPanel.add(tableNameLabel);
		textFieldPanel.add(tableName);
		textFieldPanel.add(columnNameLabel);
		textFieldPanel.add(columnNames);

		createTableButton = new JButton("Create");
		updateTableButton = new JButton("Update");
		deleteTableButton = new JButton("Delete");

		buttonPanel.add(createTableButton);
		buttonPanel.add(updateTableButton);
		buttonPanel.add(deleteTableButton);

		this.add(textFieldPanel);
		this.add(buttonPanel);

		createTableButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String[] columnNamesArray = columnNames.getText().split(",");
				tableService.createTable(dropdownComponent.getDropdown().getSelectedItem().toString(),
						tableName.getText(), Arrays.asList(columnNamesArray));

			}
		});

		updateTableButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String[] columnNamesArray = columnNames.getText().split(",");
				tableService.changeTable(dropdownComponent.getDropdown().getSelectedItem().toString(),
						tableName.getText(), Arrays.asList(columnNamesArray));

			}
		});

		deleteTableButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				tableService.deleteTable(dropdownComponent.getDropdown().getSelectedItem().toString(),
						tableName.getText());

			}
		});

	}
}
