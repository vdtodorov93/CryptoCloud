package system_yok_exception.crypt_o_cloud;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import system_yok_exception.crypt_o_cloud.Account.Cloud;
import system_yok_exception.crypt_o_cloud.Dropbox.DropboxManager;

public class UserInterface {
	private static final Account DUMMY_ACCOUNT = new Account("hackfmi_dropbox",
			Cloud.DROPBOX);
	public static final String ROOT_FOLDER_IN_CLOUD = "/crypted/";
	private ICloudManager cloudManager;

	public UserInterface() {
		try {
			cloudManager = new DropboxManager();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Problem with dropboxManager",
					"Error: DropboxManager", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
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
				new Account[] { new Account(), DUMMY_ACCOUNT });
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

		JButton uploadButton = new JButton("Upload");
		uploadDownloadPanel.add(uploadButton);

		JButton downloadButton = new JButton("Download");
		uploadDownloadPanel.add(downloadButton);

		upperPanel.add(userInfoPanel);
		upperPanel.add(uploadDownloadPanel);

		panel.add(upperPanel);
	}

	private void createLowerPanelFolderStructure(JPanel panel) {
		final LocalFileBrowser localBrowser = new LocalFileBrowser();
		JTable localTable = new JTable(localBrowser);
		localTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getClickCount() == 2) {
					Point p = e.getPoint();
					JTable table = (JTable) e.getSource();
					int row = table.rowAtPoint(p);
					localBrowser.doubleClickedRow(row);
				}
			}
		});

		try {
			final CloudFileBrowser cloudBrowser = new CloudFileBrowser(
					cloudManager);
			JTable cloudTable = new JTable(cloudBrowser);
			cloudTable.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					if (e.getClickCount() == 2) {
						Point p = e.getPoint();
						JTable table = (JTable) e.getSource();
						int row = table.rowAtPoint(p);
						try {
							cloudBrowser.doubleClickedRow(row);
						} catch (Exception ex) {
							JOptionPane.showMessageDialog(null,
									"Problem with dropboxManager",
									"Error: DropboxManager",
									JOptionPane.ERROR_MESSAGE);
							ex.printStackTrace();
						}
					}
				}
			});
			JScrollPane scroll = new JScrollPane(cloudTable);
			panel.add(scroll);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Problem with dropboxManager",
					"Error: DropboxManager", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}

		JScrollPane scroll = new JScrollPane(localTable);
		panel.add(scroll);
	}

	private void onAccountChoosen() {
		// TODO
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
	}
}
