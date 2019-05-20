package com.fii.qa;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import com.fii.qa.ui.DatabasePanel;
import com.fii.qa.ui.ImportExportPanel;
import com.fii.qa.ui.TablePanel;

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
	public static final int WINDOW_HEIGHT = 500;

	private DatabasePanel databasePanel = new DatabasePanel();
	private TablePanel tablePanel = new TablePanel();
	private ImportExportPanel importExportPanel = new ImportExportPanel();

	/** Constructor to setup the UI components */
	public App() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit when close button clicked
		setTitle("DB Administrator");
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setVisible(true); // show it

		JTabbedPane tp = new JTabbedPane();
		tp.setBounds(50, 50, 200, 200);

		tp.add("Database", databasePanel);
		tp.add("Table", tablePanel);
		tp.add("Import/Export", importExportPanel);
		this.add(tp);
		this.pack();
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
