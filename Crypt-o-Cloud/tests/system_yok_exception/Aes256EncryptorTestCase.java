package system_yok_exception;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import system_yok_exception.utils.Aes256Encryptor;
import system_yok_exception.utils.FileKeyService;
import system_yok_exception.utils.IEncryptor;

public class Aes256EncryptorTestCase {
	private IEncryptor enc;
	private byte[] key;

	@Before
	public void setUp() {
		enc = new Aes256Encryptor();
		key = new FileKeyService().getDefaultKey();
	}

	@Test
	public void testEncryptAndDecryptBytes() {
		byte[] data = { 'M', 'y', ' ', 't', 'e', 's', 't', ' ', 'i', 's', ' ',
				'a', 'w', 'e', 's', 'o', 'm', 'e' };
		
		printArr(data);
		byte[] encryptedData = enc.encrypt(data, key);
		printArr(encryptedData);
		assertFalse(Arrays.equals(data, encryptedData));
		byte[] decryptedData = enc.decrypt(encryptedData, key);
		printArr(decryptedData);
		assertTrue(Arrays.equals(data, decryptedData));
	}
	
	@Test
	public void testEncryptAndDecryptStrings() {
		String data = "3.jpg";
		
		String encryptedData = enc.encrypt(data, key);
		
		System.out.println(encryptedData);
		
		assertFalse(data.equals(encryptedData));
		String decryptedData = enc.decrypt(encryptedData, key);
		
		System.out.println(decryptedData);
		
		assertTrue(data.equals(decryptedData));
	}
	
	private void printArr(byte[] bytes) {
		for(Byte b: bytes) {
			System.out.print(b);
			System.out.print(' ');
		}
		System.out.println("Length: " + bytes.length);
	}
}
