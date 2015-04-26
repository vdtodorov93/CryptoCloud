package system_yok_exception.crypt_o_cloud;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;

public class CloudFileBrowser extends AbstractTableModel {
	private static final long serialVersionUID = 1L;

	private List<Pair<String, String>> files = new ArrayList<>();
	private Pair<String, String> selectedFile;
	private Stack<List<Pair<String, String>>> previous = new Stack<>();

	private ICloudManager cloudManager;
	private String currentPath;
	private String[] columns = { "Icon", "File" };

	public void loadRoot(ICloudManager cloudManager) throws Exception {
		this.cloudManager = cloudManager;
		if (cloudManager != null) {
			currentPath = UserInterface.ROOT_FOLDER_IN_CLOUD;
			setFiles(cloudManager.listDir(currentPath));
		}
	}

	private ImageIcon loadFolderFileIcon(Pair<String, String> file) {
		return file.getSecond().equals(ICloudManager.TYPE_FOLDER) ? new ImageIcon(
				"icons/folder-icon.png") : new ImageIcon("icons/file-icon.png");
	}

	@Override
	public Object getValueAt(int row, int column) {
		if (!previous.isEmpty() && row == 0) {
			switch (column) {
			case 0:
				return new ImageIcon("icons/previous-folder-icon.png");
			case 1:
				return "..";
			}
		}
		switch (column) {
		case 0:
			return previous.isEmpty() ? loadFolderFileIcon(files.get(row))
					: loadFolderFileIcon(files.get(row - 1));
		case 1:
			return previous.isEmpty() ? files.get(row).getFirst() : files.get(
					row - 1).getFirst();
		}
		return "";
	}

	@Override
	public int getColumnCount() {
		return columns.length;
	}

	@Override
	public Class<?> getColumnClass(int column) {
		return column == 0 ? ImageIcon.class : String.class;
	}

	@Override
	public String getColumnName(int column) {
		return columns[column];
	}

	@Override
	public int getRowCount() {
		return previous.isEmpty() ? files.size() : files.size() + 1;
	}

	private void setFiles(List<Pair<String, String>> files) {
		this.files = files;
		fireTableDataChanged();
	}

	public void doubleClickedRow(int row) throws Exception {
		if (!previous.isEmpty() && row == 0) {
			setFiles(previous.pop());
			currentPath = currentPath
					.substring(0, currentPath.lastIndexOf('/'));
		} else if (files.get(previous.isEmpty() ? row : --row).getSecond()
				.equals(ICloudManager.TYPE_FOLDER)) {
			previous.push(files);
			currentPath += "/" + files.get(row).getFirst();
			setFiles(cloudManager.listDir(currentPath));
		}
	}

	public void singleClickedRow(int row) {
		if (row == 0 && !previous.isEmpty())
			selectedFile = null;
		else
			selectedFile = previous.isEmpty() ? files.get(row) : files
					.get(row - 1);
	}

	public Pair<String, String> getSelectedFile() {
		return selectedFile;
	}

	public String getCurrentPath() {
		return currentPath;
	}

	public void updateCurrent() throws Exception {
		setFiles(cloudManager.listDir(currentPath));
	}
}