package system_yok_exception.crypt_o_cloud.Dropbox;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.Locale;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuthNoRedirect;
import com.dropbox.core.DbxWriteMode;

import system_yok_exception.crypt_o_cloud.ICloudManager;

public class DropboxManager implements ICloudManager {

    final String APP_KEY = "c6gamqdwze5e08g";
    final String APP_SECRET = "jcmcmmefr6lajuv";
    
	private String accessToken;
	private DbxClient client;
	
	public DropboxManager() throws IOException, DbxException
	{
		authenticate();
		
	}
	
	private void authenticate() throws IOException, DbxException
	{
        DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);

        DbxRequestConfig config = new DbxRequestConfig("JavaTutorial/1.0",
            Locale.getDefault().toString());
        DbxWebAuthNoRedirect webAuth = new DbxWebAuthNoRedirect(config, appInfo);

        // Have the user sign in and authorize your app.
        String authorizeUrl = webAuth.start();
        //TODO: Add an explanation for the user what to do.
        //JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
        String code = new BufferedReader(new InputStreamReader(System.in)).readLine().trim();

        //TODO: Make user input go away!
        
        //DbxAuthFinish authFinish = webAuth.finish("VtwxzitUoI8DDDLx0PlLut5Gjpw3");
        DbxAuthFinish authFinish = webAuth.finish(code);
        accessToken = authFinish.accessToken;
        client = new DbxClient(config, accessToken);
	}
	
	@Override
	public void uploadResource(Path path) throws Exception {

		File inputFile = path.toFile();
		FileInputStream inputStream = new FileInputStream(inputFile);

		if (path.toFile().isFile()) {
			DbxEntry.File uploadedFile = client.uploadFile(path.toString(),
					DbxWriteMode.add(), inputFile.length(), inputStream);
		}

		try {
			inputStream.close();
		} catch (IOException e) {
		}

	}

	@Override
	public void downloadResource(String filename, Path path) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String[] listDir(String dirName) {
		// TODO Auto-generated method stub
		return null;
	}

	//Може да върне null при неуспех!
	@Override
	public String getAccountName() throws DbxException {

		return client.getAccountInfo().displayName;
		
	}

	
	
}
