package system_yok_exception.crypt_o_cloud.Dropbox;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.Locale;

import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
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

import system_yok_exception.crypt_o_cloud.ICloudManager;
import system_yok_exception.crypt_o_cloud.Pair;
import system_yok_exception.utils.Aes256Encryptor;
import system_yok_exception.utils.CryptUtil;
import system_yok_exception.utils.FileKeyService;

public class DropboxManager implements ICloudManager {

	private final String APP_KEY = "c6gamqdwze5e08g";
	private final String APP_SECRET = "jcmcmmefr6lajuv";
	private final String CLOUD_NAME = "Dropbox";
	private final String TOKEN_PATH = "\\dbxtoken";

	private String accessToken;
	private DbxRequestConfig config;
	private DbxClient client;
	private CryptUtil cryptUtil;

	public DropboxManager() throws Exception {
		cryptUtil = new CryptUtil((new FileKeyService()).getDefaultKey(),
				new Aes256Encryptor());
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
			client = new DbxClient(config, accessToken);
		} catch (FileNotFoundException e) {
			authenticate();
			client = new DbxClient(config, accessToken);
		} catch (NoSuchFileException e) {
			authenticate();
			client = new DbxClient(config, accessToken);
		}

	}

	private void authenticate() throws IOException, DbxException {
		DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);

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
							e1.printStackTrace();
						}
					}
				}
			}
		});

		String code = JOptionPane.showInputDialog(null, editor,
				"Connect to Dropbox", JOptionPane.QUESTION_MESSAGE);

		DbxAuthFinish authFinish = webAuth.finish(code);
		accessToken = authFinish.accessToken;

		PrintWriter writer = new PrintWriter(TOKEN_PATH);

		writer.print(accessToken);

		writer.close();
	}

	@Override
	public File uploadResource(final File sourceFile,
			final String destinationPath) throws Exception {

		if (sourceFile.isFile()) {
			//File cryptedSourceFile = cryptUtil.encryptFile(sourceFile, "tmp");
			FileInputStream inputStream = new FileInputStream(sourceFile);

			client.uploadFile(
					destinationPath + "/" + sourceFile.getName(),
					DbxWriteMode.add(), sourceFile.length(), inputStream);

			try {
				inputStream.close();
			} catch (IOException e) {
			}
		} else {

			File[] children = sourceFile.listFiles();

			new DbxEntry.Folder(destinationPath, null, true);

			for (File node : children) {

				uploadResource(node,
						destinationPath + "/" + sourceFile.getName());
			}

		}

		return sourceFile;
	}

	@Override
	public File downloadResource(String resName, File downloadedRes)
			throws DbxException, IOException {
		
		File destination = new File(downloadedRes.getPath().toString() + "/"
				+ resName.substring(resName.lastIndexOf("/")));
		
		return downloadResourceExecute(resName, destination);				
	}

	private File downloadResourceExecute(String resName, File downloadedRes)
			throws DbxException, IOException {
		DbxEntry metadata = client.getMetadata(resName);

		if (metadata.isFile()) {
			(downloadedRes.getParentFile()).mkdirs();
			FileOutputStream outputStream = new FileOutputStream(downloadedRes);
			client.getFile(resName, null, outputStream);
		} else {
			ArrayList<Pair<String, String>> folderContents = listDir(resName);

			for (Pair<String, String> nodeInfo : folderContents) {
				downloadResourceExecute(resName + "/" + nodeInfo.getFirst(),
						new File(downloadedRes.getPath().toString() + "/"
								+ nodeInfo.getFirst()));
			}
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
