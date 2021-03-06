package system_yok_exception.crypt_o_cloud;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.miginfocom.swing.MigLayout;
import system_yok_exception.crypt_o_cloud.Dropbox.DropboxManager;
import system_yok_exception.utils.Aes256Encryptor;
import system_yok_exception.utils.CryptUtil;
import system_yok_exception.utils.FileKeyService;

public class UserInterface {
	private static final String ACCOUNTS_FILE_NAME = "accounts.csv";
	public static final String ROOT_FOLDER_IN_CLOUD = "/crypted";
	private ICloudManager cloudManager;

	private final CloudFileBrowser cloudBrowser = new CloudFileBrowser();
	private final LocalFileBrowser localBrowser = new LocalFileBrowser();
	private CryptUtil cryptUtil = new CryptUtil(new FileKeyService().getDefaultKey(), new Aes256Encryptor());
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
		upperPanel.setLayout(new MigLayout());

		JPanel userInfoPanel = new JPanel();
		userInfoPanel.setLayout(new MigLayout());

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
		uploadDownloadPanel.setLayout(new MigLayout());

		upperPanel.add(userInfoPanel);
		upperPanel.add(uploadDownloadPanel);

		panel.add(upperPanel, "center");
	}

	private void createLowerPanelFolderStructure(JPanel panel) {
		JPanel lowerPanel = new JPanel();
		lowerPanel.setLayout(new MigLayout());

		JTable localTable = new JTable(localBrowser);

		localTable.setShowGrid(false);
		localTable.getColumnModel().getColumn(0).setMinWidth(20);
		localTable.getColumnModel().getColumn(0).setMaxWidth(20);
		localTable.getColumnModel().getColumn(2).setMinWidth(100);
		localTable.getColumnModel().getColumn(2).setMaxWidth(100);
		localTable.getColumnModel().getColumn(3).setMinWidth(80);
		localTable.getColumnModel().getColumn(3).setMaxWidth(80);

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
			cloudTable.setShowGrid(false);
			cloudTable.getColumnModel().getColumn(0).setMinWidth(20);
			cloudTable.getColumnModel().getColumn(0).setMaxWidth(20);
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
			buttonPanel.setLayout(new MigLayout("flowy"));

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
		if (cloudManager == null) {
			JOptionPane.showMessageDialog(null,
					"Please, select account first!", "Not selected account",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (cloudBrowser.getSelectedFile() == null) {
			JOptionPane.showMessageDialog(null,
					"Please, select item to be downloaded first!",
					"Not selected item for download",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		try {
			
			
			File downloadedCryptedDir = cloudManager.downloadResource(cloudBrowser.getCurrentPath() + "/"
					+ cloudBrowser.getSelectedFile().getFirst(),
					new File("tmp"));
			cryptUtil.decryptDirectory(downloadedCryptedDir, localBrowser.getCurrentFolder().getPath());
			localBrowser.updateCurrent();
			emptyTmpFolder();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Problem when downloading resource", "Download Error",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	private void emptyTmpFolder() {
		File tmp = new File("tmp");
		for(File f: tmp.listFiles()) {
			deleteDirectory(f);
		}
	}

	private void onUpload() {
		if (cloudManager == null) {
			JOptionPane.showMessageDialog(null,
					"Please, select account first!", "Not selected account",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (localBrowser.getSelectedFile() == null) {
			JOptionPane
					.showMessageDialog(null,
							"Please, select item to be uploaded first!",
							"Not selected item for upload",
							JOptionPane.WARNING_MESSAGE);
			return;
		}
		try {
			File encryptedFile = cryptUtil.encryptDirectory(localBrowser.getSelectedFile(), "tmp");
			cloudManager.uploadResource(encryptedFile,
					cloudBrowser.getCurrentPath());
			cloudBrowser.updateCurrent();
			emptyTmpFolder();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Problem when uploading resource", "Upload Error",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	private JPanel getJPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("flowy"));
		createUpperPanel(panel);
		createLowerPanelFolderStructure(panel);
		return panel;
	}

	private static void createAndShowGUI() {
		JFrame frame = new JFrame("Crypt-o-Cloud");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setIconImage(new ImageIcon("icons/icon.png").getImage());

		frame.add(new UserInterface().getJPanel());

		// frame.setSize(800,600);
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String args[]) {
		try {
			UIManager.setLookAndFeel(UIManager
					.getCrossPlatformLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		createAndShowGUI();
		// TODO �������������� �� ��������� ��� �����
	}
	
	public static boolean deleteDirectory(File directory) {
	    if(directory.exists()){
	        File[] files = directory.listFiles();
	        if(null!=files){
	            for(int i=0; i<files.length; i++) {
	                if(files[i].isDirectory()) {
	                    deleteDirectory(files[i]);
	                }
	                else {
	                    files[i].delete();
	                }
	            }
	        }
	    }
	    return(directory.delete());
	}
}
