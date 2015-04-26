package system_yok_exception.crypt_o_cloud;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Stack;

import javax.swing.ImageIcon;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.AbstractTableModel;

class LocalFileBrowser extends AbstractTableModel {
	private static final long serialVersionUID = 1L;

	private List<File> files = new ArrayList<>();
	private File selectedFile;
	private Stack<List<File>> previous = new Stack<>();
	private Stack<File> previousFolders = new Stack<>();

	private FileSystemView fileSystemView = FileSystemView.getFileSystemView();
	private String[] columns = { "Icon", "File", "Path/name", "Size",
			"Last Modified" };

	public LocalFileBrowser() {
		File roots[] = fileSystemView.getRoots();
		for (File root : roots) {
			File[] filesInRoot = fileSystemView.getFiles(root, true);
			for (File file : filesInRoot)
				files.add(file);
		}
	}

	@Override
	public Object getValueAt(int row, int column) {
		if (!previous.isEmpty() && row == 0) {
			switch (column) {
			case 0:
				return new ImageIcon("icons/previous-folder-icon.png");
			case 1:
				return "..";
			case 2:
				return "previous folder";
			default:
				return null;
			}
		}
		File file = previous.isEmpty() ? files.get(row) : files.get(row - 1);
		switch (column) {
		case 0:
			return fileSystemView.getSystemIcon(file);
		case 1:
			return fileSystemView.getSystemDisplayName(file);
		case 2:
			return file.getPath();
		case 3:
			return file.length();
		case 4:
			return file.lastModified();
		}
		return "";
	}

	@Override
	public int getColumnCount() {
		return columns.length;
	}

	@Override
	public Class<?> getColumnClass(int column) {
		switch (column) {
		case 0:
			return ImageIcon.class;
		case 3:
			return Long.class;
		case 4:
			return Date.class;
		}
		return String.class;
	}

	@Override
	public String getColumnName(int column) {
		return columns[column];
	}

	@Override
	public int getRowCount() {
		return previous.isEmpty() ? files.size() : files.size() + 1;
	}

	private void setFiles(List<File> files) {
		this.files = files;
		fireTableDataChanged();
	}

	public void doubleClickedRow(int row) {
		if (!previous.isEmpty() && row == 0) {
			setFiles(previous.pop());
			previousFolders.pop();
		} else if (files.get(previous.isEmpty() ? row : --row).isDirectory()) {
			previous.push(files);
			previousFolders.push(files.get(row));
			setFiles(Arrays
					.asList(fileSystemView.getFiles(files.get(row), true)));
		}
	}

	public void singleClickedRow(int row) {
		if (row == 0) {
			selectedFile = null;
		} else
			selectedFile = previous.isEmpty() ? files.get(row) : files
					.get(row - 1);
	}

	public File getSelectedFile() {
		return selectedFile;
	}

	public File getCurrentFolder() {
		return previousFolders.peek();
	}

	public void updateCurrent() {
		if (previousFolders.isEmpty()) {
			File roots[] = fileSystemView.getRoots();
			for (File root : roots) {
				File[] filesInRoot = fileSystemView.getFiles(root, true);
				for (File file : filesInRoot)
					files.add(file);
			}
			fireTableDataChanged();
		} else {
			setFiles(Arrays.asList(fileSystemView.getFiles(
					previousFolders.peek(), true)));
		}

	}
}