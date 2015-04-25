package system_yok_exception.crypt_o_cloud;

import java.nio.file.Path;

public interface ICloudManager {
	public void uploadResource(Path path) throws Exception;
	public void downloadResource(String filename, Path path);
	public String[] listDir (String dirName);
	public String getAccountName() throws Exception;
	// public void authenticate() ?
}
