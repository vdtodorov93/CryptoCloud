package system_yok_exception.crypt_o_cloud;

import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class UserInterface {
	private static void createUpperPanelButtons(JPanel panel) {
		JPanel upperPanel = new JPanel();
		upperPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		upperPanel.setLayout(new BoxLayout(upperPanel, BoxLayout.X_AXIS));

		JButton uploadButton = new JButton("Upload");
		upperPanel.add(uploadButton);
		
		JButton downloadButton = new JButton("Download");
		upperPanel.add(downloadButton);

		panel.add(upperPanel);
	}

	private static void createLowerPanelFolderStructure(JPanel panel) {
		
	}

	private static JPanel getJPanel() {
		JPanel panel = new JPanel();
		createUpperPanelButtons(panel);
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
