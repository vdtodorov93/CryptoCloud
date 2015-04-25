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

import javax.crypto.CipherInputStream;

public class CryptUtil {
	private static final String temporateFolder = "tmp";
	
	private byte[] key;
	IEncryptor encryptor;
	
	public CryptUtil(byte[] key, IEncryptor enc) {
		this.key = key;
		this.encryptor = enc;
	}

	public File encryptFile(File file) throws FileNotFoundException {
		File targetFile = null;
		try {
			byte[] fileBytes = Files.readAllBytes(file.toPath());
			byte[] cipheredFile = encryptor.encrypt(fileBytes, key);
			String encryptedFileName = encryptor.encrypt(file.getName(), key);
			
			FileOutputStream fos = new FileOutputStream("tmp/" + encryptedFileName, true);
			fos.write(cipheredFile);
			fos.flush();
			fos.close();
			targetFile = new File("tmp/" + encryptedFileName);
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
			String decryptedName = encryptor.decrypt(originalName, key);
			
			FileOutputStream fos = new FileOutputStream("tmp/" + decryptedName, true);
			fos.write(decryptedFile);
			fos.flush();
			fos.close();
			targetFile = new File("tmp/" + decryptedName);
		} catch (IOException e) {
			System.out.println(e);
		}
		return targetFile;
	}
}
