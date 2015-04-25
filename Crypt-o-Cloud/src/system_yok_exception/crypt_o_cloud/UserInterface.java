package system_yok_exception.crypt_o_cloud;

import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class UserInterface {
	private static void createUpperPanel(JPanel panel) {
		JPanel upperPanel = new JPanel();
		upperPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		upperPanel.setLayout(new BoxLayout(upperPanel, BoxLayout.Y_AXIS));
		
		JPanel userInfoPanel = new JPanel();
		userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.X_AXIS));
		
		JLabel accountNameLabel = new JLabel("Акаунт:");
		
		JPanel uploadDownloadPanel = new JPanel();
		uploadDownloadPanel.setLayout(new BoxLayout(uploadDownloadPanel, BoxLayout.X_AXIS));
		
		JButton uploadButton = new JButton("Upload");
		uploadDownloadPanel.add(uploadButton);

		JButton downloadButton = new JButton("Download");
		uploadDownloadPanel.add(downloadButton);

		upperPanel.add(uploadDownloadPanel);
		upperPanel.add(userInfoPanel);
		
		panel.add(upperPanel);
	}

	private static void createLowerPanelFolderStructure(JPanel panel) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		FileBrowser fileBrowser = new FileBrowser();
		panel.add(fileBrowser.getGui());
		fileBrowser.showRootFile();
	}

	private static JPanel getJPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		createUpperPanel(panel);
		createLowerPanelFolderStructure(panel);
		return panel;
	}

	private static void createAndShowGUI() {

		JFrame frame = new JFrame("Crypt-o-Cloud");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.add(getJPanel());

		// frame.setSize(800,600);
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String args[]) {
		createAndShowGUI();
	}
}
