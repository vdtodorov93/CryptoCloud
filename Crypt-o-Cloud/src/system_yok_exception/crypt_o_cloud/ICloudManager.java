package system_yok_exception.crypt_o_cloud;

import java.io.File;
import java.util.ArrayList;

public interface ICloudManager {
	//Types used in listDir to denote if a node is a file or a folder
	public final String TYPE_FILE = "file";
	public final String TYPE_FOLDER = "folder";
	
	//uploads "sourceFile" from local machine to "destinationPath" in the Cloud
	public File uploadResource(File sourceFile, String destinationPath) throws Exception;
	
	//downloads "resName" from the Cloud to "downloadedRes" directory on the local machine
	public File downloadResource(String resName, File downloadedRes) throws Exception;
	
	//lists file in the "dirPath" directory (Pair is an implemented type storing 
	//File name/File type pair (file or folder))
	public ArrayList<Pair<String, String>> listDir (String dirPath) throws Exception;
	
	//returns the name of the currently selected account
	public String getAccountName() throws Exception;
	
	//deletes a file
	public void deleteResource(String filePath) throws Exception;
	
	//returns the name of the cloud service
	public String getCloudName();
}
