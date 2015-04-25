package system_yok_exception;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import org.junit.Before;
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
	
	@Test
	public void test() throws FileNotFoundException {
		CryptUtil cr = new CryptUtil(keyServ.getDefaultKey(), enc);
		//File b = new File("asdasdasdsa.txt");
		File b = new File("3.jpg");
		File encryptedFile = cr.encryptFile(b);
		System.out.println(encryptedFile.getName());
		File c = cr.decryptFile(encryptedFile, "tmp");
		System.out.println(c.getName());
		//File a = cr.encryptFile(new File("textew.txt"));
	}

}
