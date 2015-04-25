package system_yok_exception.crypt_o_cloud.Dropbox;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Locale;

import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxEntry.WithChildren;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuthNoRedirect;
import com.dropbox.core.DbxWriteMode;
import com.fasterxml.jackson.core.util.TextBuffer;

import system_yok_exception.crypt_o_cloud.ICloudManager;
import system_yok_exception.crypt_o_cloud.Pair;

public class DropboxManager implements ICloudManager {

	private final String APP_KEY = "c6gamqdwze5e08g";
	private final String APP_SECRET = "jcmcmmefr6lajuv";
	private final String CLOUD_NAME = "Dropbox";
	private final String TOKEN_PATH = "\\dbxtoken";

	private String accessToken;
	private DbxRequestConfig config;
	private DbxClient client;

	public DropboxManager() throws Exception {
		config = new DbxRequestConfig("CryptoCloud/1.0", Locale.getDefault()
				.toString());

		try {
			BufferedReader textBufferedReader = new BufferedReader(
					new FileReader(new File(TOKEN_PATH)));
			accessToken = textBufferedReader.readLine();
			if (accessToken == null) {
				authenticate();
			}

			client = new DbxClient(config, accessToken);
			listDir("/");
			textBufferedReader.close();

		} catch (DbxException.InvalidAccessToken e) {
			authenticate();
		} catch (FileNotFoundException e) {
			authenticate();
		} catch (NoSuchFileException e) {
			authenticate();
		}

	}

	private void authenticate() throws IOException, DbxException {
		DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);

		// TODO: what is this for?

		DbxWebAuthNoRedirect webAuth = new DbxWebAuthNoRedirect(config, appInfo);

		// Have the user sign in and authorize your app.
		String authorizeUrl = webAuth.start();

		JEditorPane editor = new JEditorPane();
		editor.setEditorKit(JEditorPane
				.createEditorKitForContentType("text/html"));
		editor.setEditable(false);
		editor.setText("<a href=\"" + authorizeUrl
				+ "\">Click here</a> and enter your code: ");

		editor.addHyperlinkListener(new HyperlinkListener() {
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					if (Desktop.isDesktopSupported()) {
						try {
							Desktop.getDesktop().browse(e.getURL().toURI());
						} catch (IOException | URISyntaxException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
			}
		});

		String code = JOptionPane.showInputDialog(null, editor,
				"Connect to Dropbox", JOptionPane.QUESTION_MESSAGE);

		// String code = new BufferedReader(new
		// InputStreamReader(System.in)).readLine().trim();

		// TODO: Make user input go away!

		DbxAuthFinish authFinish = webAuth.finish(code);
		accessToken = authFinish.accessToken;

		PrintWriter writer = new PrintWriter(TOKEN_PATH);

		writer.print(accessToken);

		writer.close();
	}

	@Override
	public File uploadResource(final File sourceFile,final String destinationPath)
			throws Exception {

		if (sourceFile.isFile()) {
			FileInputStream inputStream = new FileInputStream(sourceFile);

			DbxEntry.File uploadedFile = client.uploadFile(destinationPath
					+ "/" + sourceFile.getName(), DbxWriteMode.add(),
					sourceFile.length(), inputStream);

			try {
				inputStream.close();
			} catch (IOException e) {
			}
		} else {
			
			new Thread(){
				
			    public void run(){
			      File[] children = sourceFile.listFiles();

					DbxEntry.Folder folder = new DbxEntry.Folder(destinationPath, null, true);
					
					//TODO: Exceptions
					for (File node : children) {
						try {
							uploadResource(node, destinationPath + "/" + sourceFile.getName());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
			    }
			  }.start();
			
		}

		return sourceFile;
	}

	@Override
	public File downloadResource(String resName, File downloadedRes)
			throws DbxException, IOException {

		//ArrayList<Pair<String, String>> folderContents = listDir(resname);
		
		//if()
		{
			FileOutputStream outputStream = new FileOutputStream(downloadedRes);
			client.getFile(resName, null, outputStream);
		}
		//else
		{
			
		}
		return downloadedRes;
	}

	@Override
	public ArrayList<Pair<String, String>> listDir(String dirPath)
			throws DbxException {

		WithChildren listing = client.getMetadataWithChildren(dirPath);
		int size = listing.children.size();
		ArrayList<Pair<String, String>> children = new ArrayList<Pair<String, String>>();

		for (int i = 0; i < size; i++) {

			Pair<String, String> pair = new Pair<>();

			pair.setFirst(listing.children.get(i).name);

			if (listing.children.get(i).isFile()) {
				pair.setSecond(TYPE_FILE);
			} else {
				pair.setSecond(TYPE_FOLDER);
			}

			children.add(pair);
		}

		return children;
	}

	@Override
	public String getAccountName() throws DbxException {

		return client.getAccountInfo().displayName;

	}

	@Override
	public void deleteResource(String resPath) throws DbxException {
		client.delete(resPath);

	}

	@Override
	public String getCloudName() {

		return CLOUD_NAME;
	}

}
