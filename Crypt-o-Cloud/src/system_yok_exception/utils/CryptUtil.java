package system_yok_exception.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.crypto.CipherInputStream;

public class CryptUtil {
	private byte[] key;
	IEncryptor encryptor;
	
	public CryptUtil(byte[] key, IEncryptor enc) {
		this.key = key;
		this.encryptor = enc;
	}

	public File encryptFile(File file, String pathTo) throws FileNotFoundException {
		File targetFile = null;
		try {
			byte[] fileBytes = Files.readAllBytes(file.toPath());
			byte[] cipheredFile = encryptor.encrypt(fileBytes, key);
			String encryptedFileName = encodeFileName(encryptor.encrypt(file.getName(), key));
			String targetDestination = Paths.get(pathTo, encryptedFileName).toString();
			FileOutputStream fos = new FileOutputStream(targetDestination, true);
			fos.write(cipheredFile);
			fos.flush();
			fos.close();
			targetFile = new File(pathTo, encryptedFileName);
		} catch (IOException e) {
			System.out.println(e);
		}
		return targetFile;
	}

	public File decryptFile(File file, String dest) {
		File targetFile = null;
		try {
			byte[] fileBytes = Files.readAllBytes(file.toPath());
			byte[] decryptedFile = encryptor.decrypt(fileBytes, key);
			String originalName = file.getName();
			String decryptedName = decodeFileName(encryptor.decrypt(originalName, key));
			String finalDestination = Paths.get(dest, decryptedName).toString();
			//You get it? Final Destination ha-ha
			FileOutputStream fos = new FileOutputStream(finalDestination, true);
			fos.write(decryptedFile);
			fos.flush();
			fos.close();
			targetFile = new File(finalDestination);
		} catch (IOException e) {
			System.out.println(e);
		}
		return targetFile;
	}
	
	public File encryptDirectory(File file) throws FileNotFoundException {
		return encryptDirectory(file, "tmp");
	}
	
	public File encryptDirectory(File dir, String path) throws FileNotFoundException {
		if(dir.isFile()) {
			return encryptFile(dir, path);
		}
		else {
			String encryptedFileName = encryptor.encrypt(dir.getName(), key);
			File encryptedDirectory = new File(Paths.get(path, encryptedFileName).toString());
			encryptedDirectory.mkdirs();
			for(File f: dir.listFiles()) {
				encryptDirectory(f, encryptedDirectory.toString());
			}
			return encryptedDirectory;
		}
	}
	
	public File decryptDirectory(File dir, String path) {
		if(dir.isFile()) {
			return decryptFile(dir, path);
		}
		else {
			String decryptedFileName = encryptor.decrypt(dir.getName(), key);
			File decryptedDirectory = new File(Paths.get(path, decryptedFileName).toString());
			decryptedDirectory.mkdirs();
			for(File f: dir.listFiles()) {
				decryptDirectory(f, decryptedDirectory.toString());
			}
			return decryptedDirectory;
		}
	}
	
	//TODO: Maybe make this a bit more civilized later
	public String encodeFileName(String filename) {
		return filename.replace('/', '%').replace('\\', '$');
	}
	
	public String decodeFileName(String encodedFilename) {
		return encodedFilename.replace('%', '/').replace('$', '\\');
	}
}
