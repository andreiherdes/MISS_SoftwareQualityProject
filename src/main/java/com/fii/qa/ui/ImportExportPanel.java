package com.fii.qa.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.fii.qa.service.DatabaseService;
import com.fii.qa.service.ExportService;
import com.fii.qa.service.ImportService;

public class ImportExportPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DatabaseService databaseService = new DatabaseService();

	private JTextField filePath = new JTextField(20);

	private JButton exportDatabaseButton;
	private JButton importDatabaseButton;
	// private JButton exportTableButton;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public DropdownComponent databaseNameCombo = DropdownComponent.getInstance();

	public ImportExportPanel() {
		JPanel textFieldPanel = new JPanel();
		JPanel buttonPanel = new JPanel();

		JLabel databaseNameLabel = new JLabel("Database name: ");
		// JLabel tableNameLabel = new JLabel("Table name: ");
		JLabel exportPathLabel = new JLabel("File path: ");

		// JFileChooser jfc = new JFileChooser();

		exportDatabaseButton = new JButton("Export DB");
		importDatabaseButton = new JButton("Import DB");
		// exportTableButton = new JButton("Export table");

		textFieldPanel.add(databaseNameLabel);
		textFieldPanel.add(databaseNameCombo.getDropdown());
		textFieldPanel.add(exportPathLabel);
		textFieldPanel.add(filePath);

		buttonPanel.add(exportDatabaseButton);
		buttonPanel.add(importDatabaseButton);

		this.add(textFieldPanel);
		this.add(buttonPanel);

		exportDatabaseButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					ExportService.exportDatabase(databaseNameCombo.getDropdown().getSelectedItem().toString());
				} catch (IOException | ParserConfigurationException | SAXException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		importDatabaseButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ImportService.importDatabase(filePath.getText(),
						databaseNameCombo.getDropdown().getSelectedItem().toString());

			}
		});

	}

}
