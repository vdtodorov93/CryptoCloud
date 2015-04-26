package system_yok_exception.crypt_o_cloud;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import system_yok_exception.crypt_o_cloud.Dropbox.DropboxManager;

public class UserInterface {
	private static final String ACCOUNTS_FILE_NAME = "accounts.csv";
	public static final String ROOT_FOLDER_IN_CLOUD = "/crypted";
	private ICloudManager cloudManager;

	private final CloudFileBrowser cloudBrowser = new CloudFileBrowser();
	private final LocalFileBrowser localBrowser = new LocalFileBrowser();

	private Account[] readAccountsFromCsvFile() {
		Account noAccount = new Account("-- No Logged Account --", null);
		try {
			List<List<String>> readed = CSV.readFile(new File(
					ACCOUNTS_FILE_NAME));
			Account[] accounts = new Account[readed.size() + 1];
			accounts[0] = noAccount;
			for (int i = 0; i < readed.size(); ++i) {
				accounts[i + 1] = new Account(readed.get(i).get(0), readed.get(
						i).get(1));
			}
			return accounts;
		} catch (IOException e) {
			return new Account[] { noAccount };
		}
	}

	private void createUpperPanel(JPanel panel) {
		JPanel upperPanel = new JPanel();
		upperPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		upperPanel.setLayout(new BoxLayout(upperPanel, BoxLayout.Y_AXIS));

		JPanel userInfoPanel = new JPanel();
		userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.X_AXIS));

		JLabel accountNameLabel = new JLabel("Account:");
		userInfoPanel.add(accountNameLabel);

		JComboBox<Account> accountsCombobox = new JComboBox<Account>(
				readAccountsFromCsvFile());
		accountsCombobox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onAccountChoosen();
			}
		});
		userInfoPanel.add(accountsCombobox);

		JButton addNewAccount = new JButton("Add new");
		userInfoPanel.add(addNewAccount);

		JPanel uploadDownloadPanel = new JPanel();
		uploadDownloadPanel.setLayout(new BoxLayout(uploadDownloadPanel,
				BoxLayout.X_AXIS));

		upperPanel.add(userInfoPanel);
		upperPanel.add(uploadDownloadPanel);

		panel.add(upperPanel);
	}

	private void createLowerPanelFolderStructure(JPanel panel) {
		JPanel lowerPanel = new JPanel();
		lowerPanel.setLayout(new BoxLayout(lowerPanel, BoxLayout.X_AXIS));

		JTable localTable = new JTable(localBrowser);
		localTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Point p = e.getPoint();
				JTable table = (JTable) e.getSource();
				int row = table.rowAtPoint(p);
				if (e.getClickCount() == 2)
					localBrowser.doubleClickedRow(row);
				else if (e.getClickCount() == 1)
					localBrowser.singleClickedRow(row);

			}
		});

		try {
			JTable cloudTable = new JTable(cloudBrowser);
			cloudTable.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					Point p = e.getPoint();
					JTable table = (JTable) e.getSource();
					int row = table.rowAtPoint(p);
					try {
						if (e.getClickCount() == 2)
							cloudBrowser.doubleClickedRow(row);
						else if (e.getClickCount() == 1)
							cloudBrowser.singleClickedRow(row);
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null,
								"Problem with dropboxManager",
								"Error: DropboxManager",
								JOptionPane.ERROR_MESSAGE);
						ex.printStackTrace();
					}

				}
			});

			JScrollPane scrollLocal = new JScrollPane(localTable);
			lowerPanel.add(scrollLocal);

			JPanel buttonPanel = new JPanel();
			buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

			JButton uploadButton = new JButton(">");
			uploadButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					onUpload();
				}
			});
			buttonPanel.add(uploadButton);

			JButton downloadButton = new JButton("<");
			downloadButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					onDownload();
				}
			});
			buttonPanel.add(downloadButton);
			lowerPanel.add(buttonPanel);

			JScrollPane scrollCloud = new JScrollPane(cloudTable);
			lowerPanel.add(scrollCloud);

			panel.add(lowerPanel);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Problem with dropboxManager",
					"Error: DropboxManager", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	private void onAccountChoosen() {
		try {
			cloudManager = new DropboxManager();
			cloudBrowser.loadRoot(cloudManager);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Problem with dropboxManager",
					"Error: DropboxManager", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	private void onDownload() {
		if (cloudManager == null || cloudBrowser.getSelectedFile() == null)
			return;
		try {
			cloudManager.downloadResource(cloudBrowser.getCurrentPath() + "/"
					+ cloudBrowser.getSelectedFile().getFirst(),
					localBrowser.getCurrentFolder());
			localBrowser.updateCurrent();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Problem when downloading resours", "Download Error",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	private void onUpload() {
		if (cloudManager == null || localBrowser.getSelectedFile() == null)
			return;
		try {
			cloudManager.uploadResource(localBrowser.getSelectedFile(),
					cloudBrowser.getCurrentPath());
			cloudBrowser.updateCurrent();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Problem when uploading resours", "Upload Error",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	private JPanel getJPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		createUpperPanel(panel);
		createLowerPanelFolderStructure(panel);
		return panel;
	}

	private static void createAndShowGUI() {
		JFrame frame = new JFrame("Crypt-o-Cloud");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.add(new UserInterface().getJPanel());

		// frame.setSize(800,600);
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String args[]) {
		createAndShowGUI();
		// TODO Запаметяването на акаунтите при изход
	}
}
