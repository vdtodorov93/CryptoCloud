package system_yok_exception.crypt_o_cloud;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CSV {
	private static final String SEPARATOR = ",";
	private static final String QUOTES = "\"";

	public static List<List<String>> readFile(File file) throws IOException {
		if (!file.getName().endsWith(".csv"))
			throw new IOException("Wrong type of file: must be .csv");
		List<List<String>> readed = new ArrayList<>();
		for (String line : Files.readAllLines(Paths.get(file.getPath()), StandardCharsets.UTF_8)) {
			List<String> row = new ArrayList<>();
			boolean openQuotes = false;
			StringBuilder col = new StringBuilder();
			for (String column : line.split(SEPARATOR))
				if (openQuotes) {
					col.append(SEPARATOR).append(column);
					if (column.endsWith("\"")) {
						col.deleteCharAt(col.length() - 1);
						row.add(col.toString());
						col.setLength(0);
						openQuotes = false;
					}
				} else if (column.startsWith("\""))
					if (column.endsWith("\""))
						if (column.length() > 1)
							row.add(column.substring(1, column.length() - 1));
						else
							row.add("");
					else {
						col.append(column.substring(1));
						openQuotes = true;
					}
				else
					row.add(column);
			readed.add(row);
		}
		return readed;
	}

	public static void writeFile(File file, List<List<String>> data)
			throws IOException {
		StringBuilder toWrite = new StringBuilder();
		for (List<String> line : data) {
			for (String cell : line)
				toWrite.append(QUOTES).append(cell).append(QUOTES)
						.append(SEPARATOR);
			if (!line.isEmpty())
				toWrite.deleteCharAt(toWrite.length() - 1);
			toWrite.append("\n");
		}
		if (!data.isEmpty())
			toWrite.deleteCharAt(toWrite.length() - 1);
		Files.write(Paths.get(file.getPath()), toWrite.toString().getBytes("UTF-8"));
	}
}
