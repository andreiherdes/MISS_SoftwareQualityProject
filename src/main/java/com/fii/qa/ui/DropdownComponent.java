package com.fii.qa.ui;

import javax.swing.JComboBox;

import com.fii.qa.service.DatabaseService;

public class DropdownComponent {

	private static final DropdownComponent INSTANCE = new DropdownComponent();

	private DatabaseService dbService = new DatabaseService();

	private JComboBox databaseNameCombo;

	private DropdownComponent() {
		databaseNameCombo = new JComboBox(dbService.getAllDatabases());
	}

	public JComboBox getDropdown() {
		return databaseNameCombo;
	}

	public void updateDropdown() {
		databaseNameCombo.repaint();
		databaseNameCombo.revalidate();
	}

	public static DropdownComponent getInstance() {
		return INSTANCE;
	}
}
