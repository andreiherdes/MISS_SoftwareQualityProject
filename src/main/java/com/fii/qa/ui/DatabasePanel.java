package com.fii.qa.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.fii.qa.service.DatabaseService;

public class DatabasePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DatabaseService databaseService = new DatabaseService();

	private JTextField databaseName = new JTextField(20);
	private JTextField databaseNameForUpdate = new JTextField(20);

	private JButton createDatabaseButton;
	private JButton deleteDatabaseButton;
	private JButton renameDatabaseButton;

	public DatabasePanel() {
		JPanel textFieldPanel = new JPanel();
		JPanel buttonPanel = new JPanel();

		JLabel databaseNameLabel = new JLabel("Database name: ");
		JLabel databaseNameForRenameLabel = new JLabel("New database name: ");

		createDatabaseButton = new JButton("Create DB");
		deleteDatabaseButton = new JButton("Delete DB");
		renameDatabaseButton = new JButton("Rename DB");

		textFieldPanel.add(databaseNameLabel);
		textFieldPanel.add(databaseName);
		textFieldPanel.add(databaseNameForRenameLabel);
		textFieldPanel.add(databaseNameForUpdate);

		buttonPanel.add(createDatabaseButton);
		buttonPanel.add(deleteDatabaseButton);
		buttonPanel.add(renameDatabaseButton);

		this.add(textFieldPanel);
		this.add(buttonPanel);

		createDatabaseButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				databaseService.createDatabase(databaseName.getText());
			}
		});

		deleteDatabaseButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				databaseService.deleteDatabase(databaseName.getText());
			}
		});

		renameDatabaseButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				databaseService.changeDatabase(databaseName.getText(), databaseNameForUpdate.getText());

			}
		});

	}
}
