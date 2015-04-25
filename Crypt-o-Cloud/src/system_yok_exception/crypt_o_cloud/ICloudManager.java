package system_yok_exception.crypt_o_cloud;

import java.io.File;

public interface ICloudManager {
	
	//uploads "sourceFile" from local machine to "destinationPath" in Dropbox
	public File uploadResource(File sourceFile, String destinationPath) throws Exception;
	
	//downloads "resName" from Dropbox to "downloadedFile" on the local machine
	public File downloadResource(String resName, File downloadedRes) throws Exception;
	
	//lists file in the "dirPath" directory
	public String[] listDir (String dirPath) throws Exception;
	
	//returns the name of the currently selected account
	public String getAccountName() throws Exception;
	
	//deletes a file
	public void deleteResource(String filePath) throws Exception;
	
	//returns the name of the cloud service
	public String getCloudName();
}
