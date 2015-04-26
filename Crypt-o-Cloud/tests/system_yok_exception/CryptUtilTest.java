package system_yok_exception;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import system_yok_exception.utils.Aes256Encryptor;
import system_yok_exception.utils.CryptUtil;
import system_yok_exception.utils.FileKeyService;
import system_yok_exception.utils.IEncryptor;
import system_yok_exception.utils.IKeyService;

public class CryptUtilTest {
	private final String hardcodedString = "hoho123213213";
	
	IEncryptor enc;
	IKeyService keyServ;
	
	@Before
	public void setUp() throws IOException {
		keyServ = new FileKeyService();
		enc = new Aes256Encryptor();
		PrintWriter pw = new PrintWriter("asdasdasdsa.txt");
		pw.write(hardcodedString);
		pw.close();
	}
//
//	@Test
//	public void testEncryptDecryptFile() throws FileNotFoundException {
//		CryptUtil cr = new CryptUtil(keyServ.getDefaultKey(), enc);
//		File b = new File("3.jpg");
//		File encryptedFile = cr.encryptFile(b);
//		System.out.println(encryptedFile.getName());
//		File c = cr.decryptFile(encryptedFile, "tmp");
//		System.out.println(c.getName());
//	}
//	
//	@Test
//	public void testEncryptDecrypt10Files() throws FileNotFoundException {
//		CryptUtil cr = new CryptUtil(keyServ.getDefaultKey(), enc);
//		for(int i = 1; i <= 10; i++) {
//			File b = new File(i + ".jpg");
//			File encryptedFile = cr.encryptFile(b);
//			File c = cr.decryptFile(encryptedFile, "tmp");
//			System.out.println(c.getName());
//		}
//	}
	
//
//	@Test
//	public void testEncryptDecryptName() {
//		for(int i = 0; i < 100; i++) {
//			String name = i + ".jpg";
//			String encryptedName = enc.encrypt(name, keyServ.getDefaultKey());
//			System.out.println("ENCRYPTED: " + encryptedName);
//		}		
//	}
	
//
//	@Test
//	public void testEncryptDecryptName() {
//		String p = Paths.get("az", "tmp/", "/mam").toString();
//		
//		System.out.println(p);
//	}
	
	@Test
	public void testEncryptDirectory() throws FileNotFoundException {
		CryptUtil cr = new CryptUtil(keyServ.getDefaultKey(), enc);
		File cryptedDir = cr.encryptDirectory(new File("testFolderBatko"));
		//System.out.println("GOTOVO");
		System.out.println(cryptedDir.getName());
		File decryptedDir = cr.decryptDirectory(cryptedDir, "tmp");
		System.out.println(decryptedDir.getName());
	}
	
	
	
	
}
